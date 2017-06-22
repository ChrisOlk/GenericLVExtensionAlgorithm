package main;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;

/**
 * This class contains some test methods which ensure the proper working of the
 * code but do not have any explanatory value to the reader and can thus be
 * skipped.
 * 
 * @author Christopher Olk
 *
 */
public class TestGenericLVGridExtension {

	@Test
	public void testHaveOverloadsOccured() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		assertFalse(extender.haveOverloadsOccured());
		GridSection mockOverloadedSection = mock(GridSection.class);
		when(mockOverloadedSection.getAbsSpecificCurrent()).thenReturn(1.0);
		extender.reportCurrentOverload(mockOverloadedSection);
		assertTrue(extender.haveOverloadsOccured());

		extender = new GenericLVGridExtension(navigator);
		Bus mockBus = mock(Bus.class);
		extender.reportVoltageOverload(mockBus);
		assertTrue(extender.haveOverloadsOccured());
	}

	@Test
	public void testReportCurrentOverload() {

		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		assertFalse(extender.haveOverloadsOccured());
		GridSection mockOverloadedSection = mock(GridSection.class);
		when(mockOverloadedSection.getAbsSpecificCurrent()).thenReturn(1.0);
		extender.reportCurrentOverload(mockOverloadedSection);
	}

	@Test
	public void testReportVoltageOverload() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		Bus mockBus = mock(Bus.class);
		extender.reportVoltageOverload(mockBus);
	}

	@Test
	public void testCleanUp() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		assertFalse(extender.haveOverloadsOccured());
		GridSection mockOverloadedSection = mock(GridSection.class);
		when(mockOverloadedSection.getAbsSpecificCurrent()).thenReturn(1.0);
		extender.reportCurrentOverload(mockOverloadedSection);
		assertTrue(extender.haveOverloadsOccured());
		extender.cleanUp();
		assertFalse(extender.haveOverloadsOccured());

		extender = new GenericLVGridExtension(navigator);
		Bus mockBus = mock(Bus.class);
		extender.reportVoltageOverload(mockBus);
		assertTrue(extender.haveOverloadsOccured());
		extender.cleanUp();
		assertFalse(extender.haveOverloadsOccured());
	}

	@Test
	public void testGetRelieveFactorCurrent() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.getRelieveFactorCurrent();

		extender.setRelieveFactorCurrent(0.0);
		assertEquals(extender.getRelieveFactorCurrent(), 0.0, 0.0);
	}

	@Test
	public void testSetRelieveFactorCurrent() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.setRelieveFactorCurrent(0.0);
	}

	@Test
	public void testGetRelieveFactorVoltage() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.getRelieveFactorVoltage();

		extender.setRelieveFactorVoltage(0.0);
		assertEquals(extender.getRelieveFactorVoltage(), 0.0, 0.0);
	}

	@Test
	public void testSetRelieveFactorVoltage() {
		GridNavigator navigator = mock(GridNavigator.class);
		GenericLVGridExtension extender = new GenericLVGridExtension(navigator);
		extender.setRelieveFactorVoltage(0.0);
	}

}
