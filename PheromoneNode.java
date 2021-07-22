
/**
 * PheromoneNode
 * 
 * The PheromoneNode Class defines a Comparable object type with values that can be used to rank and compare nodes 
 * based on their pheromone level and their desirability as a target destination for a Forager ant.
 * 
 * @author Harry Anastopoulos
 *
 */

public class PheromoneNode implements Comparable
{
	private int gridCol;
	private int gridRow;
	private int pheromoneLevel;
	private int visits;
	private int randomSalt;
	
	// Constructor
	public PheromoneNode(int pcol, int prow, int plevel, int psalt)
	{
		gridCol=pcol;
		gridRow=prow;
		pheromoneLevel=plevel;
		randomSalt=psalt;
	}
	
	// Getter methods
	public int getCol()
	{
		return gridCol;
	}
	
	public int getRow()
	{
		return gridRow;
	}
	
	public int getLevel()
	{
		return pheromoneLevel;
	}
	
	public int getSalt()
	{
		return randomSalt;
	}

	public int getVisits()
	{
		return visits;
	}
	
	// Set number of visits
	public void setVisits(int pvisits)
	{
		visits=pvisits;
	}
	
	// compareTo method for Comparable Class
	@Override
	public int compareTo(Object pObject) 
	{
		PheromoneNode compObject=(PheromoneNode) pObject;
		
		if (pheromoneLevel<compObject.getLevel())
		{
			return -1;
		}
		else if (pheromoneLevel>compObject.getLevel())
		{
			return 1;
		}
		else if (pheromoneLevel==compObject.getLevel())
		{
			if  (randomSalt<compObject.getSalt())
			{
				return -1;
			}
			else if  (randomSalt>compObject.getSalt())
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		
		return 0;
	}
}
