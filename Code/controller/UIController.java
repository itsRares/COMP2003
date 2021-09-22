package controller;

import java.util.Set;

import model.RouteModel;
import model.ModelException;
import model.Route;
import view.MenuObserver;

public class UIController 
{
	//Classfields
	private RouteModel model;
	private MenuObserver menuObs;

	//UIController constructor
	public UIController(RouteModel model, MenuObserver menuObs) 
	{
		this.model = model;
		this.menuObs = menuObs;
	}
	
	/**
	 * Asks the user to input a route name and it then returns that
	 * route to the user to be used
	 * @param routeName 
	 * 
	 * @return Route (Route)
	 * @throws ControllerException
	**/
	public Route selectRoute(String routeName) throws ControllerException
	{
		Route route = null;
		try 
		{
			//Get route from the routeMap
			route = model.getRouteMap(routeName);
		} 
		catch (ModelException e) 
		{
			throw new ControllerException(e.getMessage());
		}
		return route;
	}
	
	public Set<String> getAllRoutes() throws ControllerException
	{
		try {
			return model.getRouteMap().keySet();
		} catch (ModelException e) {
			throw new ControllerException(e.getMessage());
		}
	}
	
	/** Update the routes data, done by the controller method **/
	public void routeUpdate()
	{
		menuObs.updateLocation();
	}
}
