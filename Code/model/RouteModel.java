package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteModel 
{
	//Classfields
    private List<Path> routes = new ArrayList<Path>(); //Used for validation
    private Map<String,Route> routeMap = new HashMap<>(); //Used to get route info

    /**
     * Returns a list of all the Paths
     * 
     * @return routes (List of Paths)
    */
    public List<Path> getRoutes()
    {
        return routes;
    }
    
    /**
     * Returns a Map of all the routes
     * 
     * @return routeMap (Map of Routes)
    */
    public Map<String, Route> getRouteMap() throws ModelException
    {
    	return routeMap;
    }

    /**
     * Returns a certain route given the correct key (the name of the route)
     * else it returns an exception error
     * 
     * @param name 
     * 
     * @return route (Route)
     * @throws ModelException
    **/
    public Route getRouteMap(String name) throws ModelException
    {
    	//Get the route given the key (name of route)
        Route route = routeMap.get(name);
        //Make sure route is a valid object
        if (route == null)
        {
        	throw new ModelException("Route '"+name+"' doesnt exist");
        }
        return route;
    }

    /**
     * Adds a certain route to the routeMap if valid and not in
     * the map already
     * 
     * @param route
     * 
     * @throws ModelException
    **/
    public void addRouteMap(Route route) throws ModelException 
    {
    	//Make sure route is a valid object
    	if (route == null)
    	{
    		throw new ModelException("Route not found");
    	}
    	
    	//Make sure route isnt already in the routeMap
        if(routes.contains(route))
        {
            throw new ModelException("Route already exists");
        }
        
        //Add to the route list
        routes.add(route);
        //Add to the route map
        routeMap.put(route.getName(), route);
    }

	/**
	 * Empties the data from the List and Map so we can update
	 * it with new data
	**/
	public void clearData() 
	{
		//Clear existing data from the map
		routes.clear();
		routeMap.clear();
	}
	
}
