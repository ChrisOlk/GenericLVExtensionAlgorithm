package main;

public interface RouteFinder {


	public GridSection[] findShortestPathBetweenBuses(Bus startingBus, Bus goalBus);
}
