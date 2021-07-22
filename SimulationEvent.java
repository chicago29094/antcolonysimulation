import java.util.EventObject;

/**
 *	class SimulationEvent
 *
 *	represents an event that can occur in the simulation
 */
public class SimulationEvent extends EventObject
{
	
	/************
	 *	constants
	 ***********/
	
	// types of events
	
	// set up simulation for normal run
	public final static int NORMAL_SETUP_EVENT = 0;
	
	// display current statistics for the any colony
	public final static int DISPLAY_STATISTICS_EVENT = 1;

	// fire to decrease the running speed
	public final static int DECREASE_SPEED_EVENT = 2;
	
	// fire to increase the running speed
	public final static int INCREASE_SPEED_EVENT = 3;
	
	// run simulation continuously
	public final static int RUN_EVENT = 4;
	
	// run simulation one turn at a time
	public final static int STEP_EVENT = 5;
	
	
	/*************
	 *	attributes
	 ************/
	
	// type of event
	private int eventType;
	
	
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	create a new SimulationEvent
	 *
	 *	@param	source		Object on which event occurred
	 *	@param	eventType	type of event
	 */
	public SimulationEvent(Object source, int eventType)
	{
		super(source);
		this.eventType = eventType;
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	return the type of event this is
	 *
	 *	@return	a valid SimulationEvent type
	 */
	public int getEventType()
	{
		return eventType;
	}
}