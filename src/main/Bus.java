package main;

import java.util.Collection;

public interface Bus {
	
	public double getPuVoltage();
	
	public double getVoltageDev();
	
	public Collection<GridSection> getConnectedPowerGridSections();
	
	public int getBusIndex();

}
