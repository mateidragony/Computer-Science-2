package code;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/*
 * Code for a ball that bounces with gravity around the screen.
 * This is very similar to the PongBall class, but has a few 'improvements'.  
 * Spock - 2016
 */

public class BouncyBall
{
    //Constants
    //-------------------------------------------------------

    //Instance Variables
    //-------------------------------------------------------
    private double x;  //The horizontal X position of the (left side of the) ball  
    private double y;  //The vertical Y position of the (top of the) ball  
    private double xVel;   //The horizontal velocity of the ball.        
    private double yVel;   //The vertical velocity of the ball.
    private int radius;  //The radius of the ball.  
    private Color color;
        
    //Constructor
    //-------------------------------------------------------
    public BouncyBall()  
    {
        //Initialize the instance variables...  
        Random randy = new Random();
        x = randy.nextInt(300);     //Start near the middle of the width.
        y = randy.nextInt(200);     //Start near the middle.
        xVel = randy.nextInt(9)-4;  //Choose a random starting velocity
        yVel = 0;
        radius = randy.nextInt(4)*10+20; //could be 20,30,40,50
    }
    
    //Accessors
    //-------------------------------------------------------
    public double getX() {return x;}
    public double getY() {return y;}
    public Point getCenter() { return new Point((int)(x+radius/2), (int)(y+radius/2)); }
    public int getSize() {return radius; }
    public Rectangle getBounds() { return new Rectangle((int)x,(int)y,radius,radius); }
    public Color getColor() {return color;}
    
    //Modifiers
    //-------------------------------------------------------
    public void setX(int in) {x=in;}
    public void setY(int in) {y=in;}
    public void setColor(Color c) {color = c;}
    
    /**
     * This method determines whether this BouncyBall intersects another.  
     * @param b the other BouncyBall that might intersect with this one.
     * @return true if they intersect, false otherwise.  
     */
    public boolean intersects(BouncyBall b)
    {
        if(getCenter().distance(b.getCenter()) < radius + b.getSize())
            return true; 
        return false;
    }

    /**
     * This method updates the location of the ball based on it's velocity.
     * It also limits it's motion to stay inside the frame by changing the 
     * velocity if a wall is hit.  (It bounces off walls.)
     * @param screenBounds this is the size of the window, which is needed 
     * to calculate when the walls to bounce of are.  
     */
    public void animate(Rectangle screenBounds)
    {
            x+=xVel;
            if (x > screenBounds.getMaxX()-radius || x < screenBounds.getMinX()) 
            { //change directions at the walls
                xVel=-xVel;
            } 
            
            yVel++; //change in velocity is acceleration due to gravity!
            y+=yVel;
            if (y > screenBounds.getMaxY()-radius || y < screenBounds.getMinY()) 
            { 
                //This fixes the error that can occur if the ball flies 'through' the ground.  
                if(y > screenBounds.getMaxY()-radius) 
                    y = screenBounds.getMaxY()-radius; 
                yVel=-yVel; //change directions at the walls
            } 
    }
    
    public void animateFlipped(Rectangle screenBounds)
    {
        x+=xVel;
            if (x > screenBounds.getMaxX()-radius || x < screenBounds.getMinX()) 
            { //change directions at the walls
                xVel=-xVel;
            } 
            
            yVel--; //change in velocity is acceleration due to gravity!
            y+=yVel;
            if (y > screenBounds.getMaxY()-radius || y < screenBounds.getMinY()) 
            { 
                //This fixes the error that can occur if the ball flies 'through' the ground.  
                if(y > screenBounds.getMaxY()-radius) 
                    y = screenBounds.getMaxY()-radius; 
                yVel=-yVel; //change directions at the walls
            } 
    }
    
    /**
     * Draw the object to the screen.  This allows the class to be in charge of 
     * drawing itself, rather than having the renderFrame() method do it.  
     * @param g the Graphics object to be drawn on.  
     */
    public void draw(Graphics g)
    {
        g.setColor(color);
        g.drawOval((int)x, (int)y, radius, radius);
    }

        
}  