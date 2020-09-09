import acm.program.*;
import acm.graphics.*;
import java.awt.*;

/**
 * Solver.java <p>
 * 
 * A class for solvering TowerOfHanoi. <p>
 * 
 * Ran Cao
 */
public class Solver extends GCompound implements Runnable {

  // instance variable
  private TowerOfHanoi game; // main game
  private int nDisks; // number of disks to be moved by solver
  private int source, helper, destiny; // index of source rod, heler rod and destiny rod
  private int delay = 200; // the amount of delay after a disk is moved
  private boolean isStop = false; // initially not to stop the solving
   
  
  
  // move n disks from rod source to rod destiny, using rod helper as helper  
  private void moveTower(int n, int source, int helper, int destiny)
  {
    if (isStop)  return; //if the solver is called to stop, stop further movetower    
    if (n>0){ // if there is any disks on the rod, start the movement   
    // step one:move the first n-1 disks from the source rod to the helper rod, using the destiny rod as the helper     
     moveTower(n - 1, source, destiny, helper);
    // step two: move the n-th disk (the largest one) from the source rod to the destiny rod
    if(!isStop){ // Only under the not stopping condition, disks can be moved. prevent when stopping, the larger one pile on smaller one
      game.moveDisk(source, destiny);  // call moveDisk in the main class
      pause(delay); // use pause between each movement to set a speed of moving disk
    }
    // step three: move the first n-1 disks from the helper rod to the destiny rod, using the source rod as the helper. 
     moveTower(n - 1, helper, source, destiny);  
    }
  }
  
  /***********************************************************/
  /**     DO NOT CHANGE THE FOLLOWING METHODS                */
  /***********************************************************/
  
  // the constructor, create the solver
  public Solver(int nDisks, int source, int helper, int destiny, TowerOfHanoi game) {
    // save the parameters to instance variables
    this.game = game;
    this.source = source;
    this.helper = helper;
    this.destiny = destiny;
    this.nDisks = nDisks;
  }
  
  // the run method solves the puzzle
  public void run() {
    moveTower(nDisks, source, helper, destiny); // move n disks from source rod to destiny rod, using helper rod
    game.doneSolving(); // tell game that solver has done solving
  }
  
  // for fasting the speed of moving, decrease the delay time in between
  public void fasterDisk(){
      // to set a minium delay time
      if(delay>40){ //when there is no if statement to restrict the condition, clicking button very often can donesolving very quick
      delay = delay - 40;
    }
    }
    
  // for slowing the speed of moving, increase the delay time in between
  public void slowerDisk(){
      // to set a maximum delay time
      if(delay<800){ // when the delay excesses 800, it will not further add
      delay = delay + 40;
     }
    }
    
  // to stop solving the disk in the main class
  public void stopDisk(){
      isStop = true; // set the boolean isStop to be true, in this way, cannot go further in the moveTower method
    }
}