package main;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

/**
 * This class shows how the algorithm works and can be called. See
 * {@link #testFindBusesToExtendBetween()} for more detail
 * 
 * @author Christopher Olk
 *
 */
public class ExampleGridsTestCases {

	@Test
	/**
	 * The methods called in this test showcase how the grid extension algorithm
	 * works. The grids are identical to the ones presented in the paper and the
	 * voltages and currents hardcoded here are the result of a loadflow
	 * calculation
	 */
	public void testFindBusesToExtendBetween() {
		testRadialCurrentOverload();
		testLoopCurrentOverload();
		testMeshedCurrentOverload();

		testRadialVoltageDeviation();
		testLoopVoltageDeviation();
		testMeshedVoltageDeviation();
	}

	// #region Setups

	private void testRadialCurrentOverload() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(40.99259916 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(73.18490378 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(34.26192197 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(27.47661157 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(20.64691148 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(13.78351268 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(6.897451658 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(61.31659695 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(49.27282644 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(37.0846535 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(24.78574048 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(12.41180751 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11 }));
		when(b11.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.990464103);
		when(b2.getPuVoltage()).thenReturn(0.982502702);
		when(b3.getPuVoltage()).thenReturn(0.976123884);
		when(b4.getPuVoltage()).thenReturn(0.971334115);
		when(b5.getPuVoltage()).thenReturn(0.968138244);
		when(b6.getPuVoltage()).thenReturn(0.966539502);
		when(b7.getPuVoltage()).thenReturn(0.982924914);
		when(b8.getPuVoltage()).thenReturn(0.968646276);
		when(b9.getPuVoltage()).thenReturn(0.957191007);
		when(b10.getPuVoltage()).thenReturn(0.94858068);
		when(b11.getPuVoltage()).thenReturn(0.942831478);
		when(b12.getPuVoltage()).thenReturn(0.939954184);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportCurrentOverload(slackb7);
		Bus[] result = extender.findBusesToExtendBetween();
		assertTrue(result[1] == slack);
		assertTrue(result[0] == b9);
	}

	private void testLoopCurrentOverload() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);
		GridSection b6b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });
		when(b6b12.getConnectedBuses()).thenReturn(new Bus[] { b6, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(49.77579465 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(64.14341095 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(43.03129665 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(36.21758883 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(29.34463677 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(22.42300371 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(15.46375556 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(52.30033834 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(40.30886681 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(28.2007319 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(16.00976495 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(3.771321928 / 275);
		when(b6b12.getAbsSpecificCurrent()).thenReturn(8.47835391 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6, b6b12 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11 }));
		when(b11.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12, b6b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.988389006);
		when(b2.getPuVoltage()).thenReturn(0.978344568);
		when(b3.getPuVoltage()).thenReturn(0.969885733);
		when(b4.getPuVoltage()).thenReturn(0.963028838);
		when(b5.getPuVoltage()).thenReturn(0.957787327);
		when(b6.getPuVoltage()).thenReturn(0.954171602);
		when(b7.getPuVoltage()).thenReturn(0.985036178);
		when(b8.getPuVoltage()).thenReturn(0.972824883);
		when(b9.getPuVoltage()).thenReturn(0.963406995);
		when(b10.getPuVoltage()).thenReturn(0.956814725);
		when(b11.getPuVoltage()).thenReturn(0.953070979);
		when(b12.getPuVoltage()).thenReturn(0.952188901);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportCurrentOverload(slackb7);
		Bus[] result = extender.findBusesToExtendBetween();
		assertTrue(result[1] == slack);
		assertTrue(result[0] == b9);
	}

	private void testMeshedCurrentOverload() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);

		GridSection b1b8 = mock(GridSection.class);
		GridSection b2b9 = mock(GridSection.class);
		GridSection b3b10 = mock(GridSection.class);
		GridSection b4b11 = mock(GridSection.class);
		GridSection b5b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });

		when(b1b8.getConnectedBuses()).thenReturn(new Bus[] { b1, b8 });
		when(b2b9.getConnectedBuses()).thenReturn(new Bus[] { b2, b9 });
		when(b3b10.getConnectedBuses()).thenReturn(new Bus[] { b3, b10 });
		when(b4b11.getConnectedBuses()).thenReturn(new Bus[] { b4, b11 });
		when(b5b12.getConnectedBuses()).thenReturn(new Bus[] { b5, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(65.78612203 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(47.73623652 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(46.38849452 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(33.77675505 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(23.46934726 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(14.33610058 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(6.975392047 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(35.93912163 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(36.66898614 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(30.43365246 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(21.75063583 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(11.79093736 / 275);

		when(b1b8.getAbsSpecificCurrent()).thenReturn(12.62765834 / 275);
		when(b2b9.getAbsSpecificCurrent()).thenReturn(5.766775958 / 275);
		when(b3b10.getAbsSpecificCurrent()).thenReturn(3.406897916 / 275);
		when(b4b11.getAbsSpecificCurrent()).thenReturn(2.193670902 / 275);
		when(b5b12.getAbsSpecificCurrent()).thenReturn(0.397094649 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2, b1b8 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3, b2b9 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4, b3b10 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5, b4b11 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6, b5b12 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9, b1b8 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10, b2b9 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11, b3b10 }));
		when(b11.getConnectedPowerGridSections())
				.thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12, b4b11 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12, b5b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.984680818);
		when(b2.getPuVoltage()).thenReturn(0.973897989);
		when(b3.getPuVoltage()).thenReturn(0.966056279);
		when(b4.getPuVoltage()).thenReturn(0.960612259);
		when(b5.getPuVoltage()).thenReturn(0.957288653);
		when(b6.getPuVoltage()).thenReturn(0.955671862);
		when(b7.getPuVoltage()).thenReturn(0.988886683);
		when(b8.getPuVoltage()).thenReturn(0.980525585);
		when(b9.getPuVoltage()).thenReturn(0.972003567);
		when(b10.getPuVoltage()).thenReturn(0.964938024);
		when(b11.getPuVoltage()).thenReturn(0.959892528);
		when(b12.getPuVoltage()).thenReturn(0.957158856);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportCurrentOverload(slackb1);
		Bus[] result = extender.findBusesToExtendBetween();
		assertTrue(result[1] == slack);
		assertTrue(result[0] == b2);
	}

	private void testRadialVoltageDeviation() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(40.99259916 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(62.30298268 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(34.26192197 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(27.47661157 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(20.64691148 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(13.78351268 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(6.897451658 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(52.15625275 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(41.88268079 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(31.50517837 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(21.04825056 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(10.53765251 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11 }));
		when(b11.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.990455618);
		when(b2.getPuVoltage()).thenReturn(0.982473953);
		when(b3.getPuVoltage()).thenReturn(0.976070076);
		when(b4.getPuVoltage()).thenReturn(0.971256251);
		when(b5.getPuVoltage()).thenReturn(0.9680418);
		when(b6.getPuVoltage()).thenReturn(0.966432997);
		when(b7.getPuVoltage()).thenReturn(0.98545917);
		when(b8.getPuVoltage()).thenReturn(0.973276432);
		when(b9.getPuVoltage()).thenReturn(0.963486696);
		when(b10.getPuVoltage()).thenReturn(0.956118634);
		when(b11.getPuVoltage()).thenReturn(0.951194195);
		when(b12.getPuVoltage()).thenReturn(0.94872823);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportVoltageOverload(b12);
		;
		Bus[] result = extender.findBusesToExtendBetween();
		assertTrue(result[1] == slack);
		assertTrue(result[0] == b8);
	}

	private void testLoopVoltageDeviation() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);
		GridSection b6b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });
		when(b6b12.getConnectedBuses()).thenReturn(new Bus[] { b6, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(46.81769445 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(56.36144563 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(40.07793907 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(33.2740025 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(26.41595043 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(19.51439042 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(12.58037464 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(46.22903062 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(35.98497712 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(25.65250215 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(15.25616534 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(4.821516533 / 275);
		when(b6b12.getAbsSpecificCurrent()).thenReturn(5.625292141 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6, b6b12 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11 }));
		when(b11.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12, b6b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.989086221);
		when(b2.getPuVoltage()).thenReturn(0.979737679);
		when(b3.getPuVoltage()).thenReturn(0.971972052);
		when(b4.getPuVoltage()).thenReturn(0.965804272);
		when(b5.getPuVoltage()).thenReturn(0.961246362);
		when(b6.getPuVoltage()).thenReturn(0.958307301);
		when(b7.getPuVoltage()).thenReturn(0.986860542);
		when(b8.getPuVoltage()).thenReturn(0.976075209);
		when(b9.getPuVoltage()).thenReturn(0.967674769);
		when(b10.getPuVoltage()).thenReturn(0.961683642);
		when(b11.getPuVoltage()).thenReturn(0.95811949);
		when(b12.getPuVoltage()).thenReturn(0.95699292);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportVoltageOverload(b6);
		Bus[] result = extender.findBusesToExtendBetween();
		assertTrue(result[1] == slack);
		assertTrue(result[0] == b8);
	}

	private void testMeshedVoltageDeviation() {
		Bus slack = mock(Bus.class);
		Bus b1 = mock(Bus.class);
		Bus b2 = mock(Bus.class);
		Bus b3 = mock(Bus.class);
		Bus b4 = mock(Bus.class);
		Bus b5 = mock(Bus.class);
		Bus b6 = mock(Bus.class);
		Bus b7 = mock(Bus.class);
		Bus b8 = mock(Bus.class);
		Bus b9 = mock(Bus.class);
		Bus b10 = mock(Bus.class);
		Bus b11 = mock(Bus.class);
		Bus b12 = mock(Bus.class);

		GridSection slackb1 = mock(GridSection.class);
		GridSection slackb7 = mock(GridSection.class);
		GridSection b1b2 = mock(GridSection.class);
		GridSection b2b3 = mock(GridSection.class);
		GridSection b3b4 = mock(GridSection.class);
		GridSection b4b5 = mock(GridSection.class);
		GridSection b5b6 = mock(GridSection.class);
		GridSection b7b8 = mock(GridSection.class);
		GridSection b8b9 = mock(GridSection.class);
		GridSection b9b10 = mock(GridSection.class);
		GridSection b10b11 = mock(GridSection.class);
		GridSection b11b12 = mock(GridSection.class);

		GridSection b1b8 = mock(GridSection.class);
		GridSection b2b9 = mock(GridSection.class);
		GridSection b3b10 = mock(GridSection.class);
		GridSection b4b11 = mock(GridSection.class);
		GridSection b5b12 = mock(GridSection.class);

		when(slackb1.getConnectedBuses()).thenReturn(new Bus[] { slack, b1 });
		when(slackb7.getConnectedBuses()).thenReturn(new Bus[] { slack, b7 });
		when(b1b2.getConnectedBuses()).thenReturn(new Bus[] { b1, b2 });
		when(b2b3.getConnectedBuses()).thenReturn(new Bus[] { b2, b3 });
		when(b3b4.getConnectedBuses()).thenReturn(new Bus[] { b3, b4 });
		when(b4b5.getConnectedBuses()).thenReturn(new Bus[] { b4, b5 });
		when(b5b6.getConnectedBuses()).thenReturn(new Bus[] { b5, b6 });
		when(b7b8.getConnectedBuses()).thenReturn(new Bus[] { b7, b8 });
		when(b8b9.getConnectedBuses()).thenReturn(new Bus[] { b8, b9 });
		when(b9b10.getConnectedBuses()).thenReturn(new Bus[] { b9, b10 });
		when(b10b11.getConnectedBuses()).thenReturn(new Bus[] { b10, b11 });
		when(b11b12.getConnectedBuses()).thenReturn(new Bus[] { b11, b12 });

		when(b1b8.getConnectedBuses()).thenReturn(new Bus[] { b1, b8 });
		when(b2b9.getConnectedBuses()).thenReturn(new Bus[] { b2, b9 });
		when(b3b10.getConnectedBuses()).thenReturn(new Bus[] { b3, b10 });
		when(b4b11.getConnectedBuses()).thenReturn(new Bus[] { b4, b11 });
		when(b5b12.getConnectedBuses()).thenReturn(new Bus[] { b5, b12 });

		when(slackb1.getAbsSpecificCurrent()).thenReturn(60.08866967 / 275);
		when(slackb7.getAbsSpecificCurrent()).thenReturn(42.8340611 / 275);
		when(b1b2.getAbsSpecificCurrent()).thenReturn(42.40127271 / 275);
		when(b2b3.getAbsSpecificCurrent()).thenReturn(30.94174157 / 275);
		when(b3b4.getAbsSpecificCurrent()).thenReturn(21.61240689 / 275);
		when(b4b5.getAbsSpecificCurrent()).thenReturn(13.40418069 / 275);
		when(b5b6.getAbsSpecificCurrent()).thenReturn(6.949517207 / 275);
		when(b7b8.getAbsSpecificCurrent()).thenReturn(32.73384303 / 275);
		when(b8b9.getAbsSpecificCurrent()).thenReturn(33.48177734 / 275);
		when(b9b10.getAbsSpecificCurrent()).thenReturn(27.85230156 / 275);
		when(b10b11.getAbsSpecificCurrent()).thenReturn(19.97354626 / 275);
		when(b11b12.getAbsSpecificCurrent()).thenReturn(10.88833183 / 275);

		when(b1b8.getAbsSpecificCurrent()).thenReturn(10.92649086 / 275);
		when(b2b9.getAbsSpecificCurrent()).thenReturn(4.630341629 / 275);
		when(b3b10.getAbsSpecificCurrent()).thenReturn(2.449534804 / 275);
		when(b4b11.getAbsSpecificCurrent()).thenReturn(1.292680945 / 275);
		when(b5b12.getAbsSpecificCurrent()).thenReturn(0.483235252 / 275);

		when(slack.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, slackb7 }));
		when(b1.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb1, b1b2, b1b8 }));
		when(b7.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { slackb7, b7b8 }));
		when(b2.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b1b2, b2b3, b2b9 }));
		when(b3.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b2b3, b3b4, b3b10 }));
		when(b4.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b3b4, b4b5, b4b11 }));
		when(b5.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b4b5, b5b6, b5b12 }));
		when(b6.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b5b6 }));
		when(b8.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b7b8, b8b9, b1b8 }));
		when(b9.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b8b9, b9b10, b2b9 }));
		when(b10.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b9b10, b10b11, b3b10 }));
		when(b11.getConnectedPowerGridSections())
				.thenReturn(Arrays.asList(new GridSection[] { b10b11, b11b12, b4b11 }));
		when(b12.getConnectedPowerGridSections()).thenReturn(Arrays.asList(new GridSection[] { b11b12, b5b12 }));

		when(slack.getPuVoltage()).thenReturn(1.0);
		when(b1.getPuVoltage()).thenReturn(0.985995263);
		when(b2.getPuVoltage()).thenReturn(0.976106792);
		when(b3.getPuVoltage()).thenReturn(0.968886946);
		when(b4.getPuVoltage()).thenReturn(0.963841894);
		when(b5.getPuVoltage()).thenReturn(0.96071197);
		when(b6.getPuVoltage()).thenReturn(0.959088802);
		when(b7.getPuVoltage()).thenReturn(0.99002215);
		when(b8.getPuVoltage()).thenReturn(0.982389553);
		when(b9.getPuVoltage()).thenReturn(0.974578585);
		when(b10.getPuVoltage()).thenReturn(0.968078141);
		when(b11.getPuVoltage()).thenReturn(0.963414961);
		when(b12.getPuVoltage()).thenReturn(0.960872325);

		GridNavigator navigator = mock(GridNavigator.class);
		when(navigator.getShortestRoute(b12, slack))
				.thenReturn(new GridSection[] { b11b12, b10b11, b9b10, b8b9, b7b8, slackb7 });
		when(navigator.getShortestRoute(b6, slack))
				.thenReturn(new GridSection[] { b5b6, b4b5, b3b4, b2b3, b1b2, slackb1 });

		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.reportVoltageOverload(b12);
		Bus[] result = extender.findBusesToExtendBetween();

		assertTrue(result[1] == slack);
		assertTrue(result[0] == b2);
	}

	// #endregion

}
