package view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import controller.ControllerException;
import controller.GeoTracker;
import controller.Gps;
import controller.UIController;
import model.Distance;
import model.ModelException;
import model.Path;
import model.Route;
import model.Waypoint;

public class View 
{
	//Classfields
	private UIController controller;
	private GeoTracker tracker;
	private Waypoint currentWaypoint;
	private boolean updated;
	private Gps GpsTester;

	//View constructor
	public View(UIController controller, GeoTracker tracker, Gps GpsTester)
	{
		this.controller = controller;
		this.tracker = tracker;
		this.GpsTester = GpsTester;
		this.currentWaypoint = new Waypoint(0,0,0); //Just for temp holder before GPS gives location
	}
	
	/**
	 * Display the routes to the screen at the beginning to show
	 * the user what routes are avaliable
	 * @throws ModelException 
	 * @throws ControllerException 
	**/
	public void displayRoutes() throws ControllerException
	{
		//Get all the routes
		Set<String> allRoutes = controller.getAllRoutes();
		System.out.println("For your current location we\n"
						 + "have these routes avaliable:");
		//Print info on each avaliable route
		for (String routeName : allRoutes) 
		{
			Route r = controller.selectRoute(routeName);
			List<Path> preseg = r.getAllRoute();
			//Check to see if any subroutes
			String output = "";
			for (Path seg : preseg)
			{
				if (seg instanceof Route) 
				{
					//Remove the * from the name
					//Cast to route
					Route routeInfo = (Route) seg;
					String subr = routeInfo.getName();
					//Format string
					output += " " + subr + ",";
				}
			} 
			
			//If no subroutes just print name
			if (output.isEmpty())
			{
				System.out.println("> "+r.getName());
			}
			else //else print name with the subroutes
			{
				//Remove last ','
				output = output.substring(0, output.length() - 1);
				//Add . at the end 
				output = output + ".";
				System.out.println("> "+routeName+" with subroute\\s"+ output);
			}
		}
	}
	
	/**
	 * Gives the user a preview of the route, so they can see it before
	 * they go on it
	**/
	public void routePreview()
	{
		Route route = null;
		//Get the route name
		try {
			//Get route info
			System.out.print("Enter route name: ");
			//Get routeName from the user
			String routeName = getUserString();
			route = controller.selectRoute(routeName);
		} catch (ControllerException e) {
			System.out.printf("Error has occured: %s\n", e.getMessage());
		}
		
		//Make sure route is a valid object
		if (route != null) {
			//Distance totalDistance = tracker.combineDistances(); -- fix me pls
			System.out.println("----------------------------\n");
			System.out.println("> Name: " + route.getName());
			System.out.println("> Description: " + route.getDescription()+"\n");
			//System.out.println("> Total distance: "+ totalDistance.getHorizontalDistance()+ "m \n"); -- fix me after pls
			System.out.println(route.toString().replaceAll("(?m)^", "\t"));
		}
	}
	
	/** User selects the route and the program switches to tracking mode **/
	public void routeSelect()
	{
		boolean menuUp = true;
		int option = 0;
		
		Route route = null;
		try {
			//Get route info
			System.out.print("Enter route name: ");
			//Get routeName from the user
			String routeName = getUserString();
			route = controller.selectRoute(routeName);
		} catch (ControllerException e) {
			System.out.printf("Error has occured: %s\n", e.getMessage());
		}
		
		//Make sure route is a valid object
		if (route != null) {
			System.out.println("----------------------------");
			System.out.println("Your chosen route is: " + route.getName());
			
			while (menuUp)
			{
				System.out.println("1. Start the route");
	            System.out.println("2. Exit to menu");
	            System.out.println("----------------------------");
				System.out.print("Your option: ");
				
				//Get user option
				option = getUserInt();
	            
	            switch(option)
				{
				case 1:
					//initialise the tracker
					tracker.trackingSetup(route, this);
					//begin
					trackingDisplay();
					menuUp = false;
					break;
				case 2:
					menuUp = false;
					break;
				default:
					System.out.println("Please choose a valid option");
				}
			}
		}
	}
	
	/**
	 * Updates the current waypoint for the UI to use
	 * 
	 * @param currentWaypoint
	**/
	public void updateCurrentWaypoint(Waypoint currentWaypoint) 
	{
		this.updated = true;
        this.currentWaypoint = currentWaypoint;
    }

	/**
	 * In charge of showing the tracking display, shows what the user
	 * is up to at the moment 
	**/
    public void trackingDisplay() 
    {
    	GpsTester.run();
        while (currentWaypoint != null) 
        {
        	Distance totalDistance = tracker.combineDistances();
        	//For some reason rounding wasnt working so im using this
        	DecimalFormat df = new DecimalFormat("###.##");
        	//Check if the data has been updated
    		this.updated = false;
    		
    		System.out.println("\n-------- Statistics --------");
    		System.out.println("GPS Location: "+tracker.toString());
    		System.out.println("Next waypoint: " + currentWaypoint.toString());
    		System.out.println("----- Remaining distances -----");
    		System.out.println("> Horizontal Distance: "+ df.format(totalDistance.getHorizontalDistance()));
    		System.out.println("> Vertical Climb: "+ totalDistance.getVerticalClimb());
    		System.out.println("> Vertical Descent: "+ totalDistance.getVerticalDescent());
    		System.out.println("----- Manual Input -----");
    		System.out.println("Manually confirm waypoint was reached");
    		System.out.println("1. Yes it was reached");
    		System.out.println("2. No it wasn't reached");
    		System.out.print("Your option: ");
    		//Loop until data has been updated
    		while (!updated)
    		{
    			//Get the users choice
    			int choice = getUserInt();
				
				//If the decide to go to the next waypoint
    			if (choice == 1) {
    				tracker.goToNextWaypoint();
                    System.out.println("Next waypoint set!");
                }
    			
    			//Else just print waiting
    			System.out.println("> Waiting...");
    			
    			//Delay by 1 second
        		try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e) {
    				System.out.printf("Error has occured: %s\n", e.getMessage());
    			}
    		}
        }
        System.out.println("----------------------------");
        System.out.println("> Route finished!");
    }
	
	/** 
	 * Shows the menu so the user can choose what they want todo 
	 * 
	 * @throws ModelException 
	 * @throws ControllerException 
	**/
	public void showMenu() throws ControllerException
    {
		boolean menuUp = true;
		int option = 0;
		
		System.out.println("----------------------------");
		System.out.println("     Welcome to Trekr");
		System.out.println("----------------------------");
		
		displayRoutes();
		
		System.out.println("----------------------------");
		System.out.println("You can pick the follow options:");
		System.out.println("1. Preview a route");
		System.out.println("2. Select a route");
		System.out.println("3. Update routes");
		System.out.println("4. Exit");
		
		while(menuUp)
		{
			System.out.println("----------------------------");
			System.out.print("Your option: ");
			option = getUserInt();
			
			//Options
			switch(option)
			{
			case 1:
				routePreview();
				break;
			case 2:
				routeSelect();
				break;
			case 3:
				controller.routeUpdate();
				System.out.println("----------------------------");
				displayRoutes();
				break;
			case 4:
				System.out.println("----------------------------");
				System.out.println("Thanks for using Trekr!");
				menuUp = false;
				break;
			}
		}
    }
	
    /**
     *  Gets the user integer input and validates it to make sure it
     *  is correct format
     *
     *  @return input (int)
     *  @throws ControllerException
    **/
	public int getUserInt()
	{
		Scanner sc = new Scanner(System.in);
		
		int input = 0;
		if(sc.hasNextInt()) 
		{
			//Get int input
			input = sc.nextInt();
        } 
		else 
		{
			System.out.println("Incorrect value provided, try again");
        }
		return input;		
	}
	
	/**
	 * Gets the user String input and validates it to make sure it 
	 * is correct format
	 * @param routeName 
	 * 
	 * @return input (String)
	 * @throws ControllerException
	**/
	public String getUserString()
	{
		Scanner sc = new Scanner(System.in);
		
		String input = null;
		if(sc.hasNext()) 
		{
			//Get string input
			input = sc.nextLine();
        } 
		else 
		{
        	System.out.println("Incorrect value provided, try again");
        }
		return input;		
	}

}
