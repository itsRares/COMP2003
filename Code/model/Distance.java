package model;

public class Distance
{
	//Classfields
	private double horizontalDistance;
	private double verticalClimb;
	private double verticalDescent;
	
	//Distance constructor
	public Distance()
	{
		this.horizontalDistance = 0;
		this.verticalClimb = 0;
		this.verticalDescent = 0;
	}
	
	/**
	 * Returns the horizontal distance
	 * 
	 * @return horizontalDistance (double)
	**/
	public double getHorizontalDistance()
	{
		return horizontalDistance;
	}
	
	/**
	 * Returns the verticalClimb distance
	 * 
	 * @return verticalClimb (double)
	**/
	public double getVerticalClimb()
	{
		return verticalClimb;
	}
	
	/**
	 * Returns the verticalDescent distance
	 * 
	 * @return verticalDescent (double)
	**/
	public double getVerticalDescent()
	{
		return verticalDescent;
	}
	
	/**
	 * Increment the horizontal distance
	 * 
	 * @param horizontalDistance
	**/
	public void setHorizontalDistance(double horizontalDistance)
	{
		this.horizontalDistance += roundTwoPoints(horizontalDistance);
	}
	
	/**
	 * Increment the vertical Descent
	 * 
	 * @param verticalDistance
	**/
	private void setVerticalDescent(double verticalDistance) 
	{
		this.verticalDescent = roundTwoPoints(verticalDistance);
	}

	/**
	 * Increment the vertical climb
	 * 
	 * @param verticalDistance
	**/
	private void setVerticalClimb(double verticalDistance) 
	{
		this.verticalClimb = roundTwoPoints(verticalDistance);
	}
	
	/**
	 * Find what type of vertical distance the value is, if it is above
	 * 0 (positive) then it is climb else it is descent
	 * 
	 * @param verticalDistance
	**/
	public void findVerticalType(double verticalDistance)
	{
		if (verticalDistance >= 0)
		{
			setVerticalClimb(verticalDistance);
		}
		else
		{
			setVerticalDescent(verticalDistance);
		}
	}	
	
	/**
	 * Round a number to 2 decimal places
	 * hmmm somehow doesnt work with horizontal distance?
	 * 
	 * @param number
	 * 
	 * @return number (double)
	**/
	public double roundTwoPoints(double number)
	{
		//Round 2dp
		return Math.round(number*100.0)/100.0;
	}
}
