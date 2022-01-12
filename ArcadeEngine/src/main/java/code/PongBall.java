package code;


import java.awt.Rectangle;


/*
 * Code for a generic PongBall that bounces around the screen.
 * Spock - 2006ish?
 */

public class PongBall
{
    //Constants
    //-------------------------------------------------------
    public final double BALL_HEIGHT = 40;
    public final double BALL_WIDTH = 40;

    //Instance Variables
    //-------------------------------------------------------
    private double x;  //The horizontal X position of the (left side of the) ball  
    private double y;  //The vertical Y position of the (top of the) ball  
    private double xVel;   //The horizontal velocity of the ball.        
    private double yVel;   //The vertical velocity of the ball.
    private boolean frozen; //Is this ball frozen (can't move?)
    
    private static java.util.Random randy = new java.util.Random();
    
    //Constructor
    //-------------------------------------------------------
    public PongBall()  
    {
        //Initialize the instance variables...        
        x = randy.nextInt(300);     //Start near the middle of the width.
        y = randy.nextInt(200);     //Start near the middle.
        xVel = (randy.nextInt(9)-4)*1.0;  //Choose a random starting velocity
        yVel = (randy.nextInt(9)-4)*1.0;
        frozen = false;             //Start in motion.
    }
    
    //Accessors
    //-------------------------------------------------------
    public double getX() {return x;}
    public double getY() {return y;}
    public boolean isFrozen() {return frozen;}
    //public Rectangle getRect() { return new Rectangle((int)x,(int)y,BALL_WIDTH,BALL_HEIGHT); }
    
    //Modifiers
    //-------------------------------------------------------
    public void setX(double in) {x=in;}
    public void setY(double in) {y=in;}
    public void setFrozen(boolean in) {frozen=in;}
    public void decreaseVel(double in) {xVel*=in; yVel*=in;} 
    public void increaseVel(double in) {xVel*=in; yVel*=in;}
    
    
    /*
     * This method changes the location of the ball upwards by one height
     * It also limits it's motion to stay inside the frame.
     */
    public void moveUp()
    { 
        if (!frozen) 
            y=y-BALL_HEIGHT;    //Move it up one height
        if (y<0)  
            y=0;                //Don't let it go off the frame.
    }
    
    /*
     * This method changes the location of the ball down by one height
     * It also limits it's motion to stay inside the frame.
     */
    public void moveDown()
    {
        if (!frozen) 
            y=y+BALL_HEIGHT;    //Move it down one height
        if (y>480-BALL_HEIGHT)
            y=480-BALL_HEIGHT;  //Don't let it go off the frame.
    }
    
    /*
     * This method changes the location of the ball right by one width
     * It also limits it's motion to stay inside the frame.
     */
    public void moveRight()
    {
        if (!frozen) 
            x=x+BALL_WIDTH;      //Move right one width
        if (x > 640-BALL_WIDTH) 
            x=640-BALL_WIDTH;    //Don't let it go off the frame.
    }
    
    /*
     * This method changes the location of the ball left by one width
     * It also limits it's motion to stay inside the frame.
     */
    public void moveLeft()
    {
        if (!frozen) 
            x=x-BALL_WIDTH;      //Move left one width
        if (x < 0) 
            x=0;                //Don't let it go off the frame.
    }
    
    /*
     * This method changes the location of the ball based on it's velocity.
     * It also limits it's motion to stay inside the frame by changing the velocity if a wall is hit.
     * (It bounces off walls.)
     */
    public void animate()
    {
        if (!frozen)    //Move according to the velocities.
        {
            x+=xVel;
            if (x > 640-BALL_WIDTH || x < 0)
                xVel=-xVel; //change directions at the walls
            y+=yVel;
            if (y > 480-BALL_HEIGHT || y < 0) 
                yVel=-yVel; //change directions at the walls
        }
    }
        
}  //end of PongBall class

