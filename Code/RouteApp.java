import controller.ControllerException;
import controller.GeoTracker;
import controller.GeoUtils;
import controller.GivenLocation;
import controller.Gps;
import controller.RouteController;
import controller.RouteFactory;
import controller.UIController;
import model.RouteModel;
import view.LocationObserver;
import view.MenuObserver;
import view.View;

public class RouteApp 
{
	public static void main(String[] args) 
	{
		RouteApp app = new RouteApp();
		app.start();
	}
	
	public void start() 
	{        
		//Create the RouteModel and RouteController.
		GeoUtils utils = new GeoUtils();
		RouteModel model = new RouteModel();
        RouteFactory factory = new RouteFactory();
        RouteController RouteController = new RouteController(model, factory, utils); 
        MenuObserver menuObs = new MenuObserver(RouteController);
        
        //Create the tracker, GPS and observer
        GeoTracker tracker = new GeoTracker();
        GivenLocation location = new GivenLocation();
        new LocationObserver(location, tracker);
        Gps GpsTester = new Gps(location);
        
        //Create the View and UIController.
        UIController UIController = new UIController(model, menuObs);
        View view = new View(UIController, tracker, GpsTester);
        try
        {
            //Update the route data and populate the model
        	RouteController.updateRouteData("RouteData.txt");
        	//Show menu
        	view.showMenu();
        }
        catch (ControllerException e) 
        {
        	System.out.printf("Error has occured: %s\n", e.getMessage());
            System.exit(1);
		}
	}
}
