package controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.Distance;
import model.Path;
import model.Route;
import model.Segment;
import model.Waypoint;
import view.View;

public class GeoTracker 
{
	//Classfields
	private Waypoint userLocation; //Stores the user's location as a waypoint
	private GeoUtils utils; //GeoUtils used to calcDistance
	private List<Waypoint> routeList; //List of route Waypoints
	private View view; //UI
	
	//GeoTracker constructor
	public GeoTracker()
	{
		this.utils = new GeoUtils();
		this.routeList = null;
		this.userLocation = new Waypoint(0,0,0);
		this.view = null;
	}
	
	/**
	 * Updates the location for the user by changing the values of the Waypoint
	 * 
	 * @param latitude
	 * @param longitude
	 * @param altitude
	**/
	public void updateLocation(double latitude, double longitude, double altitude)
	{
		this.userLocation.setLatitude(latitude);
		this.userLocation.setLongitude(longitude);
		this.userLocation.setAltitude(altitude);
		//Output to the UI, update the current waypoint (next one)
		view.updateCurrentWaypoint(findNextSegment());
	}
	
	/**
	 * Sets up the tracker, also gives up the route chosen and the UI
	 * so we can post stuff to the user when updating location. We also
	 * call the recursive setUp method explained below
	 * 
	 * @param route
	 * @param view
	**/
	public void trackingSetup(Route route, View view)
	{
		this.view = view;
		//List which holds all waypoints in order
		routeList = new LinkedList<Waypoint>();
		
		//Recursively go through the routes
		trackingSetupRecursive(route);
		//Add last waypoint to routeList
		routeList.add(route.getEndWaypoint());
	}
	
	/**
	 * Adds waypoints to the routeList so later we can use it. This
	 * method is recursive so it calls itself too (Only is the Path 
	 * is a route)
	 * 
	 * @param route
	**/
	public void trackingSetupRecursive(Route route)
	{
		//Get all paths
		List<Path> paths = route.getAllRoute();
		//Go through them
		for (Path p : paths)
		{		
			//Check if route
			if (p instanceof Route)
			{
				//Cast to route
				Route routeInfo = (Route) p;
				//Recursively go through and get segments
				trackingSetupRecursive(routeInfo);
			}
			else if (p instanceof Segment) //Else check if segment
			{
				//Cast to segment
				Segment routeInfo = (Segment) p;
				//Add to linked list of routes
				routeList.add(routeInfo.getStartWaypoint());
			}
		}
	}
	
	/**
	 * Finds the next waypoint if and only if the routeList size is
	 * not 0 and it is 10m horizontally and 2m vertically away from
	 * the waypoint else it returns the current waypoint again
	 * 
	 * @return nextWaypoint (Waypoint)
	**/
	public Waypoint findNextSegment()
	{
		Waypoint currentWaypoint = null;
		Waypoint nextWaypoint = null;
		boolean foundWaypoint = false;
		
		//While found a waypoint not true
		while (!foundWaypoint)
		{
			//What current waypoint trying to go to
			currentWaypoint = getCurrentWaypoint();
			//Make sure more than 0 waypoints left else end of route
			if (routeList.size() == 0)
			{
				//set to null (end of list) if 0 routes left
				nextWaypoint = null;
				foundWaypoint = true;
			}
			else if (nearWaypoint(currentWaypoint)) //else check if near waypoint
			{
				//If near then remove from the list
				//and go to the next one (reloop)
				routeList.remove(0);
			}
			else 
			{
				nextWaypoint = currentWaypoint;
				foundWaypoint = true;
			}
		}
		return nextWaypoint;
		
	}

	/**
	 * Method to check if the given distance is 10m horizontally
	 * and 2m vertically away from the next waypoint, it returns
	 * either true or false
	 * 
	 * @param currentSegment
	 * 
	 * @return boolean
	*/
	private boolean nearWaypoint(Waypoint currentSegment) 
	{
		double horiz, verti;
		//Calc distances
		horiz = getHorizontalDistance(userLocation, currentSegment);
		verti = getVerticalDistance(userLocation, currentSegment);
		//Lower than 10 or 2
		return (horiz <= 10) && (verti <= 2);
	}
	
	//todo
	public void goToNextWaypoint()
	{
		if (routeList.size() > 0)
		{
			routeList.remove(0);
		}
		view.updateCurrentWaypoint(findNextSegment());
	}
	
	/**
	 * Returns the current waypoint which the user is trying to 
	 * reach only if routeList is more than 0
	 * 
	 * @return current (Waypoint)
	**/
	private Waypoint getCurrentWaypoint()
	{
		Waypoint current = null;
		//Make sure more than one routes to prevent
		//nullpointer exception
		if (routeList.size() > 0)
		{
			//get first route (up next)
			current = routeList.get(0);
		}
		return current;
	}
	
	public Distance combineDistances()
	{
		//Add the remaining distance to the total distance
		//Mess but best could do
		Distance remaining = getRemainingDistance();
		Distance total = getTotalDistance();
		double remainingDistanceH = remaining.getHorizontalDistance();
		double remainingDistanceVC = remaining.getVerticalClimb();
		double remainingDistanceVD = remaining.getVerticalDescent();
		total.setHorizontalDistance(remainingDistanceH);
		total.findVerticalType(remainingDistanceVC);
		total.findVerticalType(remainingDistanceVD);
		return total;
	}
	
	/**
	 * Returns the remaining distance for the user to reach the
	 * waypoint by calculating the horizontal/vertical distance
	 * and returns it as a distance class (easy to read)
	 * 
	 * @return distance (Distance)
	**/
	public Distance getRemainingDistance()
	{
		Distance distance = new Distance();
		Waypoint current = getCurrentWaypoint();
		//Set the horizontal distance, use for later
		distance.setHorizontalDistance(getHorizontalDistance(userLocation, current));
		//Set the vertical distance, if its a negative it would be descent else climb
		distance.findVerticalType(getVerticalDistance(userLocation, current));
		return distance;
	}
	
	/**
	 * Gets the total distance of the entire route, then returns
	 * it as a distance class (easy to read). It also calls a 
	 * recursive method which does the calculating
	 * @param route 
	 * 
	 * @return distance (Distance)
	**/
	public Distance getTotalDistance()
	{
		Distance distance = new Distance();
		//Recursively find total distance
		getTotalDistanceRecursive(distance);
		return distance;
	}
	
	/**
	 * Recursively calculates the total distance of the route, depending
	 * if its a route or a segment depends on if the recursive method is
	 * called, only if its a route it's called
	 * 
	 * @param currentRoute
	 * @param distance
	**/
	public void getTotalDistanceRecursive(Distance distance)
	{
		Iterator<Waypoint> itr = routeList.iterator();
		Waypoint endWaypoint = null;
		Waypoint value;
		
	    while(itr.hasNext()) {
	    	value = itr.next();
	    	Waypoint startWaypoint = value;
	    	if (endWaypoint != null)
	    	{
	    		//Increment horizontal distance by counting the distance between each 
				//segment waypoint and adding it up
	    		distance.setHorizontalDistance(getHorizontalDistance(startWaypoint, endWaypoint));
				//Same with vertical distances
				distance.findVerticalType(getVerticalDistance(startWaypoint, endWaypoint));
	    	}
	    	endWaypoint = value;
	    }
	}
	
	/**
	 * Calculates the Horizontal distance given the start waypoint and
	 * end waypoint
	 * 
	 * @param startWaypoint
	 * @param endWaypoint
	 * 
	 * @return distance (double)
	**/
	public double getHorizontalDistance(Waypoint startWaypoint, Waypoint endWaypoint)
	{
		//Calc distance in metres
		return utils.calcMetresDistance(
				startWaypoint.getLatitude(), 
				startWaypoint.getLongitude(), 
				endWaypoint.getLatitude(), 
				endWaypoint.getLongitude()
				);
	}
	
	/**
	 * Calculates the Vertical difference given the start waypoint and 
	 * end waypoint
	 * 
	 * @param startWaypoint
	 * @param endWaypoint
	 * 
	 * @return distance (double)
	**/
	public double getVerticalDistance(Waypoint startWaypoint, Waypoint endWaypoint)
	{
		//Calc distance in metres
		return endWaypoint.getAltitude() - startWaypoint.getAltitude();
	}
	
	public String toString()
	{
		return userLocation.toString();
	}
	
}
