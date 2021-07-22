import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * Bala
 * 
 * The Bala class inherits basic ant attributes and methods from the abstract Ant superclass
 * and defines the turnBehavior method that defines the movement and behavior of a Bala ant.
 * 
 * @author Harry Anastopoulos
 *
 */
public class Bala extends Ant implements AntColonyConstants
{
   private final int SCOUT_MODE=0;
   private final int ATTACK_MODE=1;
   private int behaviorMode=SCOUT_MODE;   
   private LinkedList nodeAntListObject;
   
   /**
    * The Bala ant constructor 
    * 
    * @param pAntColonyObj			Object reference to containing AntColony object
    * @param pColonyNodeGrid		Object reference to the ColonyNodeGrid square array data structure 
    * @param pcol					birth square column of ant instance
    * @param prow					birth square row of any instance
    * @param pAntID					sequential numeric ID for this ant instance
    * @param pUniversal_Ant_Time	the current universal time expressed in ant turns
    */
   public Bala(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
   {
	   super(pAntColonyObj, pColonyNodeGrid, pcol, prow, pAntID, pUniversal_Ant_Time);
	   antType=BALA_ANT_TYPE;
	   antTypeName="Bala";	   
	   antDeathTurn=pUniversal_Ant_Time+BALA_LIFESPAN;
   }

   /**
    * turnBehavior
    * 
    * The turnBehavior is the method controlling the Bala ant behavior.
    * 
    */
   public void turnBehavior(int turn)
   {
	   boolean viableNode=false;
	   int newcol=col;
	   int newrow=row;
	   int numColonyAnts=0;
	   Ant colonyAnt;
	   boolean abortIteration=false;
	   int randomInt=0;
	   
	   // First check to see if the ant is already dead
	   // Do not process a turn if the ant is dead
	   if (isAntDead)
	   {
		   return;
	   }	   
	   
	   // Default behavior is Scout mode
	   behaviorMode=SCOUT_MODE;	   

	   // Next, check to see if the ant must die of old age
	   if (turn==antDeathTurn)
	   {
		   setIsAntDead(true);
		   setAntDiedTurn(turn);
		   antColonyObj.removeAntDelayed(this, col, row);
		   return;
	   }
	   
	   // Next, we see if we are in a node with colony ants that we should attack
	   
	   if ( (colonyNodeGrid[col][row].getQueenCount()>0) || (colonyNodeGrid[col][row].getForagerCount()>0) || 
			   (colonyNodeGrid[col][row].getScoutCount()>0) || (colonyNodeGrid[col][row].getSoldierCount()>0) )
	   {
		   // We attack if we are in a node with enemy ants
		   behaviorMode=ATTACK_MODE;
		   
		   // Each Colony grid square has its own LinkedList of ants in that node
		   // We retrieve that list so we can pick an attack target.
		   nodeAntListObject=colonyNodeGrid[col][row].getNodeAntList();
		   
		   // If there are no ants in our LinkedList, there are no ants to attack
		   if (nodeAntListObject.size()>0)
		   {
			   try
			   {
				   // We iterate through all of the ants in the current grid square and count the total number of colony ants
				   for (Iterator itr=nodeAntListObject.iterator(); itr.hasNext();)
				   {
					   colonyAnt=(Ant) itr.getCurrent();
					   	
					   if (!colonyAnt.getIsAntDead())
					   {
						   if (colonyAnt.getAntType()==QUEEN_ANT_TYPE) numColonyAnts++;
						   if (colonyAnt.getAntType()==FORAGER_ANT_TYPE) numColonyAnts++;
						   if (colonyAnt.getAntType()==SCOUT_ANT_TYPE) numColonyAnts++;
						   if (colonyAnt.getAntType()==SOLDIER_ANT_TYPE) numColonyAnts++;
					   } 		
					   itr.next();
				   }
				   
				   // If there are 1 or more colony ants, we must attack one of the ants
				   if (numColonyAnts>0)
				   {
					   // We pick a random attack target
					   randomInt=antColonyObj.getRandomRange(1, numColonyAnts);

					   // We iterate through the list of ants in this node's LinkesList to find our target ant
					   abortIteration=false;
					   for (Iterator itr=nodeAntListObject.iterator(); (!abortIteration) && itr.hasNext(); )
					   {
						   colonyAnt=(Ant) itr.getCurrent();
						   	
						   if (!colonyAnt.getIsAntDead())
						   {
							   if (colonyAnt.getAntType()==QUEEN_ANT_TYPE) numColonyAnts++;
							   if (colonyAnt.getAntType()==FORAGER_ANT_TYPE) numColonyAnts++;
							   if (colonyAnt.getAntType()==SCOUT_ANT_TYPE) numColonyAnts++;
							   if (colonyAnt.getAntType()==SOLDIER_ANT_TYPE) numColonyAnts++;
						   } 		
						   
						   // This is the random colony ant we have targeted for attack
						   if (numColonyAnts==randomInt)
						   {
							   double randomDouble=antColonyObj.getRandomDouble();
							   
							   // Use the BALA_ENEMY_KILL_RATIO (default 0.50) to see if we succeeded in killing the target ant
							   if ( (randomDouble>=0) && (randomDouble<BALA_ENEMY_KILL_RATIO) )
							   {
								   // We killed the target ant
								   colonyAnt.setIsAntDead(true);
								   colonyAnt.setAntDiedTurn(turn);
								   antColonyObj.removeAntDelayed(colonyAnt, col, row);
								   abortIteration=true;
								   
								   // Update our Bala kill stats
								   antColonyObj.updateBalaKillStats(true);
							   }
							   else
							   {
								   // Update our Bala kill stats
								   abortIteration=true;
								   antColonyObj.updateBalaKillStats(false);
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
				   System.out.println("Bala Uncaught Exception");
			   }
		   }
		   
		   behaviorMode=SCOUT_MODE;
		   return;
	   
	   }
	   
	   // If we aren't in the same node as colony ants, we will randomly move to a different node square

	   behaviorMode=SCOUT_MODE;
	   
	   while (viableNode==false)
	   {
		   randomInt=antColonyObj.getRandomRange(1, 8);
		   
		   // Check to make sure we aren't off of a colony edge
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
		
	   // Update our Bala ant movement stats
	   antColonyObj.updateBalaMoveStats(randomInt);
	   
	   // Move the Bala ant to an new square
	   antColonyObj.moveAnt(this, col, row, newcol, newrow);
		   
	   // Update the Ant object's current column and row variables utilized to keep track of the ant's current location
	   col=newcol;
	   row=newrow;
	   
	   return;
   }

  
}

