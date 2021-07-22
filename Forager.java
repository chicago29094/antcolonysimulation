import dataStructures.LinkedList;
import dataStructures.Iterator;
import dataStructures.BinaryMaxHeap;
import dataStructures.ArrayStack;
import dataStructures.LinkedQueue;

/**
 * Forager
 * 
 * The Forager class inherits basic ant attributes and methods from the abstract Ant superclass
 * and defines the turnBehavior method that defines the movement and behavior of a Soldier ant.
 * 
 * @author Harry Anastopoulos
 *
 */

public class Forager extends Ant implements AntColonyConstants
{
   // Behavior modes for the Forager ant
   private final int FORAGE_MODE=0;
   private final int NEST_MODE=1;
   
   // How many food units is this ant carrying
   private int carryingFoodUnits=0;
   
   // The ant's previous column and row position
   private int lastcol=0;
   private int lastrow=0;
   
   // Initial behavior mode
   private int behaviorMode=FORAGE_MODE;   
   
   // The Forager ant utilizes a BinarMaxHeap to track and process neighboring node pheromone levels
   private BinaryMaxHeap borderNodeMaxHeap=new BinaryMaxHeap();
   // The Forager ant utilizes an ArrayStack data structure to track where is has traveled looking for food, so it can find its way back to the Queen and the colony entrance
   private ArrayStack nodeMovementHistory=new ArrayStack();
   // The Forager utilizes a LinkedQueue to break the ant out of loops
   private LinkedQueue forageAntiLoopQueue=new LinkedQueue();
      
   /**
    * The Forager ant constructor 
    * 
    * @param pAntColonyObj			Object reference to containing AntColony object
    * @param pColonyNodeGrid		Object reference to the ColonyNodeGrid square array data structure 
    * @param pcol					birth square column of ant instance
    * @param prow					birth square row of any instance
    * @param pAntID					sequential numeric ID for this ant instance
    * @param pUniversal_Ant_Time	the current universal time expressed in ant turns
    */ 
   public Forager(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
   {
	   super(pAntColonyObj, pColonyNodeGrid, pcol, prow, pAntID, pUniversal_Ant_Time);
	   antType=FORAGER_ANT_TYPE;
	   behaviorMode=FORAGE_MODE;  
	   antTypeName="Forager";
	   antDeathTurn=pUniversal_Ant_Time+FORAGER_LIFESPAN;
	   carryingFoodUnits=0;
	   lastcol=pcol;
	   lastrow=prow;
   }

   /**
    * turnBehavior
    * 
    * The turnBehavior is the method controlling the Forager ant behavior.
    * 
    */   
   public void turnBehavior(int turn)
   {

	   // First check to see if the ant is already dead
	   if (isAntDead)
	   {
		   return;
	   }
	   
	   // Next, check to see if the ant must die of old age
	   if (turn==antDeathTurn)
	   {
		   setIsAntDead(true);
		   setAntDiedTurn(turn);
		   forageAntiLoopQueue.clear();
		   
		   // Per Ant Colony specifications, if we are carrying food, we must drop it into the square where the ant died
		   if (carryingFoodUnits>0)
		   {
			   int newFoodCount=colonyNodeGrid[col][row].getFoodCount()+carryingFoodUnits;
			   colonyNodeGrid[col][row].setFoodCount(newFoodCount);
		   }
		   antColonyObj.removeAntDelayed(this, col, row);
		   return;
	   }
	   
	   // If we in forage mode, we run the forage behavior method
	   if (behaviorMode==FORAGE_MODE)
	   {
		   turnBehaviorForage(turn);
	   }
	   // If we are in return-to-nest mode, we run the return-to-nest behavior method
	   else if (behaviorMode==NEST_MODE)
	   {
		   turnBehaviorNest(turn);
	   }
	   
	   return;
	   
   }
   
   // This Forager Ant is in Forage Mode
   
   private void turnBehaviorForage(int turn)
   {
	   int borderNode=0;
	   boolean viableNode=false;
	   int numViableBorderNodes=0;
	   int newcol=col;
	   int newrow=row;
	   int randomInt=0;
	   Ant colonyAnt;	 
	   boolean matchFlag=false;
	   PheromoneNode borderPheromoneNode;
	   	   
	   if (behaviorMode!=FORAGE_MODE) return;
	   
	   // We check our bordering nodes, seeking a node this is visible and has been revealed by a scout and is not off of a colony edge
	   numViableBorderNodes=0;
	   for (borderNode=1; borderNode<=8; borderNode++)
	   {
		   viableNode=false;
		   if ( (col==0) && ( (borderNode==1) || (borderNode==4) || (borderNode==6) ) )
		   {
			   viableNode=false;
		   }
		   else if ( (col==COLONY_GRID_SIZE-1) && ( (borderNode==3) || (borderNode==5) || (borderNode==8) ) )
		   {
			   viableNode=false;
		   }
		   else if ( (row==0) && ( (borderNode==1) || (borderNode==2) || (borderNode==3) ) )
		   {
			   viableNode=false;
		   }
		   else if ( (row==COLONY_GRID_SIZE-1) && ( (borderNode==6) || (borderNode==7) || (borderNode==8) ) )
		   {
			   viableNode=false;
		   }
		   else
		   {
			   viableNode=true;
		   }
		   
		   if (viableNode==true)
		   {
			   if (borderNode==1) 
			   {
				   newcol=col-1;
				   newrow=row-1;
			   }
			   else if (borderNode==2)
			   {
				   newcol=col;
				   newrow=row-1;
			   }					   
			   else if (borderNode==3)
			   {
				   newcol=col+1;
				   newrow=row-1;
			   }					   
			   else if (borderNode==4)
			   {
				   newcol=col-1;
				   newrow=row;
			   }					   			   
			   else if (borderNode==5)
			   {
				   newcol=col+1;
				   newrow=row;
			   }					   			   
			   else if (borderNode==6)
			   {
				   newcol=col-1;
				   newrow=row+1;
			   }					   			   
			   else if (borderNode==7)
			   {
				   newcol=col;
				   newrow=row+1;
			   }					   
			   else if (borderNode==8)
			   {
				   newcol=col+1;
				   newrow=row+1;
			   }
			   else
			   {
				   newcol=col;
				   newrow=row;
			   }	
			   
			   // Consider this new node only if it is visible
			   if (colonyNodeGrid[newcol][newrow].getIsVisible())
			   {
				   // The normal behavior is to move to a node with the highest pheromone level, as long as it isn't the node we occupied in the previous turn.
				   // However, looping around the colony entrance and at other locations can occur, so we check the last 100 visited nodes and see how many times
				   // we have visited our targeted node destination during our last 100 turns, and the more times we have visited the square recently, the more 
				   // we mask the square's pheromone level effect utilizing a "blocking" factor.
				   
				   int numRecentVisits=0;
				   double pheromoneBlockingFactor=1.00;
				   
				   numViableBorderNodes++;

				   numRecentVisits=antiLoopQueuePeek(newcol, newrow);
				   
				   if (numRecentVisits==0) pheromoneBlockingFactor=1.00;
				   else if (numRecentVisits==1) pheromoneBlockingFactor=0.50;
				   else if (numRecentVisits==2) pheromoneBlockingFactor=0.125;
				   else if (numRecentVisits>=3) pheromoneBlockingFactor=0.00;
				   else if (numRecentVisits>=4) pheromoneBlockingFactor=0.00;				   
				   else pheromoneBlockingFactor=1.00;
				   
				   randomInt=antColonyObj.getRandomRange(1, 1000);
				   				   
				   borderPheromoneNode=new PheromoneNode(newcol, newrow, (int) (colonyNodeGrid[newcol][newrow].getPheromoneCount()*pheromoneBlockingFactor), randomInt);
				   
				   // We place each node candidate in a BinaryMaxHeap, but we rank the nodes based on their pheromone level *and* a random "salt" value that makes
				   // nodes with equal pheromone levels be assessed in a pseudo-random fashion.
				   
				   borderNodeMaxHeap.add(borderPheromoneNode);				   
			   }
			   
		   } // if (viableNode==true)
		   
	   } // for borderNode loop
	   
	   // If there were no candidate nodes we can move into, we can't proceed
	   if (numViableBorderNodes==0)
	   {
		   return;
	   }
	   
	   // If for some reason our BinaryMaxHeap is empty, we can't proceed
	   if (borderNodeMaxHeap.isEmpty())
	   {
		   return;
	   }
	   
	   // We proceed to return Max values from our BinaryMaxHeap until we find the maximum pheromone level node, that we haven't just visited,
	   // or the last item in the heap if no other choices are viable
	   matchFlag=false;
	   do
	   {
		   borderPheromoneNode=(PheromoneNode) borderNodeMaxHeap.get();
		   borderNodeMaxHeap.remove();
		   
		   if (borderNodeMaxHeap.isEmpty())
		   {
			   matchFlag=true;
		   }
		   else if ( (borderPheromoneNode.getCol()!=lastcol) || (borderPheromoneNode.getRow()!=lastrow) )
		   {
			   matchFlag=true;
		   }
	   }
	   while(!matchFlag);
	   borderNodeMaxHeap.clear();
	   
	   newcol=borderPheromoneNode.getCol();
	   newrow=borderPheromoneNode.getRow();
	   
	   // Push our movement history into the movement stack
	   nodeMovementHistory.push(colonyNodeGrid[col][row]);
	   // Add our current position into the anti-looping position history queue
	   antiLoopQueueAdd(newcol, newrow);
	   
	   // Move the Forager to the new node square
	   antColonyObj.moveAnt(this, col, row, newcol, newrow);
	   
	   lastcol=col;
	   lastrow=row;
	   
	   col=newcol;
	   row=newrow;
		   
	   // If we have moved into a square containing food, which isn't the Queen node, and we aren't carrying food, we pickup one 
	   // unit of food, switch to return-to-nest mode, and wait for our next turn
	   if ( (colonyNodeGrid[col][row].getFoodCount()>0) && (!colonyNodeGrid[col][row].isQueenNode()) && (carryingFoodUnits==0) )
	   {
		   behaviorMode=NEST_MODE;
		   forageAntiLoopQueue.clear();
		   carryingFoodUnits=1;
		   colonyNodeGrid[col][row].decrFoodCount(1);
	   }
	   
	   return;			   
	   
   }
   
   // This Forager Ant is in Return-to-Nest Mode
   
   private void turnBehaviorNest(int turn)
   {
	   int newcol=col;
	   int newrow=row;
	   ColonyNode previousColonyNode=null;
	   
	   if (behaviorMode!=NEST_MODE) return;
	   
	   // Since we are in return-to-nest mode, we deposit ten units of pheromone in the current square 
	   // before retracing are next step back to the queen square

	   // We are purposefully capping our max pheromone to 1000 and not 1010
	   if (colonyNodeGrid[col][row].getPheromoneCount()<=990)
	   {
		   colonyNodeGrid[col][row].incrPheromoneCount(10);
	   }
	   
	   // The Forager ant utilizes the nodeMovementHistory stack to return its previous node square position
	   if (!nodeMovementHistory.isEmpty())
	   {
		   previousColonyNode=(ColonyNode) nodeMovementHistory.pop();
	   }	  

	   // The previous column and row of the Forager ant's previous position is retrieved
	   newcol=previousColonyNode.getNodeCol();
	   newrow=previousColonyNode.getNodeRow();
	   
	   // The Forager ant moves from its current node square to its previous node square 
	   antColonyObj.moveAnt(this, col, row, newcol, newrow);
	   
	   lastcol=col;
	   lastrow=row;
	   
	   // Update the Ant object's current column and row variables utilized to keep track of the ant's current location
	   col=newcol;
	   row=newrow;	   

	   // If the Forager has entered the Queen node square, its movement history stack is cleared, 
	   // the ant re-enters Forage mode, and the ant drops one unit of food into the Queen node square
	   if (colonyNodeGrid[col][row].isQueenNode())
	   {
		   nodeMovementHistory.clear();
		   behaviorMode=FORAGE_MODE;
		   carryingFoodUnits=0;
		   colonyNodeGrid[col][row].incrFoodCount(1);
	   }
	   
	   return;
   }   
   
   /**
    * antiLoopQueuePeek
    * 
    * The antiLoopQueuePeek method is utilized to check a target destination node at position col, row
    * to see how many times it has been visited during the Forager ant's last 100 turns, while in Forage mode.
    * All items are dequeued from the queue, compared to the target col and row and then queued again in the same
    * order.  A count is tabulated regarding the number of matches and is returned back to the calling method.
    * 
    * @param col		Destination node square column to check
    * @param row		Destination node square row to check
    * @return			Returns a count of the number of times the node square has been visited during the last 100 turns
    */
   private int antiLoopQueuePeek(int col, int row)
   {
	   int i=0, j=0, k=0;
	   int matchCount=0;
	   PheromoneNode borderPheromoneNode;

	   if (forageAntiLoopQueue.size()>0)
	   {
		   j=forageAntiLoopQueue.size();
		   for (i=0; i<j; i++)
		   {
			   	borderPheromoneNode=(PheromoneNode) forageAntiLoopQueue.dequeue();
			   	if ( (borderPheromoneNode.getCol()==col) && (borderPheromoneNode.getRow()==row) )
		   		{
			   		matchCount++;
		   		}
			   	forageAntiLoopQueue.enqueue(borderPheromoneNode);
		   }
	   }
	   
	   return matchCount;
   }

   // Add a specific border node to the anti-loop LinkedQueue
   private void antiLoopQueueAdd(int col, int row)
   {
	   PheromoneNode borderPheromoneNode;
	   borderPheromoneNode=new PheromoneNode(col, row, colonyNodeGrid[col][row].getPheromoneCount(), 1);
	   forageAntiLoopQueue.enqueue(borderPheromoneNode);

	   if (forageAntiLoopQueue.size()>100)
	   {
		   forageAntiLoopQueue.dequeue();
	   }
   }   
   
      
}

