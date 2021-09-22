package controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import model.RouteModel;
import model.ModelException;
import model.Path;
import model.Route;
import model.Segment;
import model.Waypoint;

public class RouteController 
{
	//Classfields
	private RouteModel model;
	private RouteFactory factory;
	private GeoUtils utils;
	private Segment prevSegment;

	//Controller constructor
    public RouteController(RouteModel model, RouteFactory factory, GeoUtils utils)
    {
        this.model = model;
        this.factory = factory;
        this.utils = utils;
        this.prevSegment = null;
    }
    
    /**
     * Given a route name it retrieves the route from the routeMap
     * 
     * @param name
     * 
     * @return route (Route)
     * @throws ControllerException
    **/
    public Route getRoute(String name) throws ControllerException
    {
    	Route route = null;
		try 
		{
			//Get route from the routeMap
			route = model.getRouteMap(name);
		} 
		catch (ModelException e) 
		{
			throw new ControllerException(e.getMessage());
		}
		return route;
    }
	
	/**
	 * Updates the route data by retrieving it from the GeoUtils
	 * and then it puts it into the routeData.txt file
	 * 
	 * @param filename 
	 * 
	 * @throws ControllerException
	 * @throws  
	**/
	public void updateRouteData(String filename) throws ControllerException
	{
		//Firstly update the file holding the routeData
		try {
			//Get the routeData from the server
			String data = utils.retrieveRouteData();
			BufferedWriter writer = null;
			try 
			{
				//Create a new file
				File file = new File("RouteData.txt");
				//Create a new BufferedWriter
				writer = new BufferedWriter(new FileWriter(file));
				//Write data to the file
				writer.write(data);
				//Close the writer
				writer.close();
			} 
			catch (IOException ie) 
			{
				throw new ControllerException(String.format("IO error occurred while writing to 'routeData.txt': '%s'",
		 ie.getMessage()));
			}
		} 
		catch (Exception e) 
		{
			throw new ControllerException("Updating data did not complete");
		}
		
		//Clear existing data from the map
		model.clearData();
		
		//Update the maps with new data
		readFrom(filename);
	}
	
	/**
     * Read the file and sort through each line and check if it is
     * a route or a waypoint/segment and send to relavent method
     * 
     * This method uses mutliple readers so that it can firstly read
     * in the routes and then read in the waypoints, this is done to
     * make sure the subroutes can store information of routes below it
     * in the file
     * 
     * @param filename
     * 
     * @throws ModelException
    **/
	public void readFrom(String filename) throws ControllerException
	{
		boolean findRoute = true;
		String name = null;
		try
        {
			BufferedReader routeReader = null, waySegReader = null;
			
			//Create BufferedReader to read only the routes from file
			routeReader = new BufferedReader(new FileReader(filename));
			//Read line
            String line = routeReader.readLine();
            while(line != null)
            {
            	//Trim extra spaces
                if(line.trim().length() > 0)
                {
	            	//Parse each line
	            	readRoutes(line);
	            	//Get next line
	            	line = routeReader.readLine();
                }
            }
            //Close routeReader
            routeReader.close();
            
            //Create BufferedReader to read waypoints/segements from
            //the routes from file
            waySegReader = new BufferedReader(new FileReader(filename));
            //Read line
            line = waySegReader.readLine();
            while(line != null)
            {
            	//Trim extra spaces
                if(line.trim().length() > 0)
                {
                	//If line is a route instead of waypoint/segment
                	//Route is added above, this just gives the waypoint/segement
                	//the routes name
                	if (findRoute == true) 
                	{
                		String[] routeInfo = line.split(" ", 2);
                		name = routeInfo[0];
                        findRoute = false;
                	}
                	else //Cant be a route so is waypoint/segement
                	{
                		String desc = null;
                		String[] parts = line.split(",", 4); 
                		
                		//If length is 3 then that means its the end of the route
                		if (parts.length == 3) {
                			desc = null;
                			findRoute = true;
                		} 
                		else //Else the route hasnt ended and it continues
                		{
                			desc = parts[3];
                		}
                		//Check to make sure you didnt cheekly not include a value to break 
                		//the whole program
                		if (!parts[0].isEmpty() && !parts[1].isEmpty() && !parts[2].isEmpty())
                		{
                			Route route = getRoute(name);
                			
                    		//More parsing of data
                    		parseRouteInfo(route,parts[0],parts[1],parts[2],desc);
                		} 
                		else //If empty throw error
                		{
                			throw new ControllerException("Lati/Longi/Alti-tude cannot be empty");
                		}
                	}
                }   
                //Read next line
                line = waySegReader.readLine();
            }
            //Close reader - not working hmmm?
            waySegReader.close();
        }
        catch(FileNotFoundException e)
        {
            throw new ControllerException(String.format(
                "'%s' not found", filename));
        }
        catch(IOException e)
        {
            throw new ControllerException(String.format(
                "IO error occurred while reading '%s': '%s'",
                filename, e.getMessage()));
        }
	}
	
	/**
     * Checks to make sure the given line is a route and if it is
     * then it is parsed in the parseRoute method
     * 
     * @param line
     * 
     * @throws ModelException
    **/
    public void readRoutes(String line) throws ControllerException
    {
    	//Split line at space into 2
    	String[] routeInfo = line.split(" ", 2);     
    	//If length is only 2 then it means its a route
    	if (routeInfo.length == 2)
    	{
    		//Parse and add to list/map
    		parseRoute(routeInfo[0], routeInfo[1]);
    	}
	}
	
	/**
	 * Parses the given information from the line and creates a route
	 * 
	 * @param name
	 * @param desc
	 * 
	 * @throws ModelException
	*/
	public void parseRoute(String name, String desc) throws ControllerException
	{
		//Make sure name is valid
		if (name == null)
		{
			throw new ControllerException("Name of the route cannot be left empty");
		}
		
		//Make sure desc is valid
		if (desc == null)
		{
			throw new ControllerException("Description of the route cannot be left empty");
		}
		
		//Create new route
		Path builtRoute = factory.buildRoute("route", name, desc, null);
				
		if (builtRoute instanceof Route) {
		    // Guaranteed to succeed, barring classloader shenanigans
			Route newRoute = (Route) builtRoute;
			//Add route to the map
			try {
				model.addRouteMap(newRoute);
			} catch (ModelException e) {
				throw new ControllerException(e.getMessage());
			}
		}
		else
		{
			throw new ControllerException("Failed to factory build route");
		}
	}
	
	/**
	 * Parses the given information from the line and creates either
	 * a waypoint/segement/subroute and stores it into the route's
	 * info
	 * 
	 * @param name
	 * @param s_lati
	 * @param s_longi
	 * @param s_alti
	 * @param desc
	 * @param waypoint
	 * 
	 * @throws ModelException
	**/
	public void parseRouteInfo(Route route, String s_lati, String s_longi, 
			String s_alti, String desc) throws ControllerException
	{
		double lati = 0, longi = 0, alti = 0;
		if (route == null)
		{
			throw new ControllerException("Invalid route have been given");
		} 
		
		//Lati/Longi/Alti-tude validation
		if (s_lati.matches("[a-zA-Z]+") || s_longi.matches("[a-zA-Z]+") || s_alti.matches("[a-zA-Z]+"))
		{
			throw new ControllerException("Lati/Longi/Alti-tude can only be numbers");
		}
		
		//Parse data to make it into a double from a string
		lati = Double.parseDouble(s_lati);
		longi = Double.parseDouble(s_longi);
		alti = Double.parseDouble(s_alti);
		
		//Create new waypoint
		Waypoint way = new Waypoint(lati, longi, alti);
		
		//Check if its the end of the route
		if (desc == null) {
			//Do nothin, just prevent anything else to be done
			if (prevSegment != null) 
			{
				//Set the endWaypoint for the segement
				try {
					prevSegment.setEndWaypoint(way);
				} catch (ModelException e) {
					throw new ControllerException(e.getMessage());
				}
			}
			prevSegment = null;
		}
		else if (desc.substring(0, 1).equals("*")) //See if segment is a subroute
		{
			//Remove the * from the string
			String subDesc = desc.substring(1);
			//Get the route info for subroute
			Route subroute = getRoute(subDesc);
			//Add subroute to list of subroutes for the route
			try {
				route.addRoute(subroute);
			} catch (ModelException e) {
				throw new ControllerException(e.getMessage());
			}
		}
		else
		{
			
			//Create a new segment
			Path builtSegment = factory.buildRoute("segment", "none", desc, way);
			if (builtSegment instanceof Segment) {
			    // Guaranteed to succeed, barring classloader shenanigans
				Segment newSegment = (Segment) builtSegment;
				//Add a new segement for route
				try {
					route.addRoute(newSegment);
				} catch (ModelException e) {
					throw new ControllerException(e.getMessage());
				}
				//See if last added segement was null
				if (prevSegment != null) 
				{
					//Set the endWaypoint for the segement
					try {
						prevSegment.setEndWaypoint(way);
					} catch (ModelException e) {
						throw new ControllerException(e.getMessage());
					}
				}
				prevSegment = newSegment;
			}
			else
			{
				throw new ControllerException("Failed to factory build segment");
			}
			
			
		}
	}
}
