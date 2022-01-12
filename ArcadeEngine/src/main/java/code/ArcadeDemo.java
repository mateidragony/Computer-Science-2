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


import Challenge25Game.Coin;
import Challenge25Game.Platform;
import Challenge25Game.Star;
import Challenge25Game.playerTest;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class ArcadeDemo extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------
    public playerTest me;
    
    public boolean running;
    public boolean facingRight;
    public boolean jetting;
    public boolean spacePressed;
    public boolean platsSet;
    public boolean gotJet;
    public boolean starsSet;
    public boolean inShop;
    
    public boolean bcDescription;
    public boolean bDescription;
    public boolean eDescription;
    public boolean bhDescription;
    public boolean jcDescription;
    public boolean purchasedMessage;
    public boolean maxedMessage;
    public boolean brokeMessage;
    
    public int width;
    public int height;
    
    public int red;
    public int green;
    public int blue;
    
    public int boughtBigCoin;
    public int boughtBoots;
    public int boughtEngine;
    public int boughtBlackHole;
    public int boughtJerryCan;
    
    public int moneyPower;
    
    public int floorNumber;
    public int screenNumber;
    
    public int timesClickedBC;
    public int timesClickedB;
    public int timesClickedE;
    public int timesClickedBH;
    public int timesClickedJC;
    
    public ArrayList<String> screenNames;
    
    public ArrayList<Platform> platList;
    public ArrayList<Coin> coinList;
    public ArrayList<Star> starList;
    
    public ArrayList<ArrayList<Platform>> plats;
    public ArrayList<ArrayList<Coin>> coins;
    public ArrayList<ArrayList<Star>> stars;
    
    public Random randy;
   
    
    //Instance Variables
    //-------------------------------------------------------
    
    
    //Constructor
    //-------------------------------------------------------
    public ArcadeDemo()
    {   //Enter the name and width and height.  
        super("Amidst the Stars", 1366, 700);
        width = 1566;
        height = 700;
        
        jetting = false;
        running = false;
        facingRight = true;
        me = new playerTest();
        //me.increaseWealth(30000);
        
        screenNames = new ArrayList<>();
        screenNames.add("Main");
        screenNames.add("Shop");
        
        platList = new ArrayList<>();
        plats = new ArrayList<>();
        coinList = new ArrayList<>();
        coins = new ArrayList<>();
        starList = new ArrayList<>();
        stars = new ArrayList<>();
        
        spacePressed = false;
        platsSet = false;
        starsSet = false;
        //inShop = true;
        //gotJet = true;
        
        red = 175;
        green = 220;
        blue = 252;
        
        floorNumber = 0;
        screenNumber = 0;
        
        timesClickedBC=0;
        timesClickedB=0;
        timesClickedE=0;
        timesClickedBH=0;
        timesClickedJC=0;
        
        randy = new Random();
        
        
    }
    
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected void renderFrame(Graphics g) 
    {
        floorNumber = (int)me.getFloorNumber();
        screenNumber = (int)me.getScreenNumber();
        
        moneyPower = me.getMoneyPower();
        
        if(floorNumber > 2)
            me.setPlatDist(50+(floorNumber-2)*20);
                
        //Only if im in the shop
        if(inShop)
        {
            g.setColor(new Color(128,64,0));
            g.fillRect(0, 0, width, height);
            
            g.drawImage(shopkeeper, 50, 250, this);
            
            g.setColor(new Color(62,31,0));
            g.fillRect(0,height-160, 650,160);
            g.setColor(new Color(50,25,0));
            g.fillRect(0,height-170, 650,10);
            
            
            g.setColor(Color.BLACK);
            g.fillRect(10, 10, 120, 40);
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Exit Shop", 25, 36);
                
            g.setColor(new Color(50,25,0));
            g.fillRect(800,200,150,20);
            g.fillRect(1100,200,150,20);
            g.fillRect(800,500,150,20);
            g.fillRect(1100,500,150,20);
            g.fillRect(500,200,150,20);
            
            g.setColor(Color.GREEN);
            g.fillRect(800,20,30*boughtBigCoin,20);
            g.fillRect(1100,20,30*boughtBoots,20);
            g.fillRect(800,320,30*boughtEngine,20);
            g.fillRect(1100,320,30*boughtBlackHole,20);
            g.fillRect(500,20,30*boughtJerryCan,20);
            
            g.setColor(Color.white);
            for(int i = 30; i<=150; i+=30)
                g.drawRect(800, 20, i, 20);
            for(int i = 30; i<=150; i+=30)
                g.drawRect(1100, 20, i, 20);
            for(int i = 30; i<=150; i+=30)
                g.drawRect(800, 320, i, 20);
            for(int i = 30; i<=150; i+=30)
                g.drawRect(1100, 320, i, 20);
            for(int i = 30; i<=150; i+=30)
                g.drawRect(500, 20, i, 20);
            
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Price: " + String.valueOf(300*(boughtBigCoin+1)), 800,70);
            g.drawString("Price: " + String.valueOf(300*(boughtBoots+1)), 1100,70);
            g.drawString("Price: " + String.valueOf(300*(boughtEngine+1)), 800,370);
            g.drawString("Price: " + String.valueOf(300*(boughtBlackHole+1)), 1100,370);
            g.drawString("Price: " + String.valueOf(300*(boughtJerryCan+1)), 500,70);
            
            g.drawImage(bigCoin, 825, 90, this);
            g.drawImage(boots, 1100, 130,145,71, this);
            g.drawImage(engine, 820, 400,124,102, this);
            g.drawImage(blackHole, 1140, 375,59,128, this);
            g.drawImage(jerryCan, 530, 83,85,118, this);
            
            
            //Writes the shopkeepers Messages
            g.drawString("Click once for a description", 100, 100);
            g.drawString("Click again to purchase", 100, 130);
            g.drawString("Money: "+String.valueOf(me.getWealth()), 100, 160);
            
            if(brokeMessage)
            {
                g.drawString("Don't try to scam me!", 350, 265);
                g.drawString("You're too broke to buy this", 350, 295);
            }
            else if(maxedMessage)
                g.drawString("I'm out! Guess you maxed it out, Huh.", 350, 265);
            else if(purchasedMessage)
            {
                g.drawString("MONEY MONEY MONEY!!", 350, 265);
                g.drawString("Oh! .... . ..", 350, 295);
                g.drawString("I mean.. enjoy...", 350, 325);
            }
            else if(bcDescription)
            {
                g.drawString("It's a big coin!", 350, 265);
                g.drawString("It increases the money from coins", 350, 295);
                g.drawString("And your periodic money", 350, 325);
            }
            else if(bDescription)
            {
                g.drawString("Better boots!", 350, 265);
                g.drawString("Makes you jump higher", 350, 295);
            }
            else if(eDescription)
            {
                g.drawString("It's a stronger engine", 350, 265);
                g.drawString("It increases the power of your JetPack", 350, 295);
                g.drawString("'Course you need a jetpack first though.", 350, 325);
            }
            else if(bhDescription)
            {
                g.drawString("It's a pocket black hole", 350, 265);
                g.drawString("It reduces the gravity", 350, 295);
                g.drawString("Be VERY careful!", 350, 325);
                g.drawString("...Don't ask me where I got it!", 350, 355);
            }
            else if(jcDescription)
            {
                g.drawString("It's a jerry can", 350, 265);
                g.drawString("It increases the fuel of your JetPack", 350, 295);
                g.drawString("'Course you need a jetpack first though.", 350, 325);
            }
        }
        
        
        if(!platsSet && screenNumber == 0)
        {
            ArrayList<Platform> tempPlatList;
            
            
            if(floorNumber == 0 && plats.isEmpty())
            {
                me.createPlatforms(550);
                platList = me.getPlatformList();
                me.createCoins(platList);
                coinList = me.getCoinList();
                tempPlatList = platList;
                plats.add(platList);
                coins.add(coinList);
            }
            else if(plats.size() <= floorNumber )
            {
                if(me.getYVel()>0)
                    me.setYVel(4);
                starsSet = false;
                me.createPlatforms(650);
                platList = me.getPlatformList();
                me.createCoins(platList);
                coinList = me.getCoinList();
                tempPlatList = platList;
                plats.add(floorNumber, platList);
                coins.add(floorNumber, coinList);
                if(floorNumber >= 7 && !starsSet)
                {
                    ArrayList<Star> tempStarList = new ArrayList<>();
                    for(int i=0; i<=20*(floorNumber-6); i++)
                    {
                        Star tempStar = new Star();
                        tempStarList.add(tempStar);
                    }
                    starsSet = true;
                    stars.add(tempStarList);
                }
            }
            else
            {
                tempPlatList = platList;
                platList = plats.get(floorNumber);
                coinList = coins.get(floorNumber);
                
                if(floorNumber >= 7)
                    starList = stars.get(floorNumber-7);
                if(!tempPlatList.equals(platList))
                {
                    if(me.getYVel()>0)
                        me.setYVel(4);
                }
            }
        }
        
        Platform jetPlat;
        if(floorNumber == 2 && screenNumber == 0)
        {
            jetPlat = new Platform(80,400,20,230);
            platList.add(jetPlat);
        }
        
        if(screenNumber == 0)
            me.setPlatforms(platList, coinList);
        
        if(me.getY()>=-48 && !inShop)
        {
            //create world 0-700;
            for(int r=0;r<=700;r+=100)
            {
                red = 240 - r/20;
                green = 180 - r/20;
                blue = 255 - r/20;
                if(red<0) red=0;
                if(blue<0) blue =0;
                if(green<0) green = 0;
                g.setColor(new Color(red,green,blue));
                g.fillRect(0, height-r, width, 100);
            }
        //g.setColor(new Color(125,195,227));
        //g.fillRect(0, 0, width, height);
            g.setColor(new Color(40,160,80));
            g.fillRect( 0, 600, width, 100);
            
            //1366X100
        }
        else if(!inShop)
        {
            for(int r=700*floorNumber;r<=1400*floorNumber;r+=100)
            {
                red = 240 - r/20;
                green = 180 - r/20;
                blue = 255 - r/20;
                if(red<0) red=0;
                if(blue<0) blue =0;
                if(green<0) green = 0;
                g.setColor(new Color(red,green,blue));
                g.fillRect(0, height-(r-700*floorNumber), width, 100);
            }            
        }
        
        if(floorNumber>=7 && !inShop)
        {
            for(Star s : starList)
            {
                g.drawImage(star, s.getX(), s.getY(), s.getSize(), s.getSize(), this);
            }
        }
        if(floorNumber == 0 && screenNumber == 0)
        {
            g.setColor(Color.black);
            g.setFont(new Font("Sans Serif", Font.BOLD, 17));
            g.drawString("Press W to jump", 60, 240);
            g.drawString("Press A and D to move", 60, 260);
            g.drawString("Press S to stop moving", 60, 280);
            g.drawString("Press R to reset the world", 60, 300);
            g.drawString("Use money to buy items in the shop", 60, 320);
            g.drawString("Coins give you more money", 60, 340);
            g.drawString("Press * for a surprise (Volume up!)", 60, 360);
            
            g.setFont(new Font("Sans Serif", Font.PLAIN, 22));
            g.drawString("SHOP", 1200, 390);
            g.drawString("-------->", 1200,420);
        }
        
        if(floorNumber == 2 && !gotJet)
        {
            g.drawImage(jet,180,366,24,34, this);
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 15));
            g.drawString("Touch JetPack to pick it up", 125, 305);
            g.drawString("Use space to activate it", 130, 320);
            g.drawString("Watch your fuel!", 160, 335);
        }
        
        if(me.getX()>=180 && me.getX()<=204 &&
                552-me.getY()>=1600 && 552-me.getY()<=1634)
            gotJet = true;
        
        
        if(screenNumber == 1 && !inShop)
        {
            g.drawImage(shop, 500, 350, this);
            g.drawImage(tree, 1000,275,430,354,this);
            g.drawImage(tree, 170,335,383,294,this);
            g.drawImage(tree, -100,275,430,354,this);
            g.drawImage(tree, 800,305,394,324,this);
        }
        
        if(gotJet && !inShop)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Fuel", 1200, 150);
            g.setColor(Color.red);
            g.fillRect(1200, 160, 40, 200);
            g.setColor(Color.green);
            g.fillRect(1200, 160, 40, (int)me.getFuel());
            g.setColor(Color.white);
            g.drawRect(1200, 160, 40, 200);
        }
           
        if(me.getX()>=2020 && me.getX()<=2063 && me.getY()==552 && !inShop)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Press S to enter shop", 600, 350);
        }
        
        
        if(me.getXVel() == 0)
            running = false;
        
        //what image should the character use
        if(jetting && facingRight)
            playerImage = jetF;
        else if (jetting && !facingRight)
            playerImage = jetB;
        else if(running == false && facingRight == true)
            playerImage = standing;
        else if(running == false && facingRight == false)
            playerImage = standingBack;
        else if(running == true && facingRight == true)
            playerImage = runningForward;
        else
            playerImage = runningBack;
        
        //if the character isnt running
        if(jetting && !inShop)
        {
            g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,22,48, this);
        }
        
        else if(running == false && !inShop)
        {
            me.setXVel(0.0);
            g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,18,48, this);
        }
        //if the character is running
        else if (!inShop)
            g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,22,48, this);
        
        me.animate();
        

        //place platforms at an interval of 60 pixels
        
                
        if(screenNumber == 0 && !inShop)
        {
            for(Platform myPlatform : platList)
            {
                myPlatform.draw(g);
            }

            for(Coin myCoin : coinList)
            {
                myCoin.draw(g);
            }
        }
        
        if(frameNumber%40==0 && !inShop)
            me.increaseWealth(2*moneyPower);
        
        
        if(!inShop)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Height: "+String.valueOf((int)(552-me.getY())), 1200, 24);
            g.drawString("Floor: "+String.valueOf(floorNumber), 1200, 48);
            g.drawString("Money: "+String.valueOf(me.getWealth()), 1200, 72);
            g.drawString("Screen: "+screenNames.get(screenNumber), 1200, 96);
          //  g.drawString("X: "+String.valueOf(me.getX()), 1200, 120);
          //  g.drawString("Y: "+String.valueOf(me.getY()), 1200, 148);
        }
        //g.drawString("Something: "+String.valueOf(me.getY()+700*floorNumber), 1200, 148);
        
       
        
    }//--end of renderFrame method--
    
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    {         
        boolean onExit = mouseX>=10 && mouseX<=130 && mouseY>= 10 && mouseY <= 50;
        boolean onBigCoin = mouseX>=825 && mouseX<= 931 && mouseY>=90 && mouseY <= 205;
        boolean onBoots = mouseX>=1100 && mouseX<= 1245 && mouseY>=130 && mouseY <= 201;
        boolean onEngine = mouseX>=820 && mouseX<= 944 && mouseY>=400 && mouseY <= 502;
        boolean onBlackHole = mouseX>=1140 && mouseX<= 1199 && mouseY>=375 && mouseY <= 503 ;
        boolean onJerryCan = mouseX>=530 && mouseX<= 615 && mouseY>=83 && mouseY <= 201;
        
        
        if(onExit)
        {
            inShop = false;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
        }
        
        if(onBigCoin && boughtBigCoin < 5 && timesClickedBC < 1)
        {
            timesClickedBC++;
            timesClickedB=0;timesClickedE=0;timesClickedBH=0;timesClickedJC=0;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            bcDescription = true;
        }
        else if(onBoots && boughtBoots < 5 && timesClickedB < 1)
        {
            timesClickedB++;
            timesClickedE=0;timesClickedBH=0;timesClickedJC=0;timesClickedBC=0;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            bDescription = true;
        }
        else if(onEngine && boughtEngine < 5 && timesClickedE < 1)
        {
            timesClickedE++;
            timesClickedBC=0;timesClickedB=0;timesClickedBH=0;timesClickedJC=0;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            eDescription = true;
        }
        else if(onBlackHole && boughtBlackHole < 5 && timesClickedBH < 1)
        {
            timesClickedBH++;
            timesClickedBC=0;timesClickedE=0;timesClickedB=0;timesClickedJC=0;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            bhDescription = true;
        }
        else if(onJerryCan && boughtJerryCan < 5 && timesClickedJC < 1)
        {
            timesClickedJC++;
            timesClickedBC=0;timesClickedB=0;timesClickedE=0;timesClickedBH=0;
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            jcDescription = true;
        }
        
        
        else if(onBigCoin && boughtBigCoin < 5 && timesClickedBC >= 1 && me.getWealth()-300*(boughtBigCoin+1) >= 0)
        {
            me.increaseWealth(-300*(boughtBigCoin+1));
            boughtBigCoin++;
            me.increaseMoneyPower(2);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onBoots && boughtBoots < 5 && timesClickedB >= 1 && me.getWealth()-300*(boughtBoots+1) >= 0)
        {
            me.increaseWealth(-300*(boughtBoots+1));
            boughtBoots++;
            me.increaseJumpPower(0.15);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onEngine && boughtEngine < 5 && timesClickedE >= 1 && me.getWealth()-300*(boughtEngine+1) >= 0)
        {
            me.increaseWealth(-300*(boughtEngine+1));
            boughtEngine++;
            me.increaseJetPower(0.0025);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onBlackHole && boughtBlackHole < 5 && timesClickedBH >= 1 && me.getWealth()-300*(boughtBlackHole+1) >= 0)
        {
            me.increaseWealth(-300*(boughtBlackHole+1));
            boughtBlackHole ++;
            me.increaseGravityPower(-0.007);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onJerryCan && boughtJerryCan < 5 && timesClickedJC >= 1 && me.getWealth()-300*(boughtJerryCan+1) >= 0)
        {
            me.increaseWealth(-300*(boughtJerryCan+1));
            boughtJerryCan++;
            me.increaseFuelPower(0.05);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onBigCoin || onBoots || onEngine || onBlackHole || onJerryCan)
        {
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            brokeMessage = true;
        }
    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar();
        

        if(c=='a')
        {
            
            me.moveLeft();
            facingRight = false;
            running = true;
        }
        if(c=='d')
        {
            me.moveRight();
            facingRight = true;
            running = true;
        }
        if(c=='s')
            me.stop();
        if(c=='s' && me.getX()>=2020 && me.getX()<=2063 && me.getY()==552)
            inShop = true;
        
        if(c=='*')
        {
            themeMusic = loadClip("src/audio/mozart.wav");
            initMusic();
        }
        
        if(c=='r')
        {
            me.increaseWealth(-1*me.getWealth());
            me.setY(552);
            plats.clear();
            coins.clear();
        }

    }
    
    public void keyPressed(KeyEvent e)
    {
        int v = e.getKeyCode();
        
        if(v == KeyEvent.VK_W)
        {
            if(me.canJump())
                me.jump();
            jetting = false;
        }
        if(v == KeyEvent.VK_SPACE && gotJet && me.getFuel() > 0)
        {
            jetting = true;
            me.jet();
        }
        else
        {
            jetting = false;
        }
        
        spacePressed = v==KeyEvent.VK_SPACE && inShop;
    }

    public void keyReleased(KeyEvent e)
    {
        int v = e.getKeyCode();
        if(v == KeyEvent.VK_SPACE)
            spacePressed = false;
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
    Image jetF;
    Image jetB;
    Image jet;
    Image star;
    
    Image shop;
    Image tree;
    Image shopkeeper;  
    Image bigCoin;
    Image boots;
    Image engine;
    Image blackHole;
    Image jerryCan;
    Image ground;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        ground = toolkit.getImage("src/images/ground.JPG");
        standing = toolkit.getImage("src/images/Standing No Bg.png");
        standingBack = toolkit.getImage("src/images/backNoBg.png");
        runningForward = toolkit.getImage("src/images/rNoBg.png");
        runningBack = toolkit.getImage("src/images/backRNoBg.png");
        jetF = toolkit.getImage("src/images/jetF.png");
        jetB = toolkit.getImage("src/images/jetB.png");
        jet = toolkit.getImage("src/images/jet.png");
        star = toolkit.getImage("src/images/star.JPG");
        shop = toolkit.getImage("src/images/shop.png");
        tree = toolkit.getImage("src/images/tree.png");
        shopkeeper = toolkit.getImage("src/images/shopkeeper.png");
        bigCoin = toolkit.getImage("src/images/bigCoin.png");
        boots = toolkit.getImage("src/images/boots.png");
        engine = toolkit.getImage("src/images/engine.png");
        blackHole = toolkit.getImage("src/images/blackHole.png");
        jerryCan = toolkit.getImage("src/images/jerryCan2.png");
    }
    
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
        bellSound = loadClip("src/audio/Skype.wav");
        if(themeMusic != null)
//            themeMusic.start(); //This would make it play once!
           themeMusic.loop(2); //This would make it loop 2 times.
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--

