package controller;

import java.io.IOException;

public class GeoUtils {
	
	/**
	 * Given the stats it calculates the distance in metres of the nearest 
	 * waypoint for the user
	 * 
	 * @param lat1 - Current Latitude
	 * @param long1 - Current Longitude
	 * @param lat2 - Waypoint Latitude
	 * @param long2 - Waypoint Longitude
	 * 
	 * @return distance (double)
	**/
	public double calcMetresDistance(double lat1, double long1, double lat2, double long2)
	{
		return 6371000 * Math.acos(
                Math.sin((Math.PI * lat1)/180) *
                Math.sin((Math.PI * lat2)/180) +
                Math.cos((Math.PI * lat1)/180) *
                Math.cos((Math.PI * lat2)/180) *
                Math.cos((Math.PI * Math.abs(long1 - long2))/180));
	}
	
	/**
	 * Connects with the database and retrieves the latest version of 
	 * the route data
	 * 
	 * @return routeData (String)
	 * @throws IOException
	**/
	public String retrieveRouteData() throws IOException
	{
		//temp
		return 
		"theClimb You'll be climbing lots\n" +
		"-31.94,115.75,47.1,[description1]\n" +
        "-31.94,115.75,55.3,[description2]\n" +
        "-31.94,115.75,71.0,[description3]\n" +
        "-31.93,115.75,108.0,[description4]\n" +
        "-31.93,115.75,131.9\n"
        + "mainRoute Main route for the route\n"
        + "-31.96,115.80,63.0,[description5]\n"
        + "-31.95,115.78,45.3,[description6]\n"
        + "-31.95,115.77,44.8,*theStroll\n"
        + "-31.94,115.75,47.1,[description7]\n"
        + "-31.93,115.72,40.1,[description8]\n"
        + "-31.94,115.75,47.1,*theClimb\n"
        + "-31.93,115.75,131.9,[description9]\n"
        + "-31.92,115.74,128.1\n"
        + "theStroll Just a simple stroll\n"
        + "-31.95,115.77,44.8,[description10]\n"
        + "-31.93,115.76,43.0,[description11]\n"
        + "-31.94,115.75,47.1\n";
	}
}
