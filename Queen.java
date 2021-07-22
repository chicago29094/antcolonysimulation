import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * Queen
 * 
 * The Queen class inherits basic ant attributes and methods from the abstract Ant superclass
 * and defines the turnBehavior method that defines the behavior of the Queen ant in the Ant Colony.
 * 
 * @author Harry Anastopoulos
 *
 */

public class Queen extends Ant implements AntColonyConstants
{
	/**
    * The Queen ant constructor 
    * 
    * @param pAntColonyObj			Object reference to containing AntColony object
    * @param pColonyNodeGrid		Object reference to the ColonyNodeGrid square array data structure 
    * @param pcol					birth square column of ant instance
    * @param prow					birth square row of any instance
    * @param pAntID					sequential numeric ID for this ant instance
    * 
	* @param pUniversal_Ant_Time	the current universal time expressed in ant turns
    */ 
   public Queen(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
   {
      super(pAntColonyObj, pColonyNodeGrid, pcol, prow, pAntID, pUniversal_Ant_Time);
      antType=QUEEN_ANT_TYPE;
      antTypeName="Queen";
      antDeathTurn=pUniversal_Ant_Time+QUEEN_LIFESPAN;
   }

   /**
    * turnBehavior
    * 
    * The turnBehavior is the method controlling the Queen ant behavior.
    * 
    */      
   public void turnBehavior(int turn)
   {
	   Ant newAnt;
	   
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
		   antColonyObj.removeAnt(this, col, row);
		   antColonyObj.setRunMode(COLONY_QUEEN_DEAD);
		   return;
	   }
	   
	   // If there is no food, the Queen starves and dies and the colony collapses
	   if (colonyNodeGrid[col][row].getFoodCount()==0)
	   {
		   setIsAntDead(true);
		   setAntDiedTurn(turn);
		   antColonyObj.removeAnt(this, col, row);
		   antColonyObj.setRunMode(COLONY_QUEEN_DEAD);
		   return;
	   }
	   else
	   {
		   // Queen consumes one unit of food per turn
		   colonyNodeGrid[col][row].decrFoodCount(1);
	   }
	   
	   // First turn of the day, Queen must hatch a new baby ant
	   if (turn%QUEEN_BIRTHRATE==1)
	   {
		   double randomDouble=antColonyObj.getRandomDouble();

		   // We select a random double value from 0 to less than 1
		   // The following frequency if/else statements work if the three ant-type frequencies 
		   // add up to 100 percent (1.00)
		   
		   if ( (randomDouble>=0) && (randomDouble<QUEEN_BIRTHFREQ_FORAGER) )
		   {
			   newAnt=antColonyObj.addAnt(FORAGER_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
		   }
		   else if ( (randomDouble>=QUEEN_BIRTHFREQ_FORAGER) && (randomDouble<(QUEEN_BIRTHFREQ_FORAGER+QUEEN_BIRTHFREQ_SCOUT)) )
		   {
			   newAnt=antColonyObj.addAnt(SCOUT_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
		   }
		   else if ( (randomDouble>=(QUEEN_BIRTHFREQ_FORAGER+QUEEN_BIRTHFREQ_SCOUT)) && (randomDouble<=(QUEEN_BIRTHFREQ_FORAGER+QUEEN_BIRTHFREQ_SCOUT+QUEEN_BIRTHFREQ_SOLDIER)) )
		   {
			   newAnt=antColonyObj.addAnt(SOLDIER_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
		   }
	   }
   }
   
   
   
}

