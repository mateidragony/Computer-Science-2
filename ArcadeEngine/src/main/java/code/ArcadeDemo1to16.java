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
1,2,3,4,5,6,7,8,9,10,11,12,13,18,22
*/


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.sound.sampled.Clip;


public class ArcadeDemo1to16 extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------

    //Instance Variables
    //-------------------------------------------------------
    BouncyBall tempBouncer;
    PongBall ball = new PongBall();
    BouncyBall bouncer = new BouncyBall();
    BouncyBall bouncer2 = new BouncyBall();
    ArrayList<Projectile> laserArray = new ArrayList<Projectile>();
    ArrayList<BouncyBall> bouncerArray = new ArrayList<>();
    boolean zPressed;
    int projLaunched;
    int timesHit;
    
    //Constructor
    //-------------------------------------------------------
    public ArcadeDemo1to16()
    {   //Enter the name and width and height.  
        super("ArcadeDemo", 640, 480);
        zPressed = false;
        projLaunched = 0;
    }
       
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected void renderFrame(Graphics g) 
    {
        //Challenge 8
        //g.drawImage(fatherBackground, 0, 0, this);
        
        //Draw a square that is stationary on the screen.
        //challenge 14 and challenge 16
        g.drawImage(ballImage, 10,20, this);
        
        if(timesHit>=255)
            timesHit = 255;
        Color boxColor = new Color(0+timesHit,0,255-timesHit);
        if(frameNumber+200 <= 480)
        {
            g.setColor(boxColor);
            g.fillRect(220,120,50+frameNumber,50+frameNumber);
        }
        else
        {
            g.setColor(boxColor);
            g.fillRect(220,120,330,330);
        }
        
        //challenge 11
        if(frameNumber % 50 == 0)
        {
            tempBouncer = new BouncyBall();
            bouncerArray.add(tempBouncer);
        }
            
        //Draw a circle that follows the mouse.
        g.setColor(Color.BLACK);
        g.fillOval(mouseX-10, mouseY-10, 20,20);
        
        //Draw a ball that bounces around the screen.
        g.drawImage(ballImage,(int)ball.getX(),(int)ball.getY(),this);
        ball.animate();
        
        
        
        //challenge 15
        if(frameNumber % 10 == 0)
        {
            projLaunched ++;
            Projectile tempstar = new Projectile();
            tempstar.fireWeapon((int)ball.getX(),(int)ball.getY());
            tempstar.setXVel((int)(mouseX-ball.getX())/50);
            tempstar.setYVel((int)(mouseY-ball.getY())/38);
            laserArray.add(tempstar);  
        }
        
        
        
        // challenge 21 
        if (ball.getX() >= 598 || ball.getX() <= 2)
            bellSound.loop(1); //change directions at the walls
        if (ball.getY() >= 438 || ball.getY() <= 2) 
            bellSound.loop(1); //change directions at the walls
        
        //challenge 7
        if(mouseX >= ball.getX() && mouseX <= ball.getX()+45
                && mouseY >= ball.getY() && mouseY <= ball.getY()+45)
            g.drawString("COLLISION!!!!", 280, 215);
        
        
        //Draw a ball with gravity! 
        bouncer.animate(g.getClipBounds());
        bouncer.setColor(Color.RED);
        bouncer.draw(g);
            
        //challenge 9
        bouncer2.animate(g.getClipBounds());
        bouncer2.setColor(Color.BLUE);
        bouncer2.draw(g);
        
        
        //challenge 5
        for(int i=0; i<laserArray.size(); i++)
        {
            Projectile p = laserArray.get(i);
            
            if(p.getY() <= 0 || p.getY() >= 480)
                laserArray.remove(p);
            if(p.getX() <= 0 || p.getX() >= 640)
                laserArray.remove(p);
            
            if(p.getY()>=120 && p.getY() <= 120+50+frameNumber &&
                    p.getX() >= 220 && p.getX() <= 220+50+frameNumber)
            {
                laserArray.remove(p);
                timesHit++;
            }
           
        }
               
        
        
        //Draw any 'lasers' that have been fired (spacebar).
        for(Projectile p : laserArray)
        {
            //Drawing element z from the array
            //challenge 2
            g.setColor(Color.GREEN);
            g.fillRect(p.getX(), p.getY(), 5,15);            
            p.animate();  
        }
        
        //Challenge 11
        for(BouncyBall b : bouncerArray)
        {
            //Drawing element z from the array
            b.animate(g.getClipBounds());
            b.setColor(Color.MAGENTA);
            b.draw(g);
        }
        
        
        //General Text (Draw this last to make sure it's on top.)
        g.setColor(Color.BLACK);
        g.drawString("ArcadeEngine 2021", 10, 12);
        g.drawString("#"+frameNumber,200,12);
        if(zPressed) g.drawString("Hooray!", 400, 400);
        
        //challenge 6
        if(frameNumber > 200)
            g.drawString("Ma'deo", 420, 12);
        
        //challenge 12
        g.drawString("Projectiles Launched: "+String.valueOf(projLaunched), 260, 12);
        
    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    { 
        ball.setFrozen(!ball.isFrozen()); //Toggle the ball's frozen status.
        //challenge 18
        laserArray.clear();
    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar();
        //Use the IJKM keys to move the PongBall
        if(c=='i' || c=='I') 
            ball.moveUp();
        if(c=='j' || c=='J') 
            ball.moveLeft();
        if(c=='k' || c=='K') 
            ball.moveRight();
        if(c=='m' || c=='M') 
            ball.moveDown();
        
        //challenge 3
        if(c=='q' || c=='Q')
        {
            ball.setX(0);
            ball.setY(0);
        }
        
        //challenge 4
        if(c=='+')
            ball.increaseVel(1);
        if(c=='-')
            ball.decreaseVel(1);
        
        //challenge 13
        if(c=='x' || c=='X')
        {
            if(ball.getX() < mouseX)
                ball.moveRight();
            else
                ball.moveLeft();
            
            if(ball.getY() < mouseY)
                ball.moveDown();
            else
                ball.moveUp();
        }
        
        
        if(c==' ') //Fire a projectile when spacebar pressed.  
        {
            //challenge 10
            projLaunched ++;
            Projectile tempstar = new Projectile();
            tempstar.fireWeapon(mouseX,mouseY);           
            laserArray.add(tempstar);            
        }
    }
    
    public void keyPressed(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_Z)
            zPressed = true;
    }

    public void keyReleased(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_Z)
            zPressed = false;
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
    Image ballImage;        
    Image starImage;
    Image fatherImage;
    Image fatherBackground;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        fatherBackground = toolkit.getImage("src/images/fatherBackground.jpg");
        fatherImage = toolkit.getImage("src/images/father.JPG");
        ballImage = toolkit.getImage("src/images/ball.gif");
        starImage = toolkit.getImage("src/images/star.gif");
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
    Clip themeMusic;
    Clip bellSound;
    Clip underSong;
    
    public void initMusic() 
    {        
        underSong = loadClip("src/audio/under.wav");
        //challenge 22
        themeMusic = loadClip("src/audio/Earrape.wav");
        bellSound = loadClip("src/audio/Skype.wav");
        //if(themeMusic != null)
//            themeMusic.start(); //This would make it play once!
           //themeMusic.loop(2); //This would make it loop 2 times.
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--

