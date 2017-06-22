package main;

/**
 * Each grid might have characteristics that can be exploited to speed up the
 * route-finding. A brute-force algorithm will be added later, but will not
 * scale well with grid size. The user is therefore invited to implement a
 * custom navigator.
 * 
 * @author Christopher Olk
 *
 */
public interface GridNavigator {

	/**
	 * Finds the shortest route with respect to absolute distance between the
	 * start bus and the goal bus.
	 * 
	 * @param startBus
	 *            The bus at which the sequence of {@link GridSection}s starts.
	 * @param goalBus
	 *            The bus at which the sequence of {@link GridSection}s ends.
	 * @return A sequence of {@link GridSection}s which follow the shortest path
	 *         from {@code startBus} to {@code goalBus}. The sequence is not
	 *         checked internally, so the implementation has to provide a
	 *         reliable path as otherwise errors will occur.
	 */
	public GridSection[] getShortestRoute(Bus startBus, Bus goalBus);
}
