
/**
 * 
 * @author Harry Anastopoulos
 *
 * The AntColonyConstants class provides an interface that all Ant Colony Simulation classes can implement 
 * to provide quick access to all configurable constants defining the behavior of the ant colony.
 * 
 */

public interface AntColonyConstants {
	
	// Global Colony Constants
	public static final int COLONY_GRID_SIZE=27;   
	public static final int COLONY_CENTER_COL=13;   
	public static final int COLONY_CENTER_ROW=13;   
	public static final int COLONY_INITIAL_SOLDIER_ANTS=10;
	public static final int COLONY_INITIAL_FORAGER_ANTS=50;
	public static final int COLONY_INITIAL_SCOUT_ANTS=4;
	public static final int COLONY_INITIAL_FOOD_UNITS=1000;
	public static final int COLONY_TURNS_PER_DAY=10;
	public static final double COLONY_BALA_BIRTHFREQ=0.03;	
	public static final double COLONY_PHEROMONE_DECAYRATIO=0.50;
	public static final double COLONY_FOODPRESENT_PROBABILITY=0.25;
		
	public static final int COLONY_STOP_MODE=0;
	public static final int COLONY_STEP_MODE=1;
	public static final int COLONY_RUN_MODE=2;
	public static final int COLONY_QUEEN_DEAD=3;
	public static final int COLONY_DEFAULT_RUN_DELAY=1000;
	
	// Bala Ant Constants
	public static final int BALA_LIFESPAN=1*365*COLONY_TURNS_PER_DAY;
	public static final double BALA_ENEMY_KILL_RATIO=0.50;	
	public static final int BALA_ANT_TYPE=5;
	
	// Forager Ant Constants
	public static final int FORAGER_LIFESPAN=1*365*COLONY_TURNS_PER_DAY;
	public static final int FORAGER_ANT_TYPE=2;
	
	// Queen Ant Constants
	public static final int QUEEN_LIFESPAN=20*365*COLONY_TURNS_PER_DAY;
	public static final int QUEEN_BIRTHRATE=10;
	public static final double QUEEN_BIRTHFREQ_FORAGER=0.50;
	public static final double QUEEN_BIRTHFREQ_SCOUT=0.25;
	public static final double QUEEN_BIRTHFREQ_SOLDIER=0.25;
	public static final int QUEEN_FOODPERTURN=1;	
	public static final int QUEEN_ANT_TYPE=1;
	
	// Scout Ant Constants
	public static final int SCOUT_LIFESPAN=1*365*COLONY_TURNS_PER_DAY;
	public static final int SCOUT_ANT_TYPE=3;
	
	// Soldier Any Constants
	public static final int SOLDIER_LIFESPAN=1*365*COLONY_TURNS_PER_DAY;
	public static final double SOLDIER_ENEMY_KILL_RATIO=0.50;	
	public static final int SOLDIER_ANT_TYPE=4;
}
