package main;

import java.util.ArrayDeque;
import java.util.Deque;

public class GenericLVGridExtension {

	private double relieveFactorCurrent = 0.4;
	private double relieveFactorVoltage = 0.7;

	private GridSection worstOverloadCurrent = null;

	private Bus worstOverloadVoltage = null;

	public boolean haveOverloadsOccured() {
		return (worstOverloadCurrent != null) || (worstOverloadVoltage != null);
	}

	public Bus[] findBusesToExtendBetween() {
		if (worstOverloadCurrent != null) {
			return handleCurrentOverloads();
		}
		if (worstOverloadVoltage != null) {
			return handleVoltageOverloads();
		}
		return null;
	}

	private Bus[] handleCurrentOverloads() {
		Bus highVoltageBus = iterativelyFindGridSectionsCurrentOverload(worstOverloadCurrent, true);
		Bus lowVoltageBus = iterativelyFindGridSectionsCurrentOverload(worstOverloadCurrent, false);
		return new Bus[]{lowVoltageBus, highVoltageBus};
	}

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
			if (getBusAtOpposingEnd(connectedSection, busToConsider).getPuVoltage() > busToConsider.getPuVoltage() == searchDirectionUp
					&& (nextSection == null || nextSection.getAbsSpecificCurrent() < connectedSection.getAbsSpecificCurrent())) {
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
		 * The next section does not carry enough current anymore to warrant a new cable beeing built. See eq (6)
		 */
		if (nextSection.getAbsSpecificCurrent() < worstOverloadCurrent.getAbsSpecificCurrent() * (1 - relieveFactorCurrent))
		{
			return busToConsider;
		}
		
		/*
		 * We have not reached the end of the search yet and will consequently
		 * continue the iterative search
		 */
		return iterativelyFindGridSectionsCurrentOverload(nextSection, searchDirectionUp);
	}

	private Bus[] handleVoltageOverloads() {
		Deque<GridSection> mainFeeder = new ArrayDeque<GridSection>();

		Bus highVoltageEnd = findMainFeederForVoltageDeviation(mainFeeder, true);
		Bus lowVoltageEnd = findMainFeederForVoltageDeviation(mainFeeder, false);
		
		System.out.println("HV bus: " + highVoltageEnd.getBusIndex() + ", LV bus: " + lowVoltageEnd.getBusIndex());

		GridSection[] feederAsArray = mainFeeder.toArray(new GridSection[] {});

		Bus centreBus = null;
		Integer indexMiddle = null;
		if (lowVoltageEnd.getPuVoltage() >= 1) {
			centreBus = lowVoltageEnd;
		} else {
			Bus currentBus = lowVoltageEnd;
			double deltaToRef = Math.abs(currentBus.getPuVoltage() - 1);
			for (int i = 0; i < mainFeeder.size(); i++) {
				currentBus = getBusAtOpposingEnd(feederAsArray[i], currentBus);
				double newDeltaToRef = Math.abs(currentBus.getPuVoltage() - 1);
				
				System.out.println("New candidate for centre bus: " + currentBus.getBusIndex());
				
				if (newDeltaToRef > deltaToRef) {
					break;
				}
				indexMiddle = i;
				centreBus = currentBus;
				deltaToRef = newDeltaToRef;
			}
		}
		
		for (int i = 0; i < feederAsArray.length; i++)
		{
			System.out.println("Grid section " + feederAsArray[i].getConnectedBuses()[0].getBusIndex() + ", " + feederAsArray[i].getConnectedBuses()[1].getBusIndex());
		}

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
		
		Bus highVoltageBus = centreBus;
		if (indexMiddle != feederAsArray.length) {
			double highVoltageCriterion = 1 + (relieveFactorVoltage*(highVoltageEnd.getPuVoltage() - 1));
			for (int i = indexMiddle + 1; i < feederAsArray.length; i--) {
				Bus newHighVoltageBus = getBusAtOpposingEnd(feederAsArray[i], highVoltageBus);
				if (highVoltageBus.getPuVoltage() < highVoltageCriterion) {
					break;
				}
				highVoltageBus = newHighVoltageBus;
			}
		}
		
		return new Bus[]{lowVoltageBus, highVoltageBus};
	}

	private Bus findMainFeederForVoltageDeviation(Deque<GridSection> mainFeeder, boolean searchingHigherVoltages) {
		Bus currentBus = worstOverloadVoltage;
		boolean stillSearchingForOtherEndOfFeeder = true;
		while (stillSearchingForOtherEndOfFeeder) {
			Bus nextBus = null;
			GridSection sectionBetweenBuses = null;
			for (GridSection connectedSection : currentBus.getConnectedPowerGridSections()) {
				Bus otherEnd = getBusAtOpposingEnd(connectedSection, currentBus);
				System.out.println("Other end: " + otherEnd.getBusIndex() + " with V: " + otherEnd.getPuVoltage());
				if (otherEnd.getPuVoltage() > currentBus.getPuVoltage() == searchingHigherVoltages
						&& (nextBus == null || sectionBetweenBuses.getAbsSpecificCurrent() < connectedSection.getAbsSpecificCurrent())) {
					nextBus = otherEnd;
					sectionBetweenBuses = connectedSection;
				}
			}
			if (nextBus == null) {
				stillSearchingForOtherEndOfFeeder = false;
				break;
			}
			else
			{
				currentBus = nextBus;
				if (searchingHigherVoltages) {
					mainFeeder.addLast(sectionBetweenBuses);
				} else {
					mainFeeder.addFirst(sectionBetweenBuses);
				}
			}
		}
		return currentBus;
	}
	
	private Bus getHighVoltageBus(GridSection section)
	{
		Bus[] connectedBuses = section.getConnectedBuses();
		if (connectedBuses[1].getPuVoltage() > connectedBuses[0].getPuVoltage())
		{
			return connectedBuses[1];
		}
		else
		{
			return connectedBuses[0];
		}
	}
	
	private Bus getLowVoltageBus(GridSection section)
	{
		Bus[] connectedBuses = section.getConnectedBuses();
		if (connectedBuses[1].getPuVoltage() < connectedBuses[0].getPuVoltage())
		{
			return connectedBuses[1];
		}
		else
		{
			return connectedBuses[0];
		}
	}
	
	private Bus getBusAtOpposingEnd(GridSection connectionBetweenBuses, Bus busThisEnd)
	{
		Bus[] connectedBuses = connectionBetweenBuses.getConnectedBuses();
		if (connectedBuses[0] == busThisEnd)
		{
			return connectedBuses[1];
		}
		if (connectedBuses[1] == busThisEnd)
		{
			return connectedBuses[0];
		}
		throw new IllegalArgumentException("The GridSection does not contain the bus and consequently no bus at the other end can be found.");
	}

	public void reportCurrentOverload(GridSection overloadedSection) {
		if (worstOverloadCurrent == null) {
			worstOverloadCurrent = overloadedSection;
		} else if (overloadedSection.getAbsSpecificCurrent() > worstOverloadCurrent.getAbsSpecificCurrent()) {
			worstOverloadCurrent = overloadedSection;
		}
	}

	public void reportVoltageOverload(Bus overloadedBus) {
		if (worstOverloadVoltage == null) {
			worstOverloadVoltage = overloadedBus;
		} else if (Math.abs(overloadedBus.getPuVoltage()-1) > Math.abs(worstOverloadVoltage.getPuVoltage()-1)) {
			worstOverloadVoltage = overloadedBus;
		}
	}

	public void cleanUp() {
		worstOverloadCurrent = null;
		worstOverloadVoltage = null;
	}

	public double getRelieveFactorCurrent() {
		return relieveFactorCurrent;
	}

	public void setRelieveFactorCurrent(double relieveFactorCurrent) {
		this.relieveFactorCurrent = relieveFactorCurrent;
	}

	public double getRelieveFactorVoltage() {
		return relieveFactorVoltage;
	}

	public void setRelieveFactorVoltage(double relieveFactorVoltage) {
		this.relieveFactorVoltage = relieveFactorVoltage;
	}

}
