package controller;

import java.util.ArrayList;
import java.util.List;

import model.Waypoint;
import view.EventObserver;
import view.GpsLocator;
import view.LocationObserver;

public class GivenLocation extends GpsLocator 
{

	private List<EventObserver> eventObservers = new ArrayList<EventObserver>();
	private Waypoint location = new Waypoint(0,0,0); //Create an empty location
	
	/**
	 * Get the current location
	 * 
	 * @return location (Waypoint)
	**/
	public Waypoint getLocation()
	{
		return location;
	}
	
	/**
	 * Sets the location data and then call the observers
	**/
	public void locationReceieved(double latitude, double longitude, double altitude)
	{
		this.location.setLatitude(latitude);
		this.location.setLongitude(longitude);
		this.location.setAltitude(altitude);
		notifyAllObservers();
	}
	
	/**
	 * Attaches a new observer
	 * 
	 * @param location
	**/
	public void attach(LocationObserver location) 
	{
		eventObservers.add(location);
	}
	
	/**
	 * Notifies all the observers of a change
	**/
	public void notifyAllObservers()
	{
		for (EventObserver eventObserver: eventObservers)
		{
			eventObserver.updateLocation();
		}
	}
}
