package main;

public interface GridSection {
	
	public double getAbsSpecificCurrent();
	
	public Bus[] getConnectedBuses();
	
	public double getLength();

}
