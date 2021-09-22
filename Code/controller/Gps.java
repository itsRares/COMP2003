package controller;

import java.util.Timer;
import java.util.TimerTask;

public class Gps extends TimerTask 
{
	private GivenLocation location;
	private Timer time;
	
	public Gps(GivenLocation location) {
		this.location = location;
	}

	//Temp GPS method which tests/only works with mainRoute
	public void run()
	{
		TimerTask newGpsTest = new TimerTask()
		{
			@Override
			public void run() 
			{
				try {
					location.locationReceieved(-31.96, 115.80, 63.0);
					Thread.sleep(4000);
					location.locationReceieved(-31.95, 115.78, 45.3);
					Thread.sleep(4000);
					location.locationReceieved(-31.95, 115.77, 44.8);
					Thread.sleep(4000);
					location.locationReceieved(-31.93, 115.76, 43.0);
					Thread.sleep(4000);
					location.locationReceieved(-31.94, 115.75, 47.1);
					Thread.sleep(4000);
					location.locationReceieved(-31.93, 115.72, 40.1);
					Thread.sleep(4000);
					location.locationReceieved(-31.94, 115.75, 47.1);
					Thread.sleep(4000);
					location.locationReceieved(-31.94, 115.75, 55.3);
					Thread.sleep(4000);
					location.locationReceieved(-31.94, 115.75, 71.0);
					Thread.sleep(4000);
					location.locationReceieved(-31.93, 115.75, 108.0);
					Thread.sleep(4000);
					location.locationReceieved(-31.93, 115.75, 131.9);
					Thread.sleep(4000);
					location.locationReceieved(-31.92, 115.74, 128.1);
					Thread.sleep(4000);
					gpsStop();
				} catch (InterruptedException e) {
					System.out.printf("Error has occured: %s\n", e.getMessage());
				}
			}
			
			//Stop the GPS
			private void gpsStop()
			{
				time.cancel();
			}
		};
		time = new Timer(); // Instantiate Timer Object
		time.schedule(newGpsTest, 0, 1000); // Create Repetitively task for every 1 secs
	}
}
