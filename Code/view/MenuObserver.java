package view;

import controller.RouteController;

public class MenuObserver extends EventObserver 
{
	//Classfields
	private RouteController controller;
	
	//MenuObserver constructor
	public MenuObserver(RouteController controller)
	{
		this.controller = controller;
	}

	/**
	 * Update the route data
	**/
	@Override
	public void updateLocation()
	{
		try {
			controller.updateRouteData("RouteData.txt");
			//Success Message
			System.out.println("----------------------------");
			System.out.println("Success Route data has been updated!");
		} catch (Exception e) {
			System.out.printf("Error has occured: %s\n", e.getMessage());
		}
	}

}
