package main;

import java.util.Collection;

/**
 * A node in the grid that has a voltage and {@link GridSection}s connected to
 * it
 * 
 * @author Christopher Olk
 *
 */
public interface Bus {

	/**
	 * Returns the p.u. voltage of the bus
	 * 
	 * @return
	 */
	public double getPuVoltage();

	/**
	 * Returns a collection of all {@link GridSection}s which are connected to
	 * this node
	 * 
	 * @return
	 */
	public Collection<GridSection> getConnectedPowerGridSections();

}
