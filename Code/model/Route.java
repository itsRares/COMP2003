package model;
import java.util.*;

public class Route implements Path
{
	//Classfields
	private String name;
	private String description;
	private List<Path> route;
	
	//Route Constructor
	public Route(String name, String description) 
	{
		this.name = name;
		this.description = description;
		this.route = new ArrayList<Path>();
	}
	
	//GETTERS
	
	/* Returns the name of the Route */
	public String getName() 
	{
		return name;
	}
	
	/* Returns the description of the Route */
	public String getDescription() 
	{
		return description;
	}

	/* Returns the startWaypoint of the Route */
	public Waypoint getStartWaypoint() 
	{
		return route.get(0).getStartWaypoint();
	}
	
	/* Returns the endWaypoint of the Route */
	public Waypoint getEndWaypoint() 
	{
		int size = route.size();
	    return route.get(size - 1).getEndWaypoint();
	}
	
	/* Returns a Map of all Route's subroutes */
	public List<Path> getAllRoute() 
	{
		return route;
	}
	
	//SETTERS

	/**
	 * Adds a new subroute given the name to the Route
	 * 
	 * @param name
	 * @param subroute
	 * 
	 * @throws ModelException
	**/
	public void addRoute(Path route) throws ModelException 
	{
		//Make sure name isnt empty
		if (name.isEmpty())
		{
			throw new ModelException("Subroute name cannot be empty");
		}
		
		//Makes sure subroute is a valid object
		if (route != null)
		{
			this.route.add(route);
		}
		else
		{
			throw new ModelException("Waypoint cannot be empty");
		}
	}
	
	/**
	 * Makes route object readable, creates a string where waypoints
	 * represent a '•' and segments represent '|' also a subroute
	 * begins at \ and ends at /
	 * 
	 * @return output (String)
	*/
	public String toString()
    {
		String output = "";
		for (Path p : route)
		{			
			if (p instanceof Route)
			{
				Route routeInfo = (Route) p;
				output = output + "•\n";
				output += " \\ - Subroute: "+routeInfo.getName() + "\n";
				output += routeInfo.toString().replaceAll("(?m)^", "  ");
				output += " / - Subroute End\n";
			}
			else
			{
				output = output + "•\n";
				output = output + "|\n";
			}
			
		}
		output = output + "•\n";
		return output;
    }
}
