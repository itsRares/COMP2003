package view;

import controller.GeoTracker;
import controller.GivenLocation;

public abstract class EventObserver 
{
	protected GivenLocation location;
	protected GeoTracker tracker;
	public abstract void updateLocation();
}
