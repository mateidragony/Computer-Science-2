package code;


/*
Challenges Completed
4,21,19,18,22,23
*/


import Challenge25Game.*;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class AmidstTheStars extends AnimationPanel 
{

    //Constants
    //-------------------------------------------------------
    public playerTest me;
    
    public boolean running,facingRight,jetting,hPressed,platsSet,gotJet,starsSet,inShop;
    public boolean gameStarted,seenLaunchPad,wantsToBuy,boughtRocket,inRocket,justGotIn,boomerMode;


    private boolean left, right, jump, down, pressedJet;


    public boolean bcDescription;
    public boolean bDescription;
    public boolean eDescription;
    public boolean bhDescription;
    public boolean jcDescription;
    public boolean purchasedMessage;
    public boolean maxedMessage;
    public boolean brokeMessage;
    public boolean rocketMessage;
    
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
    public int pWidth;
    
    public int timesClickedBC, timesClickedB, timesClickedE, timesClickedBH, timesClickedJC;
    
    public ArrayList<String> screenNames;
    
    public ArrayList<Platform> platList;
    public ArrayList<Coin> coinList;
    public ArrayList<Star> starList;
    public ArrayList<Tree> treeList;
    
    public ArrayList<ArrayList<Platform>> plats;
    public ArrayList<ArrayList<Coin>> coins;
    public ArrayList<ArrayList<Star>> stars;

    public Random randy;

    private int imageTimer;
    private ShopHandler shopHandler;

    //Instance Variables
    //-------------------------------------------------------
    
    
    //Constructor
    //-------------------------------------------------------
    public AmidstTheStars()
    {   //Enter the name and width and height.  
        super("Amidst the Stars", 1366, 700);
        width = 1366;
        height = 700;
        
        jetting = false;
        running = false;
        facingRight = true;
        me = new playerTest();
        //me.increaseWealth(3000000);
        //me.setX(3000);
        //boughtRocket = true;
        
        screenNames = new ArrayList<>();
        screenNames.add("Main");
        screenNames.add("Shop");
        screenNames.add("Rocket");
        
        platList = new ArrayList<>();
        plats = new ArrayList<>();
        coinList = new ArrayList<>();
        coins = new ArrayList<>();
        starList = new ArrayList<>();
        stars = new ArrayList<>();
        treeList = new ArrayList<>();
        
        hPressed = false;
        platsSet = false;
        starsSet = false;
        gameStarted = false;
        justGotIn = true;
        boomerMode = false;
        
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
        
        pWidth = 15;

        shopHandler = new ShopHandler(width, height);
    }
    
    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected void renderFrame(Graphics g) 
    {
        //Intro screen
        if(!gameStarted)
        {
            g.setColor(new Color(199,123,196));
            g.fillRect(0, 0, width, height);
            
            g.setColor(Color.black);
            
            g.setFont(new Font("Sans Serif", Font.PLAIN, 30));
            g.drawString("Amidst The Stars", 550, 100);
            
            g.setFont(new Font("Sans Serif", Font.PLAIN, 22));
            g.drawString("You were traveling far away from your home, when", 430, 180);
            g.drawString("your ship malfunctioned and crashed into a nearby", 430, 230);
            g.drawString("planet. This place is very strange, and the creatures", 430, 280);
            g.drawString("that live here stranger. The air seems breathable", 430, 330);
            g.drawString("and the conditions tolerable, nevertheless you must rush", 430, 380);
            g.drawString("to return home. There is dire business to attend to back home. ", 430, 430);
            g.drawString("Press Enter to Begin", 540, 580);
            
            g.drawRect(1100, 500, 35, 35);
            g.drawString("Boomer Mode", 1150, 525);
            
            if(boomerMode)
            {
                g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
                g.setColor(Color.red);
                g.drawString("X", 1104,532);
            }
            
            return;
        }

        //End screen
        if(552-me.getY() >= 35000)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
            
            g.setColor (Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("After spending far too much time on the distant planet", 430, 160);
            g.drawString("named \"Earth\" you have finally returned home. However,", 430, 210);
            g.drawString("your home has changed. Everything is quiet... too quiet.", 430, 260);
            g.drawString("Where is Everyone?...", 430, 310);
            g.drawString("What Happened?....", 430, 360);
            g.drawString("How much time have you truly spent on \"Earth\"?....", 430, 410);
            g.drawString("You win, but at what cost?", 520, 560);
            return;
        }
        
        floorNumber = (int)me.getFloorNumber();
        screenNumber = (int)me.getScreenNumber();
        
        moneyPower = me.getMoneyPower();
        
        me.setPlatXDist(Math.floorDiv(floorNumber, 3)+3);
        me.setPlatWidth(pWidth);
        
        boolean soldOut = boughtJerryCan == 5 && boughtEngine == 5 && boughtBoots == 5
                && boughtBlackHole == 5 && boughtBigCoin == 5;
        
        if(floorNumber > 2)
            me.setPlatDist(50+(floorNumber-2)*10);
                
        //Only if im in the shop
        if(inShop)
            shopHandler.drawShop(g,this,me,seenLaunchPad);
        
        //Set the platforms
        if(!platsSet && screenNumber == 0)
        {
            ArrayList<Platform> tempPlatList;
            
            
            if(floorNumber == 0 && plats.isEmpty())
            {
                me.createPlatforms(550);
                platList = me.getPlatformList();
                me.createCoins(platList);
                coinList = me.getCoinList();
                plats.add(platList);
                coins.add(coinList);
            }
            else if(plats.size() <= floorNumber )
            {
                if(me.getYVel()>0 && me.getYVel()<6)
                    me.setYVel(6);
                starsSet = false;
                me.createPlatforms(650);
                platList = me.getPlatformList();
                me.createCoins(platList);
                coinList = me.getCoinList();
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
                    if(me.getYVel()>0 && me.getYVel()<6)
                        me.setYVel(6);
                }
            }
        }

        //Setup jetpack platform
        Platform jetPlat;
        if(floorNumber == 2 && screenNumber == 0)
        {
            jetPlat = new Platform(80,400,20,230);
            platList.add(jetPlat);
        }
        
        if(screenNumber == 0)
            me.setPlatforms(platList, coinList);
        else
            me.setPlatforms(null,null);

        //draw the sky and ground
        if(me.getY()>=-48 && !inShop)
        {
            //create world 0-700;
            for(int r=0;r<=700;r+=100)
            {
                red = 170 - r/20;
                green = 200 - r/20;
                blue = 255 - r/20;

                g.setColor(new Color(red,green,blue));
                g.fillRect(0, height-r, width, 100);
            }

            g.setColor(new Color(40,160,80));
            g.fillRect( 0, 600, width, 100);

            for(int i=0; i<30; i++){
                g.drawImage(ground, i*60, 600, 60,60, this);
            }
            
            //1366X100
        }

        else if(!inShop)
        {
            for(int r=700*floorNumber;r<=1400*floorNumber;r+=100)
            {
                red = 170 - r/20;
                green = 200 - r/20;
                blue = 255 - r/20;
                if(red<0) red=0;
                if(blue<0) blue =0;
                if(green<0) green = 0;
                g.setColor(new Color(red,green,blue));
                g.fillRect(0, height-(r-700*floorNumber), width, 100);
            }            
        }

        //draw the stars
        if(floorNumber>=7 && !inShop)
        {
            if(stars.size() <= floorNumber-7)
            {
                starsSet = false;

                ArrayList<Star> tempStarList = new ArrayList<>();
                for (int i = 0; i <= 20 * (floorNumber - 6); i++) {
                    Star tempStar = new Star();
                    tempStarList.add(tempStar);
                }

                starsSet = true;
                stars.add(tempStarList);

            }
            
            starList = stars.get(floorNumber-7);
            
            for(Star s : starList)
            {
                g.drawImage(star, s.getX(), s.getY(), s.getSize(), s.getSize(), this);
            }
        }
        
        //draw the directions if you're in the start screen
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
            g.setColor(Color.RED);
            g.drawString("Press H for hints on how to git gud", 60, 380);
            
            g.setColor(Color.black);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 22));
            g.drawString("SHOP", 1200, 190);
            g.drawString("-------->", 1200, 220);
        }

        //draw the hints
        if(floorNumber == 0 && screenNumber == 0 && hPressed)
        {
            g.setColor(Color.black);
            g.setFont(new Font("Sans Serif", Font.BOLD, 17));
            g.fillRect(790, 90, 390, 320);
            g.setColor(Color.white);
            g.drawString("Hints (press H to remove)", 870, 115);
            g.drawString("Pressing A or D increases your speed by", 800, 145);
            g.drawString("some amount. Pressing multiple times keeps", 800, 165);
            g.drawString("increasing your speed until a set limit.", 800, 185);
            g.drawString("You are subject to friction on the ground,", 800, 205);
            g.drawString("and in the air. Friction slows you down.", 800, 225);
            g.drawString("Jumping sets your speed upwards by some ", 800, 265);
            g.drawString("amount. Gravity decreases your speed", 800, 285);
            g.drawString("Using the Jetpack increases your speed.", 800, 305);
            g.drawString("Don't be afraid to explore your new world.", 800, 345);
            g.drawString("The shop kinda looks like a tavern, dumb dumb", 800, 385);
        }

        //draw the jetpack hint
        if(floorNumber == 2 && !gotJet && screenNumber == 0)
        {
            g.drawImage(jet,180,366,24,34, this);
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 15));
            g.drawString("Touch JetPack to pick it up", 125, 305);
            g.drawString("Use space to activate it", 130, 320);
            g.drawString("Watch your fuel!", 160, 335);
        }

        //if you touched the jetpack
        if(me.getX()>=180 && me.getX()<=204 &&
                552-me.getY()>=1600 && 552-me.getY()<=1634)
            gotJet = true;
        
        //draw the outside of the shop + forest
        if(screenNumber == 1 && !inShop && floorNumber == 0)
        {
            g.drawImage(shop, 500, 350, this);
            g.drawImage(tree, 1000,275,430,354,this);
            g.drawImage(tree, 170,335,383,294,this);
            g.drawImage(tree, -100,275,430,354,this);
            g.drawImage(tree, 800,305,394,324,this);
            g.setColor(Color.black);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 22));
            g.drawString("Launch Pad", 1200, 190);
            g.drawString("--------------->", 1200,220);
        }
        
        

        
        //draw the fuel gauge
        if(gotJet && !inShop)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Fuel", 1150, 150);
            g.setColor(Color.red);
            g.fillRect(1150, 160, 40, 200);
            g.setColor(Color.green);
            int fuelWidth = (int)(200*(me.getFuel()/(200+me.getFuelPower())));
            g.fillRect(1150, 160+(200-fuelWidth), 40, fuelWidth);
            g.setColor(Color.white);
            g.drawRect(1150, 160, 40, 200);
        }

        //press s to enter shop
        if(me.getX()>=2020 && me.getX()<=2063 && me.getY()==552 && !inShop)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Press S to enter shop", 600, 350);
        }
        
        //draw the screen with the rocket platform
        if(screenNumber == 2 && !inShop && floorNumber == 0)
        {
            seenLaunchPad = true;
            
            Platform launchBase1 = new Platform(370, 579, 20, 700, new Color(95,100,95), new Color(95,100,95));
            Platform launchBase2 = new Platform(520, 559, 20, 400, new Color(105,110,105), new Color(105,110,105));
            
            ArrayList<Platform> myPlatList = new ArrayList<>();
            
            myPlatList.add(launchBase1);
            myPlatList.add(launchBase2);

            me.setPlatforms(myPlatList, new ArrayList<>());
            
            
            launchBase2.draw(g);
            launchBase1.draw(g);
            
            if(!boughtRocket)
            {
                g.setColor(Color.black);
                g.setFont(new Font("Sans Serif", Font.BOLD, 17));
                g.drawString("The one in the tavern", 629, 400);
                g.drawString("Shall sell his most precious", 615, 420);
                g.drawString("Rocket ship only to", 638, 440);
                g.drawString("His most loyal patron", 629,460);
                //g.drawString("The sky is the limit", 60,480);
            }
            else
            {
                if(!inRocket)
                    g.drawImage(rocket, 600, 300, this);
                launchBase2.draw(g);
                launchBase1.draw(g);
                g.setColor(new Color(40,160,80));
                g.fillRect( 0, 600, width, 100);
                
                if(me.getX()>=3350 && me.getX()<=3485)
                {
                    g.setColor(Color.black);
                    g.setFont(new Font("Sans Serif", Font.BOLD, 20));
                    g.drawString("Press S to Enter Rocket", 590, 250);
                }
            }
        }
        
        
        
        if(me.getXVel() == 0)
            running = false;


        handlePlayerMovement();


        //handle the player's images
        if(!inRocket)
        {
            //what image should the character use
            if(jetting && facingRight)
                playerImage = jetF;
            else if (jetting)
                playerImage = jetB;
            else if(!running && facingRight)
                playerImage = standing;
            else if(!running)
                playerImage = standingBack;
            else
                handleRunningImages();

            //if the character isn't running
            if(jetting && !inShop) {
                g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,22,48, this);
            } else if(!running && !inShop) {
                me.setXVel(0.0);
                g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,18,48, this);
            }

            //if the character is running
            else if (!inShop)
                g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber,32,48, this);

            me.animate();
        }
        if(inRocket)
        {
            if(justGotIn) {
                me.setX(600+width*screenNumber); me.setY(300); justGotIn = false;
            }
            
            playerImage = rocket;
            g.drawImage(playerImage, (int)me.getX()-width*screenNumber, (int)me.getY()+700*floorNumber, this);
            me.flyAway();
        }
        
        
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
        

        //draw stats
        if(!inShop)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            g.drawString("Distance From Home: "+(35000-(int)(552-me.getY())), 1070, 24);
            g.drawString("Floor: "+(floorNumber), 1200, 48);
            g.drawString("Money: "+(me.getWealth()), 1200, 72);
        }

    }//--end of renderFrame method--



    public void handlePlayerMovement(){

        if(left){
            me.moveLeft();
            facingRight = false;
            running = true;
        }if(right){
            me.moveRight();
            facingRight = true;
            running = true;
        }if(jump){
            if(me.canJump()) {
                me.jump();
            }
        }if(down){
            me.stop();
        }if(pressedJet && gotJet && me.getFuel() > 0){
            jetting = true;
            me.jet();
        } else {
            jetting = false;
        }

    }
    public void handleRunningImages(){

        if(imageTimer > 60) {
            imageTimer = 0;
        } else {
            imageTimer++;
        }

        if(facingRight){
            if(imageTimer<10){
                playerImage = runningF.get(0);
            }else if(imageTimer<20){
                playerImage = runningF.get(1);
            }else if(imageTimer<30){
                playerImage = runningF.get(2);
            }else if(imageTimer<40){
                playerImage = runningF.get(3);
            }else if(imageTimer<50){
                playerImage = runningF.get(4);
            } else {
                playerImage = runningF.get(5);
            }
        } else {
            if(imageTimer<10){
                playerImage = runningB.get(0);
            }else if(imageTimer<20){
                playerImage = runningB.get(1);
            }else if(imageTimer<30){
                playerImage = runningB.get(2);
            }else if(imageTimer<40){
                playerImage = runningB.get(3);
            }else if(imageTimer<50){
                playerImage = runningB.get(4);
            } else {
                playerImage = runningB.get(5);
            }
        }

    }


    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    {               
        Rectangle r = new Rectangle(1100, 500, 35, 35);
        if(!gameStarted && !boomerMode && r.contains(mouseX, mouseY))
        {
            boomerMode = true; pWidth = 50; 
        }
        else if(!gameStarted && boomerMode && r.contains(mouseX, mouseY))
        {
            boomerMode = false; pWidth = 15;
        }
    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar();
        
        if(c=='M')
        {
            me.increaseWealth(3000000);
            gotJet = true;
        }
        
        if(c=='h')
            hPressed = !hPressed;

        if(c=='s' && me.getX()>=2020 && me.getX()<=2063 && me.getY()==552)
            inShop = true;
        if(c=='s' && me.getX()>=3350 && me.getX()<=3485 && boughtRocket)
            inRocket = true;
        
        if(c=='*')
        {
            themeMusic.loop(1);
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
        
        if(v == KeyEvent.VK_ENTER)
            gameStarted = true;
        
        if(v == KeyEvent.VK_W || v == KeyEvent.VK_UP) {
            jump = true;
        } if(v==KeyEvent.VK_A || v==KeyEvent.VK_LEFT){
            left = true;
        } if(v==KeyEvent.VK_D || v==KeyEvent.VK_RIGHT){
            right = true;
        } if(v==KeyEvent.VK_S || v==KeyEvent.VK_DOWN){
            down=true;
        }if(v == KeyEvent.VK_SPACE) {
            pressedJet=true;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int v = e.getKeyCode();

        if(v == KeyEvent.VK_W || v == KeyEvent.VK_UP) {
            jump = false;
        } if(v==KeyEvent.VK_A || v==KeyEvent.VK_LEFT){
            left = false;
        } if(v==KeyEvent.VK_D || v==KeyEvent.VK_RIGHT){
            right = false;
        } if(v==KeyEvent.VK_S || v==KeyEvent.VK_DOWN){
            down=false;
        }if(v == KeyEvent.VK_SPACE) {
            pressedJet=false;
        }

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
    
    Image rocket;
    Image shop;
    Image tree;
    Image ground;

    ArrayList<Image> runningF;
    ArrayList<Image> runningB;

    ArrayList<Image> jumpingF;
    ArrayList<Image> jumpingB;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        ground = toolkit.getImage("ArcadeEngine/src/main/java/images/grass.png");
        standing = toolkit.getImage("ArcadeEngine/src/main/java/images/Standing No Bg.png");
        standingBack = toolkit.getImage("ArcadeEngine/src/main/java/images/backNoBg.png");
        runningForward = toolkit.getImage("ArcadeEngine/src/main/java/images/rNoBg.png");
        runningBack = toolkit.getImage("ArcadeEngine/src/main/java/images/backRNoBg.gif");
        jetF = toolkit.getImage("ArcadeEngine/src/main/java/images/jetF.png");
        jetB = toolkit.getImage("ArcadeEngine/src/main/java/images/jetB.png");
        jet = toolkit.getImage("ArcadeEngine/src/main/java/images/jet.png");
        star = toolkit.getImage("ArcadeEngine/src/main/java/images/star.JPG");
        shop = toolkit.getImage("ArcadeEngine/src/main/java/images/shop.png");
        tree = toolkit.getImage("ArcadeEngine/src/main/java/images/tree.png");
        rocket = toolkit.getImage("ArcadeEngine/src/main/java/images/rocket.png");


        runningF = new ArrayList<>();
        runningB = new ArrayList<>();
        jumpingF = new ArrayList<>();
        jumpingB = new ArrayList<>();

        for(int i=1; i<=6; i++){
            runningF.add(toolkit.getImage("ArcadeEngine/src/main/java/images/Running/rf"+i+".png"));
            runningB.add(toolkit.getImage("ArcadeEngine/src/main/java/images/Running/rb"+i+".png"));
        }

        for(int i=1; i<=5; i++){
            jumpingF.add(toolkit.getImage("ArcadeEngine/src/main/java/images/Jumping/jf"+i+".png"));
            jumpingB.add(toolkit.getImage("ArcadeEngine/src/main/java/images/Jumping/jb"+i+".png"));
        }

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
        underSong = loadClip("ArcadeEngine/src/main/java/audio/under.wav");
        //challenge 22
        //themeMusic = loadClip("src/audio/Earrape.wav");
        bellSound = loadClip("ArcadeEngine/src/main/java/audio/Skype.wav");
        themeMusic = loadClip("ArcadeEngine/src/main/java/audio/mozart.wav");
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//--end of ArcadeDemo class--

