package main;

/**
 * A cable or power line in the LV grid with an absolute specific current, a
 * length, and {@link Bus}es connected to it
 * 
 * @author Christopher Olk
 *
 */
public interface GridSection {

	/**
	 * Returns the absolute current of the grid section divided by its capacity
	 * 
	 * @return
	 */
	public double getAbsSpecificCurrent();

	/**
	 * Returns the {@link Bus}es connected to this section. Note that the array
	 * may only contain 2 {@link Bus}es
	 * 
	 * @return
	 */
	public Bus[] getConnectedBuses();

}
