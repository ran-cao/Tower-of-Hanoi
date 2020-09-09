/***********************************************************/
/**       DO NOT CHANGE THIS CLASS                         */
/***********************************************************/


import acm.program.*;
import acm.graphics.*;
import java.awt.*;

/**
 * Disk.java <p>
 * 
 * A class for Disk that used in TowerOfHanoi. <p>
 * 
 * Ran Cao
 */
public class Disk extends GCompound {
  // instance variables
  private double width, height;
  
  /** the constructor, create the disk */
  public Disk(double width, double height) {
    // save the parameters in instance variables
    this.width = width;
    this.height = height;
    
    // create a disk
    GRect disk = new GRect(-width/2+height/2, -height/2, width-height, height);
    disk.setFilled(true);
    disk.setColor(Color.red);
    add(disk); 
    
    // create left round end
    GOval left = new GOval(-width/2, -height/2, height, height);
    left.setFilled(true);
    left.setColor(Color.red);
    add(left);
    
    // create right round end
    GOval right = new GOval(width/2-height, -height/2, height, height);
    right.setFilled(true);
    right.setColor(Color.red);
    add(right);
  }
}