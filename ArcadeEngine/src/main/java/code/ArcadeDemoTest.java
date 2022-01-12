package code;

/**
 * Class ArcadeDemo
 * This class contains demos of many of the things you might
 * want to use to make an animated arcade game.
 * 
 * Adapted from the AppletAE demo from years past. 
 */
/*
Challenges Completed
4,21,19,18,22,23
*/


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import java.util.Random;
import java.awt.Rectangle;

public class ArcadeDemoTest extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------

    public int width;
    public int height;
    
    //Instance Variables
    //-------------------------------------------------------

    public boolean facingRight;
    public boolean running;
    
    public double myX;
    public double myY;
    public double myXVel;
    public double myYVel;    
    
    public int red;
    public int green;
    public int blue;
    
    public Random randy = new Random();
    
    public ArrayList<Color> bgs = new ArrayList<>();
    public ArrayList<Rectangle> cloudArray;
    //Constructor
    //-------------------------------------------------------
    public ArcadeDemoTest()
    {   //Enter the name and width and height.          
        super("ArcadeDemo", 1366, 720);
        width = 1366;
        height = 720;
        
        myX = 650;
        
        red = 175;
        green = 220;
        blue = 252;
        
        facingRight = true;
        running = false;
        
        cloudArray = new ArrayList<>();
    }
       
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected void renderFrame(Graphics g) 
    {
        int screenNumber = Math.floorDiv((int)myX, 1366);
                
        int count = 0;
        
        myX += myXVel;
        if(myXVel>0)
            myXVel -= 0.1;
        if(myXVel<0)
            myXVel += 0.1;
        
        if(myXVel>-0.1 && myXVel<0.1)
            myXVel = 0;
        
        myY += myYVel;
        
        if(myXVel == 0)
            running = false;
        
        
        
        g.setColor(new Color(red,green,blue));
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.pink);
        g.fillOval(2000-(int)myX,150 , 500, 50);
        
        
        if(frameNumber == 1)
        {
            for(int i = 0; i<200; i++)
            {
                int xLoc = randy.nextInt(50000)-25000;
                int yLoc = randy.nextInt(400);
                cloudArray.add(new Rectangle(xLoc, yLoc, 100, 20));
                //g.fillRect(xLoc-(int)myX, yLoc, 100, 20);
            }
        }
        
        for(Rectangle cloud : cloudArray)
        {
            g.setColor(Color.white);
            g.fillRect((int)cloud.getX()-(int)myX, (int)cloud.getY(), (int)cloud.getWidth(), (int)cloud.getHeight());
        }
        
        g.setColor(Color.white);    
        g.fillRect(100,100, 40, 40);
        
        g.setColor(new Color(50,180,50));
        g.fillRect(0, 600, width, height);
        
        if(running == false && facingRight == true)
            playerImage = standing;
        else if(running == false && facingRight == false)
            playerImage = standingBack;
        else if(running == true && facingRight == true)
            playerImage = runningForward;
        else
            playerImage = runningBack;
        
        
        if(!running)
             g.drawImage(playerImage, (int)650, 504,36,96, this);
        else
             g.drawImage(playerImage, (int)650, 504,44,96, this);
        
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        g.drawString("X: "+String.valueOf((int)myX), 1070, 24);
        //g.drawString("SN: "+String.valueOf((int)screenNumber), 1070, 48);


    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    { 

    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {

    }
    
    public void keyPressed(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_W)
        {
            
        }
        if(v == KeyEvent.VK_A)
        {
            myXVel = -5;
            facingRight = false;
            running = true;
        }
        if(v == KeyEvent.VK_S)
        {
            running = false;
        }
        if(v == KeyEvent.VK_D)
        {
            myXVel = 5;
            facingRight = true;
            running = true;
        }
        
    }

    public void keyReleased(KeyEvent e)
    {

    }
    
    
    //-------------------------------------------------------
    //Initialize Graphics
    //-------------------------------------------------------
//-----------------------------------------------------------------------
/*  Image section... 
 *  To add a new image to the program, do three things.
 *  1.  Make a declaration of the Image by name ...  Image imagename;
 *  2.  Actually make the image and store it in the same directory as the code.
 *  3.  Add a line into the initGraphics() function to load the file. 
//-----------------------------------------------------------------------*/

    Image standing;
    Image standingBack;
    Image runningForward;
    Image runningBack;
    Image playerImage;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        standing = toolkit.getImage("src/images/Standing No Bg.png");
        standingBack = toolkit.getImage("src/images/backNoBg.png");
        runningForward = toolkit.getImage("src/images/rNoBg.png");
        runningBack = toolkit.getImage("src/images/backRNoBg.png");
        

    } //--end of initGraphics()--
    
    //-------------------------------------------------------
    //Initialize Sounds
    //-------------------------------------------------------
//-----------------------------------------------------------------------
/*  Music section... 
 *  To add music clips to the program, do four things.
 *  1.  Make a declaration of the AudioClip by name ...  AudioClip clipname;
 *  2.  Actually make/get the .wav file and store it in the same directory as the code.
 *  3.  Add a line into the initMusic() function to load the clip. 
 *  4.  Use the play(), stop() and loop() functions as needed in your code.
//-----------------------------------------------------------------------*/
    
    public void initMusic() 
    {        

    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--

