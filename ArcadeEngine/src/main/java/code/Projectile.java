package code;


import java.awt.Rectangle;
import java.util.Random;

/*
 * Code for a generic Projectile that can move across the screen
 * Spock 2006ish?
 */

public class Projectile
{
    //Constants
    //-------------------------------------------------------
    public final int HEIGHT = 5;
    public final int WIDTH = 5;

    //Instance Variables
    //-------------------------------------------------------
    private int x;  //The horizontal X position of the (left side of the) projectile 
    private int y;  //The vertical Y position of the (top of the) projectile  
    private int xVel;   //The horizontal velocity of the projectile.        
    private int yVel;   //The vertical velocity of the projectile.
    private boolean frozen; //Is this projectile frozen (can't move?)
    private Random randy;
    
    //Constructor
    //-------------------------------------------------------
    public Projectile()  
    {
        randy = new Random();
        frozen = true;  //Start NOT in motion (frozen).
    }
    
    //Accessors
    //-------------------------------------------------------
    public int getX() {return x;}
    public int getY() {return y;}
    public boolean isFrozen() {return frozen;}
    
    //Modifiers
    //-------------------------------------------------------
    public void setX(int in) {x=in;}
    public void setY(int in) {y=in;}
    public void setXVel(int in) {xVel=in;}
    public void setYVel(int in) {yVel=in;}
    public void setFrozen(boolean in) {frozen=in;}   
    
    public Rectangle getRect() { return new Rectangle(x,y,WIDTH,HEIGHT); }
    
    public void animate()
    {
        if (!frozen)    //Move according to the velocities.
        {
            x+=xVel;
            if (x > 640-WIDTH || x < 0) 
                frozen=true;    //If this goes off the screen, 'freeze' it.
            y+=yVel;
            if (y > 480-HEIGHT || y < 0) 
                frozen=true;    //If this goes off the screen, 'freeze' it.
        }
    }
    
       
    public void fireWeapon(int startX, int startY)
    {
        //Initialize this projectile based on starting location to move upwards.
        x=startX;
        y=startY;
        xVel=randy.nextInt(9)-3;
        yVel=randy.nextInt(9)-3;
        frozen=false;
    }
    
}  //end of Projectile class

