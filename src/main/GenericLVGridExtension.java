package main;

/**
 * This class implements the algorithm presented in the paper.
 * 
 * @author Christopher Olk
 *
 */
public class GenericLVGridExtension {

	/**
	 * The relieve factor for thermal overloads, allowed between 0 and 1.
	 */
	private double relieveFactorCurrent = 0.4;

	/**
	 * The relieve factor for voltage bound violations, allowed between 0 and 1.
	 */
	private double relieveFactorVoltage = 0.7;

	/**
	 * Here the {@link GridSection} that has been reported to have the strongest
	 * thermal overload is stored.
	 */
	private GridSection worstOverloadCurrent = null;

	/**
	 * Here the {@link Bus} that has been reported to have the strongest voltage
	 * bound violation is stored.
	 */
	private Bus worstOverloadVoltage = null;

	/**
	 * The implementation of {@link GridNavigator} that the user has supplied is
	 * stored here.
	 */
	private final GridNavigator navigator;

	/**
	 * Constructor of {@link GenericLVGridExtension}
	 * 
	 * @param navigatorToUse
	 *            The implementation of {@link GridNavigator} that this class
	 *            should use
	 */
	public GenericLVGridExtension(GridNavigator navigatorToUse) {
		this.navigator = navigatorToUse;
	}

	/**
	 * Checks whether any overloads have been reported
	 * 
	 * @return True if any overloads have been reported, false if not.
	 */
	public boolean haveOverloadsOccured() {
		return (worstOverloadCurrent != null) || (worstOverloadVoltage != null);
	}

	/**
	 * The main method shown in the paper. It checks whether a thermal overload
	 * has occured and tries to fix that. If no thermal overload occured, it is
	 * checked whether a voltage bound violation has occured. If so, then it is
	 * attempted to fix it.
	 * 
	 * @return The two nodes between which a new cable should be built.
	 */
	public Bus[] findBusesToExtendBetween() {
		if (worstOverloadCurrent != null) {
			return handleCurrentOverloads();
		}
		if (worstOverloadVoltage != null) {
			return handleVoltageOverloads();
		}
		return null;
	}

	/**
	 * Handles the thermal overloads as described in the paper.
	 * 
	 * @return The two nodes between which a new cable should be built.
	 */
	private Bus[] handleCurrentOverloads() {
		Bus highVoltageBus = iterativelyFindGridSectionsCurrentOverload(worstOverloadCurrent, true);
		Bus lowVoltageBus = iterativelyFindGridSectionsCurrentOverload(worstOverloadCurrent, false);
		return new Bus[] { lowVoltageBus, highVoltageBus };
	}

	/**
	 * Helper-method for {@link #handleCurrentOverloads()} that actually
	 * implements the algorithm described in the paper.
	 * 
	 * @param lastSectionAlreadyExtended
	 *            The last {@link GridSection} that has been marked as one to be
	 *            extended in the current search direction.
	 * @param searchDirectionUp
	 *            True if the algorithm should follow the feeder towards
	 *            increasing voltages, false if it should follow decreasing
	 *            voltages.
	 * @return The bus at which the extension cable should be added.
	 */
	private Bus iterativelyFindGridSectionsCurrentOverload(GridSection lastSectionAlreadyExtended,
			boolean searchDirectionUp) {
		Bus busToConsider;
		if (searchDirectionUp) {
			busToConsider = getHighVoltageBus(lastSectionAlreadyExtended);
		} else {
			busToConsider = getLowVoltageBus(lastSectionAlreadyExtended);
		}

		/*
		 * We consider all grid sections connected to the bus and try to find
		 * the one fulfilling (2) or (3), depending on whether searchDirectionUp
		 * is true or false which tells us the search direction.
		 */
		GridSection nextSection = null;
		for (GridSection connectedSection : busToConsider.getConnectedPowerGridSections()) {
			if (getBusAtOpposingEnd(connectedSection, busToConsider).getPuVoltage() > busToConsider
					.getPuVoltage() == searchDirectionUp
					&& (nextSection == null
							|| nextSection.getAbsSpecificCurrent() < connectedSection.getAbsSpecificCurrent())) {
				nextSection = connectedSection;
			}
		}

		/*
		 * If no section has been found, it means that we have arrived at a
		 * local minimum and consequently can abort the search.
		 */
		if (nextSection == null) {
			return busToConsider;
		}

		/*
		 * The next section does not carry enough current anymore to warrant a
		 * new cable beeing built. See eq (6)
		 */
		if (nextSection.getAbsSpecificCurrent() < worstOverloadCurrent.getAbsSpecificCurrent()
				* (1 - relieveFactorCurrent)) {
			return busToConsider;
		}

		/*
		 * We have not reached the end of the search yet and will consequently
		 * continue the iterative search
		 */
		return iterativelyFindGridSectionsCurrentOverload(nextSection, searchDirectionUp);
	}

	/**
	 * Implements the algorithm described in the paper for voltage bound
	 * violations.
	 * 
	 * @return The buses between which the cable should be built to relieve the
	 *         voltage bound violation.
	 */
	private Bus[] handleVoltageOverloads() {
		/*
		 * First we find the local maximum and minimum voltage
		 */
		Bus highVoltageEnd = findMainFeederForVoltageDeviation(true);
		Bus lowVoltageEnd = findMainFeederForVoltageDeviation(false);

		/*
		 * We now let the navigator give us the shortest route between the two
		 * buses.
		 */
		GridSection[] feederAsArray = navigator.getShortestRoute(lowVoltageEnd, highVoltageEnd);

		/*
		 * This section searches for the bus in the sequence that has its
		 * voltage closest to reference voltage. We need it to start our
		 * extension algorithm from there
		 */
		Bus centreBus = null;
		Integer indexMiddle = null;
		if (lowVoltageEnd.getPuVoltage() >= 1) {
			centreBus = lowVoltageEnd;
		} else {
			Bus currentBus = lowVoltageEnd;
			double deltaToRef = Math.abs(currentBus.getPuVoltage() - 1);
			for (int i = 0; i < feederAsArray.length; i++) {
				currentBus = getBusAtOpposingEnd(feederAsArray[i], currentBus);
				double newDeltaToRef = Math.abs(currentBus.getPuVoltage() - 1);

				if (newDeltaToRef > deltaToRef) {
					break;
				}
				indexMiddle = i;
				centreBus = currentBus;
				deltaToRef = newDeltaToRef;
			}
		}

		/*
		 * The following section returns the bus at the low voltage side at
		 * which the extension cable should be added.
		 */
		Bus lowVoltageBus = centreBus;
		if (indexMiddle != 0) {
			double lowVoltageCriterion = 1 - (relieveFactorVoltage * (1 - lowVoltageEnd.getPuVoltage()));
			for (int i = indexMiddle; i >= 0; i--) {
				Bus newLowVoltageBus = getBusAtOpposingEnd(feederAsArray[i], lowVoltageBus);
				if (newLowVoltageBus.getPuVoltage() < lowVoltageCriterion) {
					break;
				}
				lowVoltageBus = newLowVoltageBus;
			}
		}

		/*
		 * The following section returns the bus at the high voltage side at
		 * which the extension cable should be added.
		 */
		Bus highVoltageBus = centreBus;
		if (indexMiddle != feederAsArray.length) {
			double highVoltageCriterion = 1 + (relieveFactorVoltage * (highVoltageEnd.getPuVoltage() - 1));
			for (int i = indexMiddle + 1; i < feederAsArray.length; i--) {
				Bus newHighVoltageBus = getBusAtOpposingEnd(feederAsArray[i], highVoltageBus);
				if (highVoltageBus.getPuVoltage() < highVoltageCriterion) {
					break;
				}
				highVoltageBus = newHighVoltageBus;
			}
		}

		return new Bus[] { lowVoltageBus, highVoltageBus };
	}

	/**
	 * Searches for the local maximum and minimum voltage, depending on
	 * {@code searchingHigherVoltages}
	 * 
	 * @param searchingHigherVoltages
	 *            True if the method should find the local maximum, false if it
	 *            should find the local minimum
	 * @return The local maximum or minimum voltage, depending on
	 *         {@code searchingHigherVoltages}
	 */
	private Bus findMainFeederForVoltageDeviation(boolean searchingHigherVoltages) {
		Bus currentBus = worstOverloadVoltage;
		boolean stillSearchingForOtherEndOfFeeder = true;
		while (stillSearchingForOtherEndOfFeeder) {
			Bus nextBus = null;
			GridSection sectionBetweenBuses = null;
			for (GridSection connectedSection : currentBus.getConnectedPowerGridSections()) {
				Bus otherEnd = getBusAtOpposingEnd(connectedSection, currentBus);
				if (otherEnd.getPuVoltage() > currentBus.getPuVoltage() == searchingHigherVoltages && (nextBus == null
						|| sectionBetweenBuses.getAbsSpecificCurrent() < connectedSection.getAbsSpecificCurrent())) {
					nextBus = otherEnd;
					sectionBetweenBuses = connectedSection;
				}
			}
			if (nextBus == null) {
				stillSearchingForOtherEndOfFeeder = false;
				break;
			} else {
				currentBus = nextBus;
			}
		}
		return currentBus;
	}

	/**
	 * Returns the bus at the cable with the higher voltage.
	 * 
	 * @param section
	 *            The section to find the bus with the higher voltage at.
	 * @return
	 */
	private Bus getHighVoltageBus(GridSection section) {
		Bus[] connectedBuses = section.getConnectedBuses();
		if (connectedBuses[1].getPuVoltage() > connectedBuses[0].getPuVoltage()) {
			return connectedBuses[1];
		} else {
			return connectedBuses[0];
		}
	}

	/**
	 * Returns the bus at the cable with the lower voltage.
	 * 
	 * @param section
	 *            The section to find the bus with the lower voltage at.
	 * @return
	 */
	private Bus getLowVoltageBus(GridSection section) {
		Bus[] connectedBuses = section.getConnectedBuses();
		if (connectedBuses[1].getPuVoltage() < connectedBuses[0].getPuVoltage()) {
			return connectedBuses[1];
		} else {
			return connectedBuses[0];
		}
	}

	/**
	 * Return the {@link Bus} that is at the other end of
	 * {@code connectionBetweenBuses}
	 * 
	 * @param connectionBetweenBuses
	 * @param busThisEnd
	 *            Already known bus, not the one to be returned.
	 * @return
	 */
	private Bus getBusAtOpposingEnd(GridSection connectionBetweenBuses, Bus busThisEnd) {
		Bus[] connectedBuses = connectionBetweenBuses.getConnectedBuses();
		if (connectedBuses[0] == busThisEnd) {
			return connectedBuses[1];
		}
		if (connectedBuses[1] == busThisEnd) {
			return connectedBuses[0];
		}
		throw new IllegalArgumentException(
				"The GridSection does not contain the bus and consequently no bus at the other end can be found.");
	}

	/**
	 * Use this method to supply the overloaded {@link GridSection} that turned
	 * out to be overloaded in your simulation. If a section has already been
	 * provided, it is checked which one of the two is carrying more specific
	 * current.
	 * 
	 * @param overloadedSection
	 *            The section of the grid that had a thermal overload.
	 */
	public void reportCurrentOverload(GridSection overloadedSection) {
		if (worstOverloadCurrent == null) {
			worstOverloadCurrent = overloadedSection;
		} else if (overloadedSection.getAbsSpecificCurrent() > worstOverloadCurrent.getAbsSpecificCurrent()) {
			worstOverloadCurrent = overloadedSection;
		}
	}

	/**
	 * Report the {@link Bus} that is experiencing a voltage bound violation. If
	 * a {@link Bus} has already been reported, the two are compared for the
	 * worse voltage bound violation.
	 * 
	 * @param overloadedBus
	 *            The bus that is overloaded
	 */
	public void reportVoltageOverload(Bus overloadedBus) {
		if (worstOverloadVoltage == null) {
			worstOverloadVoltage = overloadedBus;
		} else if (Math.abs(overloadedBus.getPuVoltage() - 1) > Math.abs(worstOverloadVoltage.getPuVoltage() - 1)) {
			worstOverloadVoltage = overloadedBus;
		}
	}

	/**
	 * Removes the reported overloads.
	 */
	public void cleanUp() {
		worstOverloadCurrent = null;
		worstOverloadVoltage = null;
	}

	/**
	 * Returns the relieve factor used for thermal overloads
	 * 
	 * @return
	 */
	public double getRelieveFactorCurrent() {
		return relieveFactorCurrent;
	}

	/**
	 * Sets a new relieve factor for thermal overloads.
	 * 
	 * @param relieveFactorCurrent
	 *            The new relieve factor. <b>Must be between 0 and 1</b>
	 * @throws IllegalArgumentException
	 *             if the relieve factor was not within 0 and 1
	 */
	public void setRelieveFactorCurrent(double relieveFactorCurrent) {
		if (relieveFactorCurrent > 1.0 || relieveFactorCurrent < 0) {
			throw new IllegalArgumentException(
					"The relieve factor for thermal overloads must be between 0 and 1, but was "
							+ relieveFactorCurrent);
		}
		this.relieveFactorCurrent = relieveFactorCurrent;
	}

	/**
	 * Returns the relieve factor used for voltage bound violations
	 * 
	 * @return
	 */
	public double getRelieveFactorVoltage() {
		return relieveFactorVoltage;
	}

	/**
	 * Sets a new relieve factor for voltage bound violations.
	 * 
	 * @param relieveFactorCurrent
	 *            The new relieve factor. <b>Must be between 0 and 1</b>
	 * @throws IllegalArgumentException
	 *             if the relieve factor was not within 0 and 1
	 */
	public void setRelieveFactorVoltage(double relieveFactorVoltage) {
		if (relieveFactorVoltage > 1.0 || relieveFactorVoltage < 0) {
			throw new IllegalArgumentException(
					"The relieve factor for voltage bound violations must be between 0 and 1, but was "
							+ relieveFactorVoltage);
		}
		this.relieveFactorVoltage = relieveFactorVoltage;
	}

}
