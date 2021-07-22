import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * @author Harry Anastopoulos
 * 
 *	class Ant
 *
 *	The abstract Ant class defines the common class fields and methods applicable to all specialized ant types. 
 */

public abstract class Ant
{
	// Unique identifier for all ant instances
   protected int antID;
   // A numeric int identifier to identify the ant type of an ant instance
   protected int antType;
   // Tracks the current ant colony grid column position on an ant instance
   protected int col;
   // Tracks the current ant colony grid row position on an ant instance
   protected int row;
   // A String representation on the ant instance type
   protected String antTypeName;
   // When an ant is instantiated track the birthday turn of the ant
   protected int antBirthTurn;
   // When an ant is instantiated we already know the turn in which the ant will die
   protected int antDeathTurn;
   // An ant may die of old age or by being killed, we record the death turn for statistics purposes
   protected int antDiedTurn;
   // A quick flag reflecting whether the ant is dead
   protected boolean isAntDead;

   // Utilized as a reference to the ant colony grid squares
   protected ColonyNode[][] colonyNodeGrid=null; 
   // Utilizes as a reference to the ant colony that this ant instance belongs to
   protected AntColony antColonyObj=null;   
   
   /**
    * Ant constructor 
    * 
    * @param pAntColonyObj			object reference to the containing ant colony
    * @param pColonyNodeGrid		object reference to the colony grid square data array structure
    * @param pcol					birth square column of ant instance
    * @param prow					birth square row of ant instance
    * @param pAntID					sequential numeric ID for this ant instance
    * @param pUniversal_Ant_Time	the current universal time expressed in ant turns
    */
   public Ant(AntColony pAntColonyObj, ColonyNode[][] pColonyNodeGrid, int pcol, int prow, int pAntID, int pUniversal_Ant_Time)
   {
	   antColonyObj=pAntColonyObj;
	   colonyNodeGrid=pColonyNodeGrid;
	   col=pcol;
	   row=prow;
	   antID=pAntID;
	   antBirthTurn=pUniversal_Ant_Time;
	   isAntDead=false;
   }
   
   // Get the ant type
   public int getAntType()   
   {
      return antType;
   }

   // Get the ant birth turn
   public int getAntBirthTurn()
   {
	   return antBirthTurn;
   }
   
   // Get the calculated old-age death turn
   public int getAntDeathTurn()
   {
	   return antDeathTurn;
   }   
   
   // Get the actual turn that the ant died or was killed
   public int getAntDiedTurn()
   {
	   return antDiedTurn;
   }   
   
   // Get the ant type in String format
   public String getAntTypeName()
   {
      return antTypeName;
   }
   
   // Get the ant ID
   public int getAntID()
   {
      return antID;
   }     
   
   // Set the ant is dead flag
   public void setIsAntDead(boolean pvalue)
   {
	   isAntDead=pvalue;
   }
   
   // Set the ant died turn value
   public void setAntDiedTurn(int turn)
   {
	   antDiedTurn=turn;
   }
   
   // Get the ant is dead flag
   public boolean getIsAntDead()
   {
	   return isAntDead;
   }
      
   // Define an abstract turn behavior method that all specific ant types must override 
   public abstract void turnBehavior(int turn);
   
}

