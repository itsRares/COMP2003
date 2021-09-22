package controller;

import model.Path;
import model.Route;
import model.Segment;
import model.Waypoint;

public class RouteFactory 
{
	/**
	 * We create an object and refer it to the common interface,
	 * all the checking is done prior
	 * 
	 * @param type
	 * @param name
	 * @param desc
	 * @param startWaypoint
	 * 
	 * @return path (Path)
	*/
	public Path buildRoute(String type, String name, String desc, Waypoint startWaypoint)
	{
		Path path = null;
		if (type.equalsIgnoreCase("ROUTE"))
		{
			path = new Route(name, desc);
		}
		else if (type.equalsIgnoreCase("SEGMENT"))
		{
			path = new Segment(desc, startWaypoint);
		}
		return path;
	}
}
