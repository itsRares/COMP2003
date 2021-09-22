package view;

import controller.GeoTracker;
import controller.GivenLocation;
import model.Waypoint;

public class LocationObserver extends EventObserver 
{
	//LocationObserver constructor
	public LocationObserver(GivenLocation location, GeoTracker tracker)
	{
		this.location = location;
		this.tracker = tracker;
		this.location.attach(this);
	}

	/**
	 * Update location and override the other method
	**/
	@Override
	public void updateLocation() 
	{
		Waypoint local = location.getLocation();
		tracker.updateLocation(local.getLatitude(), local.getLongitude(), local.getAltitude());
	}

}
