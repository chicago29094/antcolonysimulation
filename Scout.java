import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * Scout
 * 
 * The Scout class inherits basic ant attributes and methods from the abstract Ant superclass
 * and defines the turnBehavior method that defines the movement and behavior of a Scout ant.
 * 
 * @author Harry Anastopoulos
 *
 */
public class Scout extends Ant implements AntColonyConstants
{
   /**
    * The Scout ant constructor 
    * 
    * @param pAntColonyObj			Object reference to containing AntColony object
    * @param pColonyNodeGrid		Object reference to the ColonyNodeGrid square array data structure 
    * @param pcol					birth square column of ant instance
    * @param prow					birth square row of any instance
    * @param pAntID					sequential numeric ID for this ant instance
    * @param pUniversal_Ant_Time	the current universal time expressed in ant turns
    */ 
	public Scout(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
	{
		super(pAntColonyObj, pColonyNodeGrid, pcol, prow, pAntID, pUniversal_Ant_Time);
		antType=SCOUT_ANT_TYPE;
		antTypeName="Scout";
		antDeathTurn=pUniversal_Ant_Time+SCOUT_LIFESPAN;
	}

	   /**
	    * turnBehavior
	    * 
	    * The turnBehavior is the method controlling the Scout ant behavior.
	    * 
	    */   	
	   public void turnBehavior(int turn)
	   {
		   boolean viableNode=false;
		   int newcol=col;
		   int newrow=row;
		   int randomInt=0;

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
			   antColonyObj.removeAntDelayed(this, col, row);
			   return;
		   }

		   // Scouts can wander to any neighboring node square as long as it is now off of a colony edge.
		   while (viableNode==false)
		   {
			   randomInt=antColonyObj.getRandomRange(1, 8);
			
			   // Check to make sure we are not off of a N, W, E, or S edge
			   
			   if ( (col==0) && ( (randomInt==1) || (randomInt==4) || (randomInt==6) ) )
			   {
				   viableNode=false;
			   }
			   else if ( (col==COLONY_GRID_SIZE-1) && ( (randomInt==3) || (randomInt==5) || (randomInt==8) ) )
			   {
				   viableNode=false;
			   }
			   else if ( (row==0) && ( (randomInt==1) || (randomInt==2) || (randomInt==3) ) )
			   {
				   viableNode=false;
			   }
			   else if ( (row==COLONY_GRID_SIZE-1) && ( (randomInt==6) || (randomInt==7) || (randomInt==8) ) )
			   {
				   viableNode=false;
			   }
			   else
			   {
				   viableNode=true;
			   }
		   }
			   
		   if (randomInt==1) 
		   {
			   newcol=col-1;
			   newrow=row-1;
		   }
		   else if (randomInt==2)
		   {
			   newcol=col;
			   newrow=row-1;
		   }					   
		   else if (randomInt==3)
		   {
			   newcol=col+1;
			   newrow=row-1;
		   }					   
		   else if (randomInt==4)
		   {
			   newcol=col-1;
			   newrow=row;
		   }					   			   
		   else if (randomInt==5)
		   {
			   newcol=col+1;
			   newrow=row;
		   }					   			   
		   else if (randomInt==6)
		   {
			   newcol=col-1;
			   newrow=row+1;
		   }					   			   
		   else if (randomInt==7)
		   {
			   newcol=col;
			   newrow=row+1;
		   }					   
		   else if (randomInt==8)
		   {
			   newcol=col+1;
			   newrow=row+1;
		   }
		   else
		   {
			   newcol=col;
			   newrow=row;
		   }
			   
		   // Update the Scout ant movement stats
		   antColonyObj.updateScoutMoveStats(randomInt);
		   
		   // Move the Scout any to a new square
		   antColonyObj.moveAnt(this, col, row, newcol, newrow);

		   // Make the new destination square visible
		   colonyNodeGrid[newcol][newrow].showNode();
			   
		   // Update the Ant object's current column and row variables utilized to keep track of the ant's current location
		   col=newcol;
		   row=newrow;
	   }

}

