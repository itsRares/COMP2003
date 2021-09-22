package model;

public class Segment implements Path
{
	//Classfields
	private String description;
	private Waypoint startWaypoint;
	private Waypoint endWaypoint;
	
	//Segment constructor
	public Segment(String description, Waypoint startWaypoint) 
	{
		this.description = description;
		this.startWaypoint = startWaypoint;
	}
	
	//GETTERS

	/* Returns the description of the Segment */
	public String getDescription() 
	{
		return description;
	}

	/* Returns the startWaypoint of the Segment */
	public Waypoint getStartWaypoint()
	{
		return startWaypoint;
	}

	/* Returns the endWaypoint of the Segment */
	public Waypoint getEndWaypoint() 
	{
		return endWaypoint;
	}
	
	//SETTERS

	/**
	 * Sets the endWaypoint for the segment
	 * 
	 * @param endWaypoint
	 * 
	 * @throws ModelException
	**/
	public void setEndWaypoint(Waypoint endWaypoint) throws ModelException 
	{
		//Makes sure that waypoint is a valid object
		if (endWaypoint != null)
		{
			this.endWaypoint = endWaypoint;
		}
		else
		{
			throw new ModelException("Waypoint cannot be empty");
		}
	}
}
