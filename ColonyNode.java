import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * ColonyNode
 * 
 * The ColonyNode data object keeps track of all ants, food, and pheromone levels in a colony grid square.
 * ColonyNode also maintains a LinkedList data structure of all ants currently occupying the square.
 * 
 * @author Harry Anastopoulos
 *
 */
public class ColonyNode
{
	/* Class field variables */
	
	// A reference to the corresponding colony GUI square
	private ColonyNodeView guiNode=null;
	// A unique string identifier for the node
	private String nodeID;
	// The nodes's column
	private int nodeCol=0;
	// The nodes' row
	private int nodeRow=0;
	// Track whether the node is visible
	private boolean isVisible=false;
	// Track whether this is the queen node square
	private boolean isQueenNode=false;
	// A count of Queens in this node square
	private int queenCount=0;
	// A count of Foragers in this node square
	private int foragerCount=0;
	// A count of Scouts in this node square
	private int scoutCount=0;
	// A count of Soldiers in this node square
	private int soldierCount=0;
	// A count of Bala ants in this node square
	private int balaCount=0;
	// A count of food units in the node square
	private int foodCount=0;
	// A count of pheromone levels in the node square
	private int pheromoneCount=0;
	// The LinkedList data structure list of Ant object occupants in the node square
	private LinkedList nodeAntList=new LinkedList();
	
	/**
	 *  ColonyNode constructor
	 *  
	 * @param col		The node square column
	 * @param row		The node square row
	 * @param pColonyNodeView	A reference to the corresponding GUI node square
	 */
	public ColonyNode(int col, int row, ColonyNodeView pColonyNodeView)
	{
	    String nodeID=new String();
	    nodeID=(col) + "," + (row);
	    
	    setGUINode(pColonyNodeView);
	    setGUINodeID(nodeID);		
		setNodeCol(col);
		setNodeRow(row);
		setQueenNode(false);
		setQueenCount(0);
		setForagerCount(0);
		setScoutCount(0);
		setSoldierCount(0);
		setBalaCount(0);
		setFoodCount(0);
		setPheromoneCount(0);
	}
	
	// Set the GUI node object reference
	public void setGUINode(ColonyNodeView pColonyNodeView)
	{
		guiNode=pColonyNodeView;
	}
	   
	// Set the GUI node Node ID
    public void setGUINodeID(String pnodeID)
    {
    	nodeID=pnodeID;
    	guiNode.setID(pnodeID);
    }
    	
    // Set the node square column
	public void setNodeCol(int col)
	{
		nodeCol=col;
	}
	
	// Set the node square row
	public void setNodeRow(int row)
	{
		nodeRow=row;
	}
	
	// Set the node visible/hidden flag and update the GUI also
	public void setIsVisible(boolean pvisible)
	{
		isVisible=pvisible;
		
		if (isVisible==true)
		{
			guiNode.showNode();		
		}
		else if (isVisible==false)
		{
			guiNode.hideNode();
		}
	}	

	// Set the Queen node flag and update the GUI node also
	public void setQueenNode(boolean pqueen)
	{
		isQueenNode=pqueen;
		guiNode.setQueen(isQueenNode);
	}

	// Set the Queen count and update the GUI
	public void setQueenCount(int count)
	{
		queenCount=count;

		if (queenCount>0)
			guiNode.showQueenIcon();
		else
			guiNode.hideQueenIcon();	
	}
	
	// Set the Forager count and update the GUI
	public void setForagerCount(int count)
	{
		foragerCount=count;
		guiNode.setForagerCount(foragerCount);

		if (foragerCount>0)
			guiNode.showForagerIcon();
		else
			guiNode.hideForagerIcon();	
		
	}
	
	// Set the Scout count and update the GUI
	public void setScoutCount(int count)
	{
		scoutCount=count;
		guiNode.setScoutCount(scoutCount);	

		if (scoutCount>0)
			guiNode.showScoutIcon();
		else
			guiNode.hideScoutIcon();	
		
	}
	
	// Set the Soldier count and update the GUI
	public void setSoldierCount(int count)
	{
		soldierCount=count;
		guiNode.setSoldierCount(soldierCount);	 

		if (soldierCount>0)
			guiNode.showSoldierIcon();
		else
			guiNode.hideSoldierIcon();	
	
	}
	
	// Set the Bala count and update the GUI
	public void setBalaCount(int count)
	{
		balaCount=count;
		guiNode.setBalaCount(balaCount);	 

		if (balaCount>0)
			guiNode.showBalaIcon();
		else
			guiNode.hideBalaIcon();	
		
	}
	
	// Set the Food count and update the GUI
	public void setFoodCount(int count)
	{
		foodCount=count;
		guiNode.setFoodAmount(foodCount);	
	}
	
	// Set the Pheromone Level and update the GUI
	public void setPheromoneCount(int count)
	{
		pheromoneCount=count;
		guiNode.setPheromoneLevel(pheromoneCount);	
	}
		
	/* Increment and Decrement Methods - These update the AntColony data structures *and* the GUI ColonyNodeView display. */

	public void incrQueenCount(int count)
	{
		queenCount=queenCount+count;

		if (queenCount>0)
			guiNode.showQueenIcon();
		else
			guiNode.hideQueenIcon();	
	}
	
	public void incrForagerCount(int count)
	{
		foragerCount=foragerCount+count;
		guiNode.setForagerCount(foragerCount);
		
		if (foragerCount>0)
			guiNode.showForagerIcon();
		else
			guiNode.hideForagerIcon();			
	}
	
	public void incrScoutCount(int count)
	{
		scoutCount=scoutCount+count;
		guiNode.setScoutCount(scoutCount);	

		if (scoutCount>0)
			guiNode.showScoutIcon();
		else
			guiNode.hideScoutIcon();	
	}
	
	public void incrSoldierCount(int count)
	{
		soldierCount=soldierCount+count;
		guiNode.setSoldierCount(soldierCount);	
		
		if (soldierCount>0)
			guiNode.showSoldierIcon();
		else
			guiNode.hideSoldierIcon();			
	}
	
	public void incrBalaCount(int count)
	{
		balaCount=balaCount+count;
		guiNode.setBalaCount(balaCount);	

		if (balaCount>0)
			guiNode.showBalaIcon();
		else
			guiNode.hideBalaIcon();	
	}
	
	public void incrFoodCount(int count)
	{
		foodCount=foodCount+count;
		guiNode.setFoodAmount(foodCount);	
	}
	
	public void incrPheromoneCount(int count)
	{
		pheromoneCount=pheromoneCount+count;
		guiNode.setPheromoneLevel(pheromoneCount);	
	}

	public void decrQueenCount(int count)
	{
		queenCount=queenCount-count;

		if (queenCount>0)
			guiNode.showQueenIcon();
		else
			guiNode.hideQueenIcon();	
	}
		
	public void decrForagerCount(int count)
	{
		foragerCount=foragerCount-count;
		guiNode.setForagerCount(foragerCount);

		if (foragerCount>0)
			guiNode.showForagerIcon();
		else
			guiNode.hideForagerIcon();	
	}
	
	public void decrScoutCount(int count)
	{
		scoutCount=scoutCount-count;
		guiNode.setScoutCount(scoutCount);	
		
		if (scoutCount>0)
			guiNode.showScoutIcon();
		else
			guiNode.hideScoutIcon();			
	}
	
	public void decrSoldierCount(int count)
	{
		soldierCount=soldierCount-count;
		guiNode.setSoldierCount(soldierCount);	 

		if (soldierCount>0)
			guiNode.showSoldierIcon();
		else
			guiNode.hideSoldierIcon();	
	}
	
	public void decrBalaCount(int count)
	{
		balaCount=balaCount-count;
		guiNode.setBalaCount(balaCount);	 

		if (balaCount>0)
			guiNode.showBalaIcon();
		else
			guiNode.hideBalaIcon();	
	}
	
	public void decrFoodCount(int count)
	{
		foodCount=foodCount-count;
		guiNode.setFoodAmount(foodCount);	
	}
	
	public void decrPheromoneCount(int count)
	{
		pheromoneCount=pheromoneCount-count;
		guiNode.setPheromoneLevel(pheromoneCount);	
	}
			

	/* Class Getter methods */
	
	public String getNodeID()
	{
		return nodeID;
	}
	
	public int getNodeCol()
	{
		return nodeCol;
	}
	
	public int getNodeRow()
	{
		return nodeRow;
	}

	public boolean isQueenNode()
	{
		return isQueenNode;
	}
	
	public int getQueenCount()
	{
		return queenCount;
	}
		
	public int getForagerCount()
	{
		return foragerCount;
	}
	
	public int getScoutCount()
	{
		return scoutCount;
	}
	
	public int getSoldierCount()
	{
		return soldierCount;
	}
	
	public int getBalaCount()
	{
		return balaCount;
	}
	
	public int getFoodCount()
	{
		return foodCount;
	}
	
	public int getPheromoneCount()
	{
		return pheromoneCount;
	}
	
	public boolean getIsVisible()
	{
		return isVisible;
	}
	
	// Show this node square
	public void showNode()
	{
		isVisible=true;
		guiNode.showNode();	
	}
	
	// Hide this node square
	public void hideNode()
	{
		isVisible=false;
		guiNode.hideNode();
	}
	
	// Add an Ant object to this node square
	public void addNodeAntList(Object antObject)
	{
		nodeAntList.add(antObject);
	}
	
	// Remove an Ant object from this node square
	public void removeNodeAntList(Object antObject)
	{
		nodeAntList.remove(antObject);		
	}	

	// Return a reference to the full node square Ant LinkedList
	public LinkedList getNodeAntList()
	{
		return nodeAntList;
	}	
	
}

