import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import acm.gui.*;

/**
 * TowerOfHanoi.java <p>
 * 
 * The Tower of Hanoi game.  The aim of game is to move the disks from the source rod to the destiny rod by the order in a top with
 *                           smallest disk and bottom with biggest size. To expanding the program, first, add four buttons in the main class
 *                           located in the North panel. The usage of if statement in the actionPerformed method helps to illustrate what
 *                           happen when clicking on different button.The doneSolving shows what happen when ending the solving.
 *                           In the solver class, by using the recursion, call moveTower method twice within the moveTower method but with 
 *                           different parameters, and call the moveDisk which located in the main class for finishing the whole steps in 
 *                           the moveTower method. Also, add three methods: fasterDisk, slowerDisk, stopDisk which are called in the main
 *                           class under the method of actionPerformed.    <p>
 * 
 * 
 *         Ran Cao
 */
public class TowerOfHanoi extends GraphicsProgram {
  // initial size of the window
  public static int 
  APPLICATION_WIDTH = 600,
  APPLICATION_HEIGHT = 400;

  // constants
  private static final double
  DISK_H = 12, // height of disks
  DISK_W = 30, // width of the first disk
  GAP = 2, // gap between disks
  DISK_W_DIFF = 16; // diff in width between two consecutive disks

  // total number of fisks
  private static final int N_DISK = 8;

  // instance variables 
  private double width, height;
  private double groundY; // y-coordinate of the ground
  private GLabel label; // label of the rods
  private int count = 0; // move count
  private GLabel countLabel; // display of move count

  private GLine[] rod = new GLine[3]; // three rods
  private Disk[][] disk = new Disk[3][N_DISK]; // an array of disk for each rod
  private int[] numDisk = new int[3]; // number of disks on each rod

  private GPoint lastPoint; // last mouse point when dragging
  private Disk draggedDisk; // dragged disk
  private GPoint lastLocation; // original location of the dragged disk

  private int fromRod; // orignal rod of the dragged disk
  private boolean isDragging = false; // indicator of dragging
  
  private boolean isSolving = false; // solver is working
  
  private Solver solver; // solver class
  private JButton solveButton;  // the 1st button - Solve
  private JButton fasterButton;  // the 2nd button - Faster
  private JButton slowerButton;  // the 3rd button - Slower
  private JButton stopButton;  // the 4th button - Stop
  
  
  // handle button clicks
  public void actionPerformed (ActionEvent evt) {
    if (evt.getSource() == solveButton) { // when click the solveButton
      isSolving = true; // start solving
      initializeGame(); // after clicking stop and then solve button, initialize the game to its initial status
      solveButton.setEnabled(false); // during solving, change the button color to be gray, cannot click again
      solver = new Solver(N_DISK, 0, 1, 2, this); // call the solver class, with int N_DISK = 8, 0 = source, 1 = helper, 2 = destiny
      new Thread(solver).start(); // add to the class
    } else if (evt.getSource() == fasterButton) { // when click the fasterButton
      solver.fasterDisk();  // call fasterdisk method in the solver class    
    } else if(evt.getSource() == slowerButton){ // when click the slowerButton
      solver.slowerDisk(); // call slowerdisk method in the solver class
    }  else if(evt.getSource() == stopButton){ // when click the stopButton
      solver.stopDisk(); // call stopdisk method in the solver class
    }
  }
  
  // solver is done
  public void doneSolving() {
    isSolving = false; // when done with solving, change the boolean to false
    solveButton.setEnabled(true); // when done with solving process, show the solve button 
  }
  
  // draw the GUI components
  private void drawGUI() {
    solveButton = new JButton("Solve");
    solveButton.addActionListener(this);
    add(solveButton, "North");
    
    fasterButton = new JButton("Faster");
    fasterButton.addActionListener(this);
    add(fasterButton, "North");
    
    slowerButton = new JButton("Slower");
    slowerButton.addActionListener(this);
    add(slowerButton, "North");
    
    stopButton = new JButton("Stop");
    stopButton.addActionListener(this);
    add(stopButton, "North");
  }

  
  /***********************************************************/
  /**     DO NOT CHANGE THE FOLLOWING METHODS                */
  /***********************************************************/

  // the init method, draw the inital graphics 
  public void init() {
    drawGUI();
    drawGraphics();
  }

  /* if pressed on a disk on the top of the stack on a rod, start dragging it */
  // DO NOT CHANGE THIS CLASS
  public void mousePressed(GPoint point) {
    if (isSolving) return;
    for (int i = 0; i < 3; i++) { // check all three stacks of disks on three rods
      // if the disk on the top of a stack is pressed on, start dragging it
      if (numDisk[i] > 0 && disk[i][numDisk[i]-1].contains(point)) {
        draggedDisk = disk[i][numDisk[i]-1];
        lastLocation = draggedDisk.getLocation();
        fromRod = i;
        isDragging = true;
      }
    }
    lastPoint = point; // record last mouse point
  }

  // if is dragging a disk, move the disk along with mouse
  public void mouseDragged(GPoint point) {
    if (!isDragging) return;
    draggedDisk.move(point.getX() - lastPoint.getX(), point.getY() - lastPoint.getY());
    lastPoint = point;
  }
  
  // if the dragged disk is dropped on a rod, move it to the top of the stack on that rod if the move is legal
  public void mouseReleased(GPoint point) {
    if (!isDragging) return;
    isDragging = false; // stop dragging
    int toRod = fromRod; // the destiny rod, by default it is the same as the orignal rod of the dragged disk
    for (int i = 0; i < 3; i++) {
      // check if the draggeddisk intersects a rod and if it is legal to move the draggedDisk to this rod
      if (draggedDisk.getBounds().intersects(rod[i].getBounds()) && 
          !(numDisk[i] > 0 && disk[i][numDisk[i]-1].getWidth() < draggedDisk.getWidth())) {
        toRod = i;
      }
    }
    draggedDisk.setLocation(lastLocation); // first return draggedDisk to its original location
    if (fromRod != toRod) moveDisk(fromRod, toRod); // if needs to move the draggedDisk, call a helper method
  }  
  
  // move a disk from top of rod x to top of rod y, assuming it is legal
  public void moveDisk(int x, int y) {
    Disk movedDisk = disk[x][numDisk[x]-1]; // disk to be moved
    if (numDisk[y] > 0) { // already have a disk on rod y
      movedDisk.setLocation(disk[y][numDisk[y]-1].getX(), disk[y][numDisk[y]-1].getY()-DISK_H-GAP);
    } else { // no disk on rod y
      movedDisk.setLocation(rod[y].getEndPoint().getX(), groundY-DISK_H/2-GAP);
    }
    disk[y][numDisk[y]] = movedDisk; // add movedDisk to array for the new rod
    // update the counters of both rods
    numDisk[y]++; 
    numDisk[x]--;
    // update the move count and the display     
    count++;
    updateDisplay();
  }  
  
  // initially draw the background, stars and a label
  private void drawGraphics() {
    width = getWidth(); // width of the window
    height = getHeight(); // height of the window

    groundY = height*3/4; // y-coordinate of ground
    initializeGame(); // initlaize the game
  }

  // initlaize the game
  private void initializeGame() {
    removeAll();

    // draw ground 
    GRect ground = new GRect(0, groundY, width, height-groundY);
    ground.setFilled(true);
    ground.setColor(Color.BLACK);
    add(ground);

    // draw three rods
    for (int i = 0; i < 3; i++) {
      double x = width/5 + i*width*3/10;
      rod[i] = new GLine(x, groundY/2, x, groundY);
      add(rod[i]);
    }

    // draw all disks on rod 0
    for (int i = 0; i < N_DISK; i++) {
      disk[0][i] = new Disk(DISK_W+(N_DISK-1-i)*DISK_W_DIFF, DISK_H);
      add(disk[0][i], width/5, groundY-DISK_H/2-GAP-(DISK_H+GAP)*i);
    }

    // initial number of disks on each rod
    numDisk[0] = N_DISK;
    numDisk[1] = 0;
    numDisk[2] = 0;

    // draw the label for each rod
    label = new GLabel("0                            1                            2");
    label.setFont(new Font("Sanserif", Font.BOLD, 20));
    label.setColor(Color.BLUE);
    add(label, width/2, 100);
    label.move(-label.getWidth()/2, label.getHeight());
    
    // draw the label for move count
    count = 0;
    countLabel = new GLabel("Number of moves: " + count);
    countLabel.setFont(new Font("Sanserif", Font.BOLD, 16));
    countLabel.setColor(Color.BLUE);
    add(countLabel, 40, 40);
  }
  
  // update the display of move count
  private void updateDisplay() {
    countLabel.setLabel("Number of moves: " + count);
  }
}