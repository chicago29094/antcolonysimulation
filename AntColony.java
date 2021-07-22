import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.text.*;
import javax.swing.Timer;
import java.util.Date;
import java.util.Random;
import dataStructures.ArrayStack;
import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * AntColony controller class
 * 
 * The AntColony class receives events from the Ant Colony Simulation GUI and manages the display and behavior of member ants
 * 
 * @author Harry Anastopoulos
 *
 */

public class AntColony implements AntColonyConstants, SimulationEventListener
{
   // Global tracking of simulation turns
   private int universal_ant_time=1;
   
   // Utilized to assign each ant a unique ID
   private int ant_id_counter=0;
   
   // A reference to the size of the any colony grid
   private int colonyGridSize=0;  

   // An object reference to the Ant Simulation GUI
   private AntSimGUI simulationGUI=null;

   // An object reference to the ColonyView GUI object
   private ColonyView simulationColonyView=null;
   
   // A 2-dimensional array of ColonyNodeView object reference pointers for GUI functions
   // The only simple array allowed for the semester project
   private ColonyNodeView[][] colonyNodeViewGrid=null;   
   
   // A 2-dimensional array of ColonyNodeGrid object reference pointers for colony node square data and logical operations 
   // The only simple array allowed for the semester project
   private ColonyNode[][] colonyNodeGrid=new ColonyNode[COLONY_GRID_SIZE][COLONY_GRID_SIZE]; 
   
   // A master list of all living ants
   private LinkedList colonyAntList=new LinkedList();

   // A stack of dead ants to removed from the colonyAntList LinkedList after LinkedList behavior iterations are performed 
   private ArrayStack colonyDeadAntStack=new ArrayStack();

   // A self-reference to the AntColony  object
   private AntColony antColonyObj=null;
   
   // The runMode variable is set during button GUI event operations or when the Queen ant has died.  It controls the operations of the AntColony
   // Valid modes are: stop, run, step, and "queen is dead" mode.
   private int runMode=COLONY_STOP_MODE;   
   
   // So that data-sensitive ADT iterations aren't disrupted by GUI events, we track whether we are executing an iteration that must complete before
   // significant changes are made to AntColony operations or data structures
   private boolean executingTurn=false;

   // The Ant Colony Simulation speed can be slowed or decreased by this delay variable.  It affects the delay in which a recurring Timer object is triggered.
   private int runDelay=COLONY_DEFAULT_RUN_DELAY;;
   
   // The recommended unified static Random value object.  All randomization function in the Any Colony Simulation utilize this Random object.
   private static Random utilRandom=new Random();
   
   // Ant colony run-time operating statistics variables.  These are strictly for displaying Any Colony Statistics.
   
   private int totalLiveAnts=0;
   private int totalLiveQueens=0;
   private int totalLiveForagers=0;
   private int totalLiveScouts=0;
   private int totalLiveSoldiers=0;
   private int totalLiveBalas=0;
   
   private int totalBornAnts=0;
   private int totalBornQueens=0;
   private int totalBornForagers=0;
   private int totalBornScouts=0;
   private int totalBornSoldiers=0;
   private int totalBornBalas=0;
   
   private int totalDeadAnts=0;
   private int totalDeadQueens=0;
   private int totalDeadForagers=0;
   private int totalDeadScouts=0;
   private int totalDeadSoldiers=0;
   private int totalDeadBalas=0;
   
   private int totalSoldierKillAttempts=0;
   private int totalSoldierKillSuccesses=0;

   private int totalBalaKillAttempts=0;
   private int totalBalaKillSuccesses=0;
   
   private long totalAgeAntDeath=0;

   private int scoutNWCount=0;
   private int scoutNCount=0;
   private int scoutNECount=0;
   private int scoutWCount=0;
   private int scoutECount=0;
   private int scoutSWCount=0;
   private int scoutSCount=0;
   private int scoutSECount=0;
   private int totalScoutMoves=0;

   private int balaNWCount=0;
   private int balaNCount=0;
   private int balaNECount=0;
   private int balaWCount=0;
   private int balaECount=0;
   private int balaSWCount=0;
   private int balaSCount=0;
   private int balaSECount=0;
   private int totalBalaMoves=0;
   
		   
   /**
    * AntColony constructor
    * 
    * The AntColony class manages the entire operation of the GUI and life of the ant colony.
    * 
    * @param psimulationGUI			Object reference to the Ant Colony Simulation GUI
    * @param psimulationColonyView	Object reference to the ColonyView object
    * @param pcolonyNodeViewGrid	Object reference to the ColonyNodeView grid
    * @param pcolonySize			A numeric reference to the size of the colony grid
    */
   
   public AntColony(AntSimGUI psimulationGUI, ColonyView psimulationColonyView, ColonyNodeView[][] pcolonyNodeViewGrid, int pcolonySize)
   {
	   int i=0, j=0, k=0;

	   colonyGridSize=pcolonySize;
	   simulationGUI=psimulationGUI;
	   simulationColonyView=psimulationColonyView;
	   colonyNodeViewGrid=pcolonyNodeViewGrid;
	   antColonyObj=this;
	   setRunMode(COLONY_STOP_MODE);
	   executingTurn=false;
	   
	   // Instantiate the ColonyNodeView objects and add them to the Ant Simulation ColonyView
	   // In parallel, instantiate the colonyNodeGrid array and pass a reference to each corresponding ColonyNodeView square
	   for (i=0; i<COLONY_GRID_SIZE; i++)
	   {
		   	for (j=0; j<COLONY_GRID_SIZE; j++)
	   		{
			   	colonyNodeViewGrid[i][j]=new ColonyNodeView();			   	
	   			simulationColonyView.addColonyNodeView(colonyNodeViewGrid[i][j], i, j);   			
			   	colonyNodeGrid[i][j]=new ColonyNode(i, j, colonyNodeViewGrid[i][j]);
	   		}
	   }     
	   
	  // Add the AntColony as an event listener for GUI Simulation Events, which are basically GUI button operations 
      simulationGUI.addSimulationEventListener(this);
      
      // Update the simulation speed display label
      simulationGUI.setSpeed("Delay Factor: " + runDelay + "    ");

      // Update the simulation time label
      simulationGUI.setTime("Colony Time:  Year:" + 1 + "   Day:" + 1 + "   Turn:" + 0 + "   Overall Turns Completed:" + 0);

      // Reset the Ant Colony statistics
      resetAntColonyStatistics();
      
   }      
    
   // Reset the ant colony operating statistics
   public void resetAntColonyStatistics()
   {
      // Variables used to track ant colony statistics
      totalLiveAnts=0;
      totalLiveQueens=0;
      totalLiveForagers=0;
      totalLiveScouts=0;
      totalLiveSoldiers=0;
      totalLiveBalas=0;
      
      totalBornAnts=0;
      totalBornQueens=0;
      totalBornForagers=0;
      totalBornScouts=0;
      totalBornSoldiers=0;
      totalBornBalas=0;
      
      totalDeadAnts=0;
      totalDeadQueens=0;
      totalDeadForagers=0;
      totalDeadScouts=0;
      totalDeadSoldiers=0;
      totalDeadBalas=0;
      
      totalSoldierKillAttempts=0;
      totalSoldierKillSuccesses=0;

      totalBalaKillAttempts=0;
      totalBalaKillSuccesses=0;
      
      totalAgeAntDeath=0;

      scoutNWCount=0;
      scoutNCount=0;
      scoutNECount=0;
      scoutWCount=0;
      scoutECount=0;
      scoutSWCount=0;
      scoutSCount=0;
      scoutSECount=0;
      totalScoutMoves=0;

      balaNWCount=0;
      balaNCount=0;
      balaNECount=0;
      balaWCount=0;
      balaECount=0;
      balaSWCount=0;
      balaSCount=0;
      balaSECount=0;
      totalBalaMoves=0;
                  
   };

   /*
    * Colony Initialization Method
    * 
    * initializeColony, usually called by pressing the "Normal Setup" button, prepares the AntColony for initial operation.
    * 
    */
   
  
   public void initializeColony()
   {
	   int i=0, j=0, k=0;
	   int randomInt=0;
	   double randomDouble;
	   Ant newAnt;
	   String nodeID=new String();
	   	
	   // Make sure the operation of the colony is stopped
	   setRunMode(COLONY_STOP_MODE);
	   
	   // Reset the screen labels
	   simulationGUI.setSpeed("Delay Factor: " + runDelay + "    ");
	   simulationGUI.setTime("Colony Time:  Year:" + 1 + "   Day:" + 1 + "   Turn:" + 0 + "   Overall Turns Completed:" + 0);

	   // Reset the tracking statistics
	   resetAntColonyStatistics();	   
	   
	   // Set the initial state of the simulation	   
	   
	   // First, reset all nodes in the GUI and the colonyNode data grid
	   
	   for (i=0; i<COLONY_GRID_SIZE; i++)
	   {
		   	for (j=0; j<COLONY_GRID_SIZE; j++)
	   		{
	   			colonyNodeGrid[i][j].hideNode();
	   			colonyNodeGrid[i][j].setQueenNode(false);
	   			colonyNodeGrid[i][j].setForagerCount(0);
	   			colonyNodeGrid[i][j].setScoutCount(0);	    		  
	   			colonyNodeGrid[i][j].setSoldierCount(0);	    		  
	   			colonyNodeGrid[i][j].setBalaCount(0);	  
	   			
	   			// We have opted to assign Colony Grid square food values when the colony is initialized, instead of when a square is revealed.
	   			// We select a random value from 0 to 1, and if the random vale is between zero and the "Food Present" probability we 
	   			// assign a random amount of food between 500 and 1000 units.
	   				   			
	   			randomDouble=getRandomDouble();
	   			if ( (randomDouble>=0) && (randomDouble<COLONY_FOODPRESENT_PROBABILITY) )
	   			{
	   				colonyNodeGrid[i][j].setFoodCount(getRandomRange(500, 1000));	
	   			}
	   			else
	   			{
	   				// No food is present in this square
	   				colonyNodeGrid[i][j].setFoodCount(0);
	   			}
	   			
	   			// Initially there is no pheromone in any square
	   			colonyNodeGrid[i][j].setPheromoneCount(0);		   			
	   		}
	   }     
	   
	   // Display the colony entrance and surrounding 8 node squares

	   for (i=COLONY_CENTER_COL-1; i<=COLONY_CENTER_COL+1; i++)
	   {
		   for (j=COLONY_CENTER_ROW-1; j<=COLONY_CENTER_ROW+1; j++)
		   {
			   colonyNodeGrid[i][j].showNode();	   	
		   }
	   }
	   
	   // Set the initial colony entrance queen node values
	   
	   colonyNodeGrid[COLONY_CENTER_COL][COLONY_CENTER_ROW].setFoodCount(COLONY_INITIAL_FOOD_UNITS);
	   
	   // Add the Queen ant first.  Specs. call for her being ant ID #0
	   
	   newAnt=addAnt(QUEEN_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
	  
	    // Add the initial Soldier ants
	   for (i=0; i<COLONY_INITIAL_SOLDIER_ANTS; i++)
	   {
		   newAnt=addAnt(SOLDIER_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
	   }

	   // Add the initial Forager ants
	   for (i=0; i<COLONY_INITIAL_FORAGER_ANTS; i++)
	   {
		   newAnt=addAnt(FORAGER_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
	   }

	   // Add the initial Scout ants
	   for (i=0; i<COLONY_INITIAL_SCOUT_ANTS; i++)
	   {
		   newAnt=addAnt(SCOUT_ANT_TYPE, COLONY_CENTER_COL, COLONY_CENTER_ROW);
	   }
	   
   };
   
   // We are using a Java Timer class to rerun colonySingleStep after a delay
   ActionListener timerHandler=new ActionListener()
   {
      public void actionPerformed(ActionEvent evt)
      {
   	   	 if (runMode==COLONY_RUN_MODE)
   	   	 {
   	   		colonySingleStep();
   	   	 }   
    	  
      }
   };
   Timer antTurnTimer = new Timer(runDelay, timerHandler);
   
   /*
    * colonSingleStep Method 
    * 
    * This is the main iterating control structure for the entire Ant Colony.
    * This method is called repeatedly automatically by a timer in "Run" mode
    * and is also called individually in "Step" mode when the step button is clicked.
    * 
    */
   public void colonySingleStep()
   {
	   Ant colonyAnt;
	   double randomDouble;
	   
	   // We don't allow colonySingleStep to be called again if a GUI event calls for it
	   // if this is already running.  We track this with the "executingTurn" variable.
	   executingTurn=true;	 

	   // We don't know how long a single colony "turn" will take, so we stop the "Run" mode
	   // timer in case it is running.  We don't want colonySingleStep to be called a second time
	   // if we haven't completed our previous iteration.
	   antTurnTimer.stop();

	   displayColonyTime();
	   
	   // In there are no ants, something is wrong, so run the initialization method.
	   if (colonyAntList.size()==0)
	   {
		   int localRunMode=runMode;
		   initializeColony();
		   runMode=localRunMode;
	   }
	   
	   // Check all squares for pheromone and decay the amount by the specified decay ratio (default 50%) once every 10 turns (11, 21, 31, 41, etc.)
	   if (universal_ant_time%COLONY_TURNS_PER_DAY==1)
	   {
		   for (int i = 0; i<COLONY_GRID_SIZE; i++)
		   {
			   for (int j=0; j<COLONY_GRID_SIZE; j++)
	   			{    
		   			int pheromoneCount=(int) (colonyNodeGrid[i][j].getPheromoneCount()*COLONY_PHEROMONE_DECAYRATIO);
		   			colonyNodeGrid[i][j].setPheromoneCount(pheromoneCount);		   			
	   			}
		   }     	
	   }
	   
	   // Add a Bala ant on an edqe square based on the colony Bala birth frequency (default 3%)
	   randomDouble=getRandomDouble();
	   if ( (randomDouble>=0) && (randomDouble<COLONY_BALA_BIRTHFREQ) )
	   {
		   int randomInt=getRandomRange(1, 4);
		   int randomInt2=getRandomRange(0, COLONY_GRID_SIZE-1);
		   
		   // We pick a random side, N, W, E, S and add a Bala ant on that side
		   if (randomInt==1)
		   {
			   colonyAnt=addAnt(BALA_ANT_TYPE, randomInt2, 0);
		   }
		   else if (randomInt==2)
		   {
			   colonyAnt=addAnt(BALA_ANT_TYPE, COLONY_GRID_SIZE-1, randomInt2);
		   }
		   else if (randomInt==3)
		   {
			   colonyAnt=addAnt(BALA_ANT_TYPE, randomInt2, COLONY_GRID_SIZE-1);
		   }
		   else if (randomInt==4)
		   {
			   colonyAnt=addAnt(BALA_ANT_TYPE, 0, randomInt2);
		   }
	   }
	   
	   // Here is the main iterating loop for the Ant Colony Simulation.
	   // If there are living ants in our master LinkedList of ants, we will
	   // iterate through every ant and give them one turn with their specific behavior
	   
	   if (colonyAntList.size()>0)
	   {
		   try
		   {
			   // Get the first Ant object from our linked list
			   colonyAnt=(Ant) colonyAntList.getFirst();
			   
			   // The queen was added first and should always by extension execute her turn first
			   if (colonyAnt.getAntType()==QUEEN_ANT_TYPE)
			   {
				   // Execute one turn of ant behavior
				   colonyAnt.turnBehavior(universal_ant_time);	   
			   }
			   
			   // Iterate through all ants in our colony, but abort the iteration if the queen dies at some point
			   for (Iterator itr=colonyAntList.iterator(); (runMode!=COLONY_QUEEN_DEAD) && itr.hasNext();)
			   {
				   colonyAnt=(Ant) itr.getCurrent();
				   	
				   // Don't execute a turn for dead ants or for the queen, the queen executed first
				   if ( (!colonyAnt.getIsAntDead()) && (colonyAnt.getAntType()!=QUEEN_ANT_TYPE) )
				   {
					   colonyAnt.turnBehavior(universal_ant_time);
				   }
				   		
				   itr.next();
			   }
		   }
		   catch (IndexOutOfBoundsException e)
		   {	
			   System.out.println("AntColony Uncaught Exception");
			   setRunMode(COLONY_STOP_MODE);
			   // End Simulation
		   }
		   
		   // Since we can't remove objects from our LinkedList during iteration, we store a stack of dead ant objects, and remove them in bulk now
		   if (colonyDeadAntStack.size()>0)
		   {
			   while (!colonyDeadAntStack.isEmpty())
			   {
				   colonyAnt=(Ant) colonyDeadAntStack.pop();
				   colonyAntList.remove(colonyAnt);		   
			   }
		   }
		   

		   // One turn is over.  Now we iterate to the next turn.
		   
		   incrementAntTime();

	   }
	   
	   // Now that we are done iterating, we set the executingTurn flag to false and allow button events to occur normally
	   executingTurn=false;
	   
	   // If we are in "Run" mode, we set the Swing timer to resume, but at the current runDelay, which may have changed 
	   // by button events.
	   if (runMode==COLONY_RUN_MODE)
	   {
		   antTurnTimer.setInitialDelay(runDelay);
		   antTurnTimer.restart();
	   }
   }
   
   // Increment the global ant turn time
   private void incrementAntTime()
   {
	   universal_ant_time++;
   }
   
   // Display Ant Colony time information in the GUI
   private void displayColonyTime()
   {
	   String colonyTime=new String();
	   String colonyYears=new String();
	   String colonyDays=new String();
	   String colonyTurns=new String();
	   String colonyOverallTurns=new String();
	   
	   if (universal_ant_time%(365*COLONY_TURNS_PER_DAY)==0)
	   {
		   colonyYears="" + ((universal_ant_time/(365*COLONY_TURNS_PER_DAY)));		   
	   }
	   else
	   {
		   colonyYears="" + (1+(universal_ant_time/(365*COLONY_TURNS_PER_DAY)));
	   }
	   
	   if ((universal_ant_time%COLONY_TURNS_PER_DAY)==0)
	   {
		   colonyDays="" + ((universal_ant_time/(COLONY_TURNS_PER_DAY)));		   
	   }
	   else
	   {
		   colonyDays="" + (1+(universal_ant_time/(COLONY_TURNS_PER_DAY)));
	   }
	   
	   colonyTurns="" + (universal_ant_time%(COLONY_TURNS_PER_DAY));
	   
	   if (colonyTurns.equals("0"))
	   {
		   colonyTurns="10"; 
	   }
	   
	   colonyOverallTurns="" + universal_ant_time;
	   
	   colonyTime="Colony Time:  Year:" + colonyYears + "   Day:" + colonyDays + "   Turn:" + colonyTurns + "   Overall Turns Completed:" + colonyOverallTurns;
	   simulationGUI.setTime(colonyTime);
   }
   
   /**
    * addAnt method
    * 
    * This method adds a new ant to the colony by instantiating the corresponding Ant object type.
    * 
    * @param antType	A numeric identifier for the any type.
    * @param col		The colony grid column position of the new ant.
    * @param row		The colony grid row position of the new ant.
    * @return			Return a reference to the new Ant object
    * 
    */
   public Ant addAnt(int antType, int col, int row)
   {
	   Ant newAnt=null;
	  
	   // Add a queen ant
	   if (antType==QUEEN_ANT_TYPE)
	   {
		   newAnt=new Queen(antColonyObj, colonyNodeGrid, col, row, ant_id_counter++, universal_ant_time);
		   colonyAntList.add(newAnt);
		   colonyNodeGrid[col][row].addNodeAntList(newAnt);
		   colonyNodeGrid[col][row].setQueenNode(true);	
		   colonyNodeGrid[col][row].incrQueenCount(1);
		   totalLiveAnts++;
		   totalBornAnts++;
		   totalLiveQueens++;
		   totalBornQueens++;		   
	   }
	   // Add a Forager ant
	   else if (antType==FORAGER_ANT_TYPE)
	   {
		   newAnt=new Forager(antColonyObj, colonyNodeGrid, col, row, ant_id_counter++, universal_ant_time);
		   colonyAntList.add(newAnt);
		   colonyNodeGrid[col][row].addNodeAntList(newAnt);
		   colonyNodeGrid[col][row].incrForagerCount(1); 
		   totalLiveAnts++;
		   totalBornAnts++;
		   totalLiveForagers++;
		   totalBornForagers++;		   
	   }
	   // Add a Scout ant
	   else if (antType==SCOUT_ANT_TYPE)
	   {
		   newAnt=new Scout(antColonyObj, colonyNodeGrid, col, row, ant_id_counter++, universal_ant_time);
		   colonyAntList.add(newAnt);
		   colonyNodeGrid[col][row].addNodeAntList(newAnt);
		   colonyNodeGrid[col][row].incrScoutCount(1); 	
		   totalLiveAnts++;
		   totalBornAnts++;
		   totalLiveScouts++;
		   totalBornScouts++;
	   }
	   // Add a Soldier ant
	   else if (antType==SOLDIER_ANT_TYPE)
	   {
		   newAnt=new Soldier(antColonyObj, colonyNodeGrid, col, row, ant_id_counter++, universal_ant_time);
		   colonyAntList.add(newAnt);
		   colonyNodeGrid[col][row].addNodeAntList(newAnt);
		   colonyNodeGrid[col][row].incrSoldierCount(1); 	
		   totalLiveAnts++;
		   totalBornAnts++;
		   totalLiveSoldiers++;
		   totalBornSoldiers++;
	   }
	   // Add a Bala ant
	   else if (antType==BALA_ANT_TYPE)
	   {
		   newAnt=new Bala(antColonyObj, colonyNodeGrid, col, row, ant_id_counter++, universal_ant_time);
		   colonyAntList.add(newAnt);
		   colonyNodeGrid[col][row].addNodeAntList(newAnt);
		   colonyNodeGrid[col][row].incrBalaCount(1); 
		   totalLiveAnts++;	 
		   totalBornAnts++;
		   totalLiveBalas++;
		   totalBornBalas++;
	   }
	   
	   // Return a Ant object reference to the newly instatiated ant
	   return newAnt;
   };
      
   /**
    * removeAnt
    * 
    * The removeAnt method immediately removes an Ant object from the colony.
    * 
    * @param antObject		An object reference for the Ant to be removed
    * @param col			The ant's current grid column.
    * @param row			The ant's current grid row.
    */
   public void removeAnt(Ant antObject, int col, int row)
   {
	   int antType=antObject.getAntType();
	   
	   // Keep a running aggregate total of how old ants are when they die.  This is for a life expectancy calculation.
	   totalAgeAntDeath=totalAgeAntDeath+(antObject.getAntDiedTurn()-antObject.getAntBirthTurn());
	   
	   // Remove a Queen ant
	   if (antType==QUEEN_ANT_TYPE)
	   {
		   colonyAntList.remove(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrQueenCount(1);
		   totalLiveAnts--;
		   totalLiveQueens--;
		   totalDeadAnts++;
		   totalDeadQueens++;
		   
		   if (colonyNodeGrid[col][row].getQueenCount()==0)
		   {
			   colonyNodeGrid[col][row].setQueenNode(false);
			   setRunMode(COLONY_QUEEN_DEAD);
		   }		   
	   }
	   // Remove a Forager ant
	   else if (antType==FORAGER_ANT_TYPE)
	   {
		   colonyAntList.remove(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrForagerCount(1); 
		   totalLiveAnts--;
		   totalLiveForagers--;
		   totalDeadAnts++;
		   totalDeadForagers++;
	   }
	   // Remove a Scout ant
	   else if (antType==SCOUT_ANT_TYPE)
	   {
		   colonyAntList.remove(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrScoutCount(1); 		
		   totalLiveAnts--;
		   totalLiveScouts--;
		   totalDeadAnts++;
		   totalDeadScouts++;
	   }
	   // Remove a Soldier ant
	   else if (antType==SOLDIER_ANT_TYPE)
	   {
		   colonyAntList.remove(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrSoldierCount(1); 
		   totalLiveAnts--;
		   totalLiveSoldiers--;
		   totalDeadAnts++;
		   totalDeadSoldiers++;
	   }
	   // Remove a Bala ant
	   else if (antType==BALA_ANT_TYPE)
	   {
		   colonyAntList.remove(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrBalaCount(1); 	
		   totalLiveAnts--;
		   totalLiveBalas--;
		   totalDeadAnts++;
		   totalDeadBalas++;
	   }	   	    
   };
   
   /**
    * removeAntDelayed
    * 
    * The removeAntDelayed method removes an Ant object from the colony grid immediately, but from AntColony LinkedList data structures after an ongoing iteration has occurred. 
    * Using this method, dead ants are placed in a dead ant stack and removed in a batch, when it is safe to do so.
    * 
    * @param antObject		An object reference for the Ant to be removed
    * @param col			The ant's current grid column.
    * @param row			The ant's current grid row.
    */
   public void removeAntDelayed(Ant antObject, int col, int row)
   {
	   int antType=antObject.getAntType();
	   
	   totalAgeAntDeath=totalAgeAntDeath+(antObject.getAntDiedTurn()-antObject.getAntBirthTurn());
	   
	   // Remove Queen ant
	   if (antType==QUEEN_ANT_TYPE)
	   {
		   colonyDeadAntStack.push(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrQueenCount(1);
		   totalLiveAnts--;
		   totalLiveQueens--;
		   totalDeadAnts++;
		   totalDeadQueens++;		   
		   
		   if (colonyNodeGrid[col][row].getQueenCount()==0)
		   {
			   colonyNodeGrid[col][row].setQueenNode(false);
			   setRunMode(COLONY_QUEEN_DEAD);
		   }		   
	   }
	   // Remove Forager ant
	   else if (antType==FORAGER_ANT_TYPE)
	   {
		   colonyDeadAntStack.push(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrForagerCount(1); 
		   totalLiveAnts--;
		   totalLiveForagers--;
		   totalDeadAnts++;
		   totalDeadForagers++;		   
	   }
	   // Remove Scout ant
	   else if (antType==SCOUT_ANT_TYPE)
	   {
		   colonyDeadAntStack.push(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrScoutCount(1); 		
		   totalLiveAnts--;
		   totalLiveScouts--;
		   totalDeadAnts++;
		   totalDeadScouts++;		   
	   }
	   // Remove Soldier ant
	   else if (antType==SOLDIER_ANT_TYPE)
	   {
		   colonyDeadAntStack.push(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrSoldierCount(1); 	
		   totalLiveAnts--;
		   totalLiveSoldiers--;
		   totalDeadAnts++;
		   totalDeadSoldiers++;		   
	   }
	   // Remove Bala ant
	   else if (antType==BALA_ANT_TYPE)
	   {
		   colonyDeadAntStack.push(antObject);
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrBalaCount(1); 
		   totalLiveAnts--;
		   totalLiveBalas--;
		   totalDeadAnts++;
		   totalDeadBalas++;		   
	   }	   	    
   };
   
   // Track Soldier ant kill statistics
   public void updateSoldierKillStats(Boolean success)
   {
	   totalSoldierKillAttempts++;
	   if (success)
	   {
		   totalSoldierKillSuccesses++;
	   }
   }

   // Track Bala ant kill statistics
   public void updateBalaKillStats(Boolean success)
   {
	   totalBalaKillAttempts++;
	   if (success)
	   {
		   totalBalaKillSuccesses++;
	   }
   }   
   
   // Track Scout ant movement randomness statistics
   public void updateScoutMoveStats(int borderNode)
   {
	   totalScoutMoves++;
	   
	   if (borderNode==1)		scoutNWCount++;
	   else if (borderNode==2)	scoutNCount++;
	   else if (borderNode==3)	scoutNECount++;
	   else if (borderNode==4)	scoutWCount++;
	   else if (borderNode==5)	scoutECount++;
	   else if (borderNode==6)	scoutSWCount++;
	   else if (borderNode==7)	scoutSCount++;
	   else if (borderNode==8)	scoutSECount++;	   
   }
   
   // Track Bala ant movement randomness statistics
   public void updateBalaMoveStats(int borderNode)
   {
	   totalBalaMoves++;

	   if (borderNode==1)		balaNWCount++;
	   else if (borderNode==2)	balaNCount++;
	   else if (borderNode==3)	balaNECount++;
	   else if (borderNode==4)	balaWCount++;
	   else if (borderNode==5)	balaECount++;
	   else if (borderNode==6)	balaSWCount++;
	   else if (borderNode==7)	balaSCount++;
	   else if (borderNode==8)	balaSECount++;	   
   }
   
   /**
    * moveAnt
    * 
    * The moveAnt method moves an Ant object from one grid square to another
    * 
    * @param antObject		A reference to the Ant object
    * @param col			The original ant column.
    * @param row			The original ant row.
    * @param newcol			The destination ant column.
    * @param newrow			The destination ant row.
    */
   public void moveAnt(Ant antObject, int col, int row, int newcol, int newrow)
   {
	   int antType=antObject.getAntType();
	   
	   
	   // The queen does not move!!! 
	   if (antType==QUEEN_ANT_TYPE)
	   {
		   return;
	   }
	   // Move a Forager Ant
	   else if (antType==FORAGER_ANT_TYPE)
	   {
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrForagerCount(1); 
		   colonyNodeGrid[newcol][newrow].addNodeAntList(antObject);
		   colonyNodeGrid[newcol][newrow].incrForagerCount(1); 		   
	   }
	   // Move a Scout Ant
	   else if (antType==SCOUT_ANT_TYPE)
	   {
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrScoutCount(1); 		   
		   colonyNodeGrid[newcol][newrow].addNodeAntList(antObject);
		   colonyNodeGrid[newcol][newrow].incrScoutCount(1); 		
	   }
       // Move a Soldier Ant
	   else if (antType==SOLDIER_ANT_TYPE)
	   {
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrSoldierCount(1); 		   
		   colonyNodeGrid[newcol][newrow].addNodeAntList(antObject);
		   colonyNodeGrid[newcol][newrow].incrSoldierCount(1); 		
	   }
	   // Move a Bala Ant
	   else if (antType==BALA_ANT_TYPE)
	   {
		   colonyNodeGrid[col][row].removeNodeAntList(antObject);
		   colonyNodeGrid[col][row].decrBalaCount(1); 		   
		   colonyNodeGrid[newcol][newrow].addNodeAntList(antObject);
		   colonyNodeGrid[newcol][newrow].incrBalaCount(1); 		
	   }	   	    
   };
   
   // Setter method to set the curret run mode
   public void setRunMode(int prunMode)
   {
	   runMode=prunMode;
	   
	   if (runMode==COLONY_QUEEN_DEAD)
	   {
		   // Annnounce the death of the queen
		   JOptionPane.showMessageDialog(null, "The Ant Colony Queen Ant is Dead.",  "Simulation Stopped", JOptionPane.ERROR_MESSAGE);
	   }
   }
   
   // End the Ant Colony Simulation
   public void endSimulation()
   {
	   // Currently not implemented.
   };
   
   /**
    * simulationEventOccurred
    * 
    * We register ourselves as a simulationEvent listener and process the events in this method.
    *  
    */
   public void simulationEventOccurred(SimulationEvent simEvent) 
   {
	   // Process the Normal Setup button event
	   if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) 
	   {
		   // set up the simulation for normal operation 
		   
		   setRunMode(COLONY_STOP_MODE);
		   
		   if (!executingTurn)
		   {
		   		initializeColony();
		   }
	   }
	   // Process the Display Statistics button event
       else if (simEvent.getEventType() == SimulationEvent.DISPLAY_STATISTICS_EVENT)  
       {
    	   // display current statistics for the any colony
    	   
    	   displayColonyStatistics();		   
       }
	   // Process the Decrease Speed button event
       else if (simEvent.getEventType() == SimulationEvent.DECREASE_SPEED_EVENT) 
       {
    	   // fire to decrease the running speed
    	   
    	   if (runDelay==0)
    	   {
    		   runDelay=1;
    	   }
    	   else
    	   {
    		   runDelay=(int)((double) runDelay * 1.10);
    	   }
    	   simulationGUI.setSpeed("Delay Factor: "+runDelay+"    ");
		   
       }
	   // Process the Increase Speed button event
       else if (simEvent.getEventType() == SimulationEvent.INCREASE_SPEED_EVENT) 
       {
    	   // fire to increase the running speed
    	   
    	   runDelay=(int)((double) runDelay * 0.90);
    	   simulationGUI.setSpeed("Delay Factor: "+runDelay+"    ");
    	   
       }
	   // Process the Run button event
	   else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT)
	   {		   
		   if (runMode==COLONY_QUEEN_DEAD)
		   {
			   return;
		   }
		   
		   // run the simulation continuously
		   setRunMode(COLONY_RUN_MODE);
		   
		   if (!executingTurn)
		   {
			   colonySingleStep();
		   }
	   }
	   // Process the Step button event
	   else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) 
	   {
		   
		   if (runMode==COLONY_QUEEN_DEAD)
		   {
			   return;
		   }
		   		   
		   	// run the next turn of the simulation 
		   setRunMode(COLONY_STEP_MODE);
		   
		   if (!executingTurn)
		   {
			   colonySingleStep();
		   }
	   }
	   else
	   {
		   // invalid event occurred - probably will never happen }
		   System.out.println("Invalid Event");
	   }
   	}
   
    /**
     *  displayColonyStatistics
     *  
     *  Displays a snapshot of counts, totals, and statistics regarding the currently running Ant Colony
     */
   public void displayColonyStatistics()
   {
	   // Define a number display format for controlled float value formatting   
	   String pattern = "#######0.0000";  
	   DecimalFormat swf = new DecimalFormat(pattern);   
	   
	   String message=new String();
	   
	   message=message+"<html>\n";
	   message=message+"<body>\n";
	   message=message+"<table border=1 cellspacing=0 cellpadding=5 width=\"300px\">\n";
	   
	   message=message+"<tr><td align=center colspan=2 bgcolor=\"#E0E0E0\">ANT COLONY STATISTICS</td></tr>\n";
	   
	   message=message+"<tr bgcolor=\"#E0E0E0\"><td align=center><b>Statistic</b></td><td align=center><b>Value</b></td></tr>\n";
	   
	   message=message+"<tr><td align=center>Colony Size</td><td align=center>" + COLONY_GRID_SIZE + "x" + COLONY_GRID_SIZE + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Turns Completed</td><td align=center>" + (universal_ant_time-1) + "</td></tr>\n";
	   
	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">LIVE ANTS</td></tr>\n";
	   
	   message=message+"<tr><td align=center>Total Live Ants</td><td align=center>" + totalLiveAnts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Live Queens</td><td align=center>" + totalLiveQueens + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Live Foragers</td><td align=center>" + totalLiveForagers + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Live Scouts</td><td align=center>" + totalLiveScouts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Live Soldiers</td><td align=center>" + totalLiveSoldiers + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Live Balas</td><td align=center>" + totalLiveBalas + "</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">ANT BIRTHS</td></tr>\n";	   
	   
	   message=message+"<tr><td align=center>Total Born Ants</td><td align=center>" + totalBornAnts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Queens</td><td align=center>" + totalBornQueens + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Foragers</td><td align=center>" + totalBornForagers + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Scouts</td><td align=center>" + totalBornScouts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Soldiers</td><td align=center>" + totalBornSoldiers + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Balas</td><td align=center>" + totalBornBalas + "</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">BIRTH PERCENTAGES</td></tr>\n";	   	   
	   
	   message=message+"<tr><td align=center>Total Born Ants Percentage</td><td align=center>" + swf.format((double)(totalBornAnts-totalBornBalas)/(double)(totalBornAnts-totalBornBalas)) + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Queens Percentage</td><td align=center>" + swf.format((double)totalBornQueens/(double)(totalBornAnts-totalBornBalas)) + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Foragers Percentage</td><td align=center>" + swf.format((double)totalBornForagers/(double)(totalBornAnts-totalBornBalas)) + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Scouts Percentage</td><td align=center>" + swf.format((double)totalBornScouts/(double)(totalBornAnts-totalBornBalas)) + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Born Soldiers Percentage</td><td align=center>" + swf.format((double)totalBornSoldiers/(double)(totalBornAnts-totalBornBalas)) + "</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">BALA BIRTH PERCENTAGE / TURN</td></tr>\n";	   	   
	   
	   message=message+"<tr><td align=center>Total Born Balas Percentage</td><td align=center>" + swf.format((double)totalBornBalas/(double)universal_ant_time) + "</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">ANT DEATHS</td></tr>\n";	   	   	   
	   
		message=message+"<tr><td align=center>Total Dead Ants</td><td align=center>" + totalDeadAnts + "</td></tr>\n";
	   	message=message+"<tr><td align=center>Total Dead Queens</td><td align=center>" + totalDeadQueens + "</td></tr>\n";
	   	message=message+"<tr><td align=center>Total Dead Foragers</td><td align=center>" + totalDeadForagers + "</td></tr>\n";
	   	message=message+"<tr><td align=center>Total Dead Scouts</td><td align=center>" + totalDeadScouts + "</td></tr>\n";
	   	message=message+"<tr><td align=center>Total Dead Soldiers</td><td align=center>" + totalDeadSoldiers + "</td></tr>\n";
	   	message=message+"<tr><td align=center>Total Dead Balas</td><td align=center>" + totalDeadBalas + "</td></tr>\n";
	   
	   
	   message=message+"<tr><td align=center>Total Killed Ants</td><td align=center>" + (totalSoldierKillSuccesses+totalBalaKillSuccesses) + "</td></tr>\n";

	   message=message+"<tr><td align=center>Total Ant Life</td><td align=center>" + (totalAgeAntDeath) + "</td></tr>\n";

	   if (totalDeadAnts>0)
		   message=message+"<tr><td align=center>Average Ant Life Expectancy</td><td align=center>" + swf.format((double)totalAgeAntDeath/(double)totalDeadAnts) + " Turns</td></tr>\n";
	   else
		   message=message+"<tr><td align=center>Average Ant Life Expectancy</td><td align=center>" + "--" + " Turns</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">SOLDIER KILL RATIO</td></tr>\n";	   	   	   	   
	   
	   message=message+"<tr><td align=center>Total Soldier Kill Attempts</td><td align=center>" + totalSoldierKillAttempts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Soldier Kill Successes</td><td align=center>" + totalSoldierKillSuccesses + "</td></tr>\n";

	   if (totalSoldierKillAttempts>0)
		   message=message+"<tr><td align=center>Total Soldier Kill Percentage</td><td align=center>" + swf.format((double)totalSoldierKillSuccesses/(double)totalSoldierKillAttempts) + "</td></tr>\n";
	   else
		   message=message+"<tr><td align=center>Total Soldier Kill Percentage</td><td align=center>" + "--" + "</td></tr>\n";

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">BALA KILL RATIO</td></tr>\n";	   	   	   	    
	   
	   message=message+"<tr><td align=center>Total Bala Kill Attempts</td><td align=center>" + totalBalaKillAttempts + "</td></tr>\n";
	   message=message+"<tr><td align=center>Total Bala Kill Successes</td><td align=center>" + totalBalaKillSuccesses + "</td></tr>\n";

	   if (totalBalaKillAttempts>0)
		   message=message+"<tr><td align=center>Total Bala Kill Percentage</td><td align=center>" + swf.format((double)totalBalaKillSuccesses/(double)totalBalaKillAttempts) + "</td></tr>\n";
	   else
		   message=message+"<tr><td align=center>Total Bala Kill Percentage</td><td align=center>" + "--" + "</td></tr>\n";
	
	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">SCOUT MOVEMENT PERCENTAGES</td></tr>\n";	   	   	   	   
	   
	   if (totalScoutMoves==0)
	   {
		    message=message+"<tr><td align=center>Scout NW Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout N Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout NE Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout W Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout E Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout SW Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout S Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Scout SE Moves</td><td align=center>" + "--" + "</td></tr>\n";
	   }
	   else
	   {
		   message=message+"<tr><td align=center>Scout NW Moves</td><td align=center>" + swf.format((double)scoutNWCount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout N Moves</td><td align=center>" + swf.format((double)scoutNCount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout NE Moves</td><td align=center>" + swf.format((double)scoutNECount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout W Moves</td><td align=center>" + swf.format((double)scoutWCount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout E Moves</td><td align=center>" + swf.format((double)scoutECount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout SW Moves</td><td align=center>" + swf.format((double)scoutSWCount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout S Moves</td><td align=center>" + swf.format((double)scoutSCount/(double)totalScoutMoves) + "</td></tr>\n";
		   message=message+"<tr><td align=center>Scout SE Moves</td><td align=center>" + swf.format((double)scoutSECount/(double)totalScoutMoves) + "</td></tr>\n";		   
	   }

	   message=message+"<tr><td align=center colspan=2  bgcolor=\"#E0E0E0\">BALA MOVEMENT PERCENTAGES</td></tr>\n";	   	   	   	   	   
	   
	   if (totalBalaMoves==0)
	   {
		   message=message+"<tr><td align=center>Bala NW Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala N Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala NE Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala W Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala E Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala SW Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala S Moves</td><td align=center>" + "--" + "</td></tr>\n";
		   message=message+"<tr><td align=center>Bala SE Moves</td><td align=center>" + "--" + "</td></tr>\n";		   
	   }
	   else
	   {
		   	message=message+"<tr><td align=center>Bala NW Moves</td><td align=center>" + swf.format((double)balaNWCount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala N Moves</td><td align=center>" + swf.format((double)balaNCount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala NE Moves</td><td align=center>" + swf.format((double)balaNECount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala W Moves</td><td align=center>" + swf.format((double)balaWCount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala E Moves</td><td align=center>" + swf.format((double)balaECount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala SW Moves</td><td align=center>" + swf.format((double)balaSWCount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala S Moves</td><td align=center>" + swf.format((double)balaSCount/(double)totalBalaMoves) + "</td></tr>\n";
	   		message=message+"<tr><td align=center>Bala SE Moves</td><td align=center>" + swf.format((double)balaSECount/(double)totalBalaMoves) + "</td></tr>\n";
	   }
	   
	   message=message+"</table>\n<BR>\n<BR>\n";
	   message=message+"</body>\n";
	   message=message+"</html>\n";	   

	   JLabel htmlLabel = new JLabel(message);
	   JPanel panel=new JPanel();
	   JScrollPane scrollPane=new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   Container container;
	   JFrame statsFrame=new JFrame("Ant Colony Statistics");
	   statsFrame.setBounds(100,100,500,600);
	   statsFrame.setVisible(true);
	   //statsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   container=statsFrame.getContentPane();
	   container.add(scrollPane);
	   panel.add(htmlLabel);
	   
   }
   
   // Return a random integer within a minimum and maximum range, inclusively
   public static int getRandomRange(int min, int max)
   {
	   int i=utilRandom.nextInt(max-min+1)+min;
	   return i;
   }

   // Return a random double number between 0 and less than 1
   public static double getRandomDouble()
   {
	   double i=utilRandom.nextDouble();
	   return i;
   }   
   
   
}