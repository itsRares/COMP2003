package model;

public class Waypoint 
{
	//Classfields
	private double latitude;
	private double longitude;
	private double altitude;
	
	//Waypoint Constructor
	public Waypoint(double latitude, double longitude, double altitude) 
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}
	
	//GETTERS
	
	/* Returns the latitude of the Waypoint */
	public double getLatitude() {
		return latitude;
	}
	
	/* Returns the longitude of the Waypoint */
	public double getLongitude() {
		return longitude;
	}
	
	/* Returns the altitude of the Waypoint */
	public double getAltitude() {
		return altitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setAltitude(double altitude)
	{
		this.altitude = altitude;
	}
	
	public String toString()
	{
		return "Lati: " + latitude +", Longi: " + longitude + ", Alti: " + altitude;
	}
}
