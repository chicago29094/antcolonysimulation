import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import dataStructures.LinkedList;
import dataStructures.Iterator;

/**
 * AntColonySimulation
 * 
 * The Any Colony Simulation simulates the life cycle of an ant colony according to specific ant colony rules and behaviors.
 * The AntColonySimulation Class is the main class utilzied to begin the operation of the GUI-based simulation.
 * 
 * Class:	CSC385, Section 11792
 * Lesson:	Semester Project - Ant Colony Simulation
 * 
 * @author Harry Anastopoulos
 */
public class AntColonySimulation implements AntColonyConstants
{
   // Main static method
   public static void main(String[] args)
   {
      AntSimGUI simulationGUI=new AntSimGUI();
      ColonyView simulationColonyView=new ColonyView(COLONY_GRID_SIZE, COLONY_GRID_SIZE);
      ColonyNodeView[][] colonyNodeViewGrid=new ColonyNodeView[COLONY_GRID_SIZE][COLONY_GRID_SIZE];
      
      simulationGUI.initGUI(simulationColonyView);

      // Instantiate a new AntColont ant colony
      AntColony phaseivColony=new AntColony(simulationGUI, simulationColonyView, colonyNodeViewGrid, COLONY_GRID_SIZE);
      
   };
}