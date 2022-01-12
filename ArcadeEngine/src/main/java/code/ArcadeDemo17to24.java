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
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.sound.sampled.Clip;


public class ArcadeDemo17to24 extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------

    //Instance Variables
    //-------------------------------------------------------
    MovingWord challenge24 = new MovingWord();
    PongBall ball = new PongBall();
    BouncyBall bouncer = new BouncyBall();
    BouncyBall bouncer2 = new BouncyBall();
    ArrayList<Projectile> laserArray = new ArrayList<Projectile>();
    ArrayList<BouncyBall> bouncerArray = new ArrayList<>();
    ArrayList<Projectile> bunnyArray = new ArrayList<>();
    boolean zPressed;
    int projLaunched;
    int timesHit;
    boolean clickedInBox;
    
    //Constructor
    //-------------------------------------------------------
    public ArcadeDemo17to24()
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
        g.setColor(Color.BLUE);
        g.fillRect(220,120,50,50);
        
            
        //Draw a circle that follows the mouse.
        int circleX = mouseX-10;
        int circleY = mouseY-10;
        
        g.setColor(Color.BLACK);
        
        if(mouseX>=420)
            circleX = 400;
        else if(mouseX <= 220)
            circleX = 220;
        
        if(mouseY>=340)
            circleY = 320;
        else if(mouseY <= 140)
            circleY = 140;
        
        g.fillOval(circleX, circleY, 20,20);
        
        
        
        //Draw a ball that bounces around the screen.
        g.drawImage(ballImage,(int)ball.getX(),(int)ball.getY(),this);
        ball.animate();
        
        
        
        
        
        
        // challenge 21 
        if (ball.getX() >= 598 || ball.getX() <= 2)
            bellSound.loop(1); //change directions at the walls
        if (ball.getY() >= 438 || ball.getY() <= 2) 
            bellSound.loop(1); //change directions at the walls
        
        //challenge 23
        if(clickedInBox)
        {
            bouncer.animateFlipped(g.getClipBounds());
            bouncer.setColor(Color.RED);
            bouncer.draw(g);
        }
        //Draw a ball with gravity! 
        else
        {
            bouncer.animate(g.getClipBounds());
            bouncer.setColor(Color.RED);
            bouncer.draw(g);
        }
        
        
        
        //challenge 20
        for(Projectile bunny : bunnyArray)
        {
            g.drawImage(smallBunny, bunny.getX(), bunny.getY(), this);
            bunny.animate();
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
        
        //challenge 19
        g.drawString("X: "+String.valueOf(mouseX), 480, 12);
        g.drawString("Y: "+String.valueOf(mouseY), 520, 12);
        
        g.setColor(Color.RED);
        g.drawRect(220, 140, 200, 200);
        
        challenge24.animate(g.getClipBounds());
        challenge24.draw(g);
        
    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    { 
        ball.setFrozen(!ball.isFrozen()); //Toggle the ball's frozen status.
        //challenge 18
        laserArray.clear();
         
        if(mouseX >= 220 && mouseX <= 270 &&
                mouseY >= 120 && mouseY <= 170)
        {   
            if(!clickedInBox)
                clickedInBox = true;
            else
                clickedInBox = false;
        }
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
            ball.increaseVel(1.1);
        if(c=='-')
            ball.decreaseVel(0.9);
        
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
        
        //challenge 20
        if(c == 'r')
        {
            Projectile tempstar = new Projectile();
            tempstar.fireWeapon((int)ball.getX(), (int)ball.getY());
            bunnyArray.add(tempstar);
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
    Image smallBunny;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        fatherBackground = toolkit.getImage("src/images/fatherBackground.jpg");
        fatherImage = toolkit.getImage("src/images/father.JPG");
        ballImage = toolkit.getImage("src/images/ball.gif");
        starImage = toolkit.getImage("src/images/star.gif");
        smallBunny = toolkit.getImage("src/images/smolBunny.jpg");
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
        //themeMusic = loadClip("src/audio/Earrape.wav");
        //bellSound = loadClip("src/audio/Skype.wav");
        if(themeMusic != null)
//            themeMusic.start(); //This would make it play once!
           themeMusic.loop(2); //This would make it loop 2 times.
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--

