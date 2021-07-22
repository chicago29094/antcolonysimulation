import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * Soldier class
 * 
 * The Soldier class inherits basic ant attributes and methods from the abstract Ant superclass
 * and defines the turnBehavior method that defines the movement and behavior of a Soldier ant.
 * 
 * @author Harry Anastopoulos
 * 
 */

public class Soldier extends Ant implements AntColonyConstants
{
   private final int SCOUT_MODE=0;
   private final int ATTACK_MODE=1;
   private int behaviorMode=SCOUT_MODE;   
   private LinkedList nodeAntListObject;
   
  /**
   * The Soldier ant constructor 
   * 
   * @param pAntColonyObj			Object reference to containing AntColony object
   * @param pColonyNodeGrid			Object reference to the ColonyNodeGrid square array data structure 
   * @param pcol					birth square column of ant instance
   * @param prow					birth square row of any instance
   * @param pAntID					sequential numeric ID for this ant instance
   * @param pUniversal_Ant_Time		the current universal time expressed in ant turns
   */
   public Soldier(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
   {
	   super(pAntColonyObj, pColonyNodeGrid, pcol, prow, pAntID, pUniversal_Ant_Time);
	   antType=SOLDIER_ANT_TYPE;
	   antTypeName="Soldier";
	   antDeathTurn=pUniversal_Ant_Time+SOLDIER_LIFESPAN;	   
   }

   /**
    * turnBehavior
    * 
    * The turnBehavior is the method controlling the Soldier ant behavior.
    * 
    */   
   public void turnBehavior(int turn)
   {
	   int borderNode=0;
	   int numViableBorderNodes=0;
	   int randomViableBorderNode=0;
	   boolean viableNode=false;
	   boolean moveToBalaNode=false;
	   int newcol=col;
	   int newrow=row;
	   int numBalaAnts=0;
	   int numBalaNodes=0;
	   int randomBalaNode=0;
	   Ant colonyAnt;
	   boolean abortIteration=false;
	   int randomInt=0;

	   // First check to see if the ant is already dead
	   if (isAntDead)
	   {
		   return;
	   }
	   
	   behaviorMode=SCOUT_MODE;
	   
	   // Next, check to see if the ant must die of old age
	   if (turn==antDeathTurn)
	   {
		   setIsAntDead(true);
		   setAntDiedTurn(turn);
		   antColonyObj.removeAntDelayed(this, col, row);
		   return;
	   }
	   
	   // Next, we see if we are in a node with Bala ants that we should attack
	   
	   if ( (colonyNodeGrid[col][row].getBalaCount()>0) )
	   {
		   behaviorMode=ATTACK_MODE;
		   
		   // Retrieve the LinkedList of Ant objects currently in this node square
		   nodeAntListObject=colonyNodeGrid[col][row].getNodeAntList();
		   
		   // If there are no ants here, we can't proceed with an attack
		   if (nodeAntListObject.size()>0)
		   {
			   try
			   {
				   // Iterate through the entire list of ants in this square to check for Bala ants
				   for (Iterator itr=nodeAntListObject.iterator(); itr.hasNext();)
				   {
					   colonyAnt=(Ant) itr.getCurrent();
					   	
					   if (!colonyAnt.getIsAntDead())
					   {
						   // Count the total number of Bala ants
						   if (colonyAnt.getAntType()==BALA_ANT_TYPE) numBalaAnts++;
					   } 		
					   itr.next();
				   }
				   
				   // There must be one or more Bala ants for the Soldier ant to perform an attack
				   if (numBalaAnts>0)
				   {
					   // A random Bala ant is selected as the target for our attack
					   randomInt=antColonyObj.getRandomRange(1, numBalaAnts);

					   // Iterate through the list of ants in this node again, until we find a Bala ant at the targetted count number
					   abortIteration=false;
					   for (Iterator itr=nodeAntListObject.iterator(); (!abortIteration) && itr.hasNext(); )
					   {
						   // Retrieve the Ant object detals
						   colonyAnt=(Ant) itr.getCurrent();
						   	
						   // Make sure the ant isn't already dead
						   if (!colonyAnt.getIsAntDead())
						   {
							   // Count this ant if it is a Bala ant
							   if (colonyAnt.getAntType()==BALA_ANT_TYPE) numBalaAnts++;
						   } 		
						   
						   // If this Bala ant is the targeted Bala ant, attack it
						   if (numBalaAnts==randomInt)
						   {
							   // Generate a random double between 0 and 1
							   double randomDouble=antColonyObj.getRandomDouble();
							   
							   // if the random double is within our kill ratio range, the Bala ant has been killed
							   if ( (randomDouble>=0) && (randomDouble<SOLDIER_ENEMY_KILL_RATIO) )
							   {
								   // Kill the Bala ant and schedule it for removal
								   colonyAnt.setIsAntDead(true);
								   colonyAnt.setAntDiedTurn(turn);
								   antColonyObj.removeAntDelayed(colonyAnt, col, row);
								   abortIteration=true;
								   
								   // Update the Soldier ant kill stats
								   antColonyObj.updateSoldierKillStats(true);
							   }
							   else
							   {
								   // Update the Soldier ant kill stats
								   antColonyObj.updateSoldierKillStats(false);
							   }
						   }
						   
						   if (!abortIteration)
						   {
							   itr.next();
						   }
					   }
					   
					   
				   }
				   
			   }
			   catch (IndexOutOfBoundsException e)
			   {	
				   System.out.println("Soldier Uncaught Exception");
			   }
		   }
		   
		   behaviorMode=SCOUT_MODE;
		   return;
	   
	   }
	   
	   // If we aren't in the same node as Bala ants, we will move to a different neighboring visible node square 

	   behaviorMode=SCOUT_MODE;
	   
	   // Check the neighboring square we can move to.  If there are Bala ants there, move there, otherwise move randomly
	   numBalaNodes=0;
	   numViableBorderNodes=0;
	   for (borderNode=1; borderNode<=8; borderNode++)
	   {
		   // Don't move off of a colony edge
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
			   
			   // Count the number of viable border nodes and the number of border nodes with Bala ants
			   if ( (colonyNodeGrid[newcol][newrow].getIsVisible()) && (colonyNodeGrid[newcol][newrow].getBalaCount()>0) )
			   {
				   numViableBorderNodes++;
				   numBalaNodes++;
			   }			   
			   else if (colonyNodeGrid[newcol][newrow].getIsVisible())
			   {
				   numViableBorderNodes++;
			   }
			   
		   }	
		   
	   }
	
	   // if any of the neighboring nodes have Bala ants, we need to move to one of them
	   if (numBalaNodes>0)
	   {
		   // We will move to a random neighboring node with 1 or more Bala ants
		   randomBalaNode=antColonyObj.getRandomRange(1, numBalaNodes);
		   moveToBalaNode=true;
	   }

	   // We will also select a general viable target neighboring node to potentially move to
	   if (numViableBorderNodes>0)
	   {
		   randomViableBorderNode=antColonyObj.getRandomRange(1, numViableBorderNodes);
	   }

	   // Iterate through the eight border nodes again
	   numBalaNodes=0;
	   numViableBorderNodes=0;
	   for (borderNode=1; borderNode<=8; borderNode++)
	   {
		   // Avoid going off of a colony edge
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

			   // If we are moving to a neighboring Bala occupied node, check to make sure it is visible and the Bala count is greater than zero
			   if ( (moveToBalaNode) && (colonyNodeGrid[newcol][newrow].getIsVisible()) && (colonyNodeGrid[newcol][newrow].getBalaCount()>0) )
			   {
				   numBalaNodes++;
					   
				   // If this specific node matches our target random Bala node, move to that new node
				   if (numBalaNodes==randomBalaNode)
				   {
					   antColonyObj.moveAnt(this, col, row, newcol, newrow);
						   
					   // Update the Ant object's current column and row variables utilized to keep track of the ant's current location
					   col=newcol;
					   row=newrow;
						   
					   return;						   
				   }			   				   
			   }
			   // If we are not moving to Bala occupied node, we will move to our randomly selected viable border node
			   else if ( (!moveToBalaNode) && (colonyNodeGrid[newcol][newrow].getIsVisible()) )
			   {
				   numViableBorderNodes++;

				   // If this specific node matches our random choice, move to that new node
				   if (numViableBorderNodes==randomViableBorderNode)
				   {
					   antColonyObj.moveAnt(this, col, row, newcol, newrow);
						   
					   // Update the Ant object's current column and row variables utilized to keep track of the ant's current location
					   col=newcol;
					   row=newrow;
						   
					   return;						   
				   }			   				   				   
			   }
			   			   
		   } // End if (viableNode==true)	   		   

	   } // End borderNode (1..8) loop
	   
   } // End Soldier turnBehavior
   
}

