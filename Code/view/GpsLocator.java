package view;

public abstract class GpsLocator {
	protected abstract void locationReceieved(
			double latitude, 
			double longitude, 
			double alitude);
}
