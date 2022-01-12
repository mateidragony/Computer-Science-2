package Challenge25Game;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Arrays;

public class ShopHandler {


    private final int width, height;
    private int boughtBigCoin, boughtBoots, boughtEngine, boughtBlackHole, boughtJerryCan;

    private boolean[] itemMessageBools = new boolean[8];
    //Broke, Maxed, Purchased, Fuel, Big Coin, Boots, Engine, Black Hole
    // No to rocket, yes to rocket, sold out

    private final int BROKE=0,MAXED=1,PURCHASED=2,FUEL=3,BC=4,BOOTS=5,ENGINE=6,BH=7,NO=8,YES=9,SOLDOUT=10;

    public ShopHandler(int w,int h){
        width = w;
        height = h;

        initGraphics();
        initMessages();
    }

    public void drawShop(Graphics g, ImageObserver io, playerTest me, boolean seenLaunchPad){
        //Draw background
        g.setColor(new Color(128,64,0));
        g.fillRect(0, 0, width, height);

        g.drawImage(itemImages.get(0), 50, 250, io);

        //draw Table
        g.setColor(new Color(62,31,0));
        g.fillRect(0,height-160, 650,160);
        g.setColor(new Color(50,25,0));
        g.fillRect(0,height-170, 650,10);

        //Exit Button
        g.setColor(Color.BLACK);
        g.fillRect(10, 10, 120, 40);
        g.setColor(Color.white);
        g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        g.drawString("Exit Shop", 25, 36);

        //Draw the shelves
        g.setColor(new Color(50,25,0));
        g.fillRect(800,200,150,20);
        g.fillRect(1100,200,150,20);
        g.fillRect(800,500,150,20);
        g.fillRect(1100,500,150,20);
        g.fillRect(500,200,150,20);

        //Draw the green showing how many you bought
        g.setColor(Color.GREEN);
        g.fillRect(800,20,30*boughtBigCoin,20);
        g.fillRect(1100,20,30*boughtBoots,20);
        g.fillRect(800,320,30*boughtEngine,20);
        g.fillRect(1100,320,30*boughtBlackHole,20);
        g.fillRect(500,20,30*boughtJerryCan,20);

        //Draw the white separators
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


        //Price is Dependent on how many times you bought
        g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        g.drawString("Price: " + (300*(boughtBigCoin+1)), 800,70);
        g.drawString("Price: " + (300*(boughtBoots+1)), 1100,70);
        g.drawString("Price: " + (300*(boughtEngine+1)), 800,370);
        g.drawString("Price: " + (300*(boughtBlackHole+1)), 1100,370);
        g.drawString("Price: " + (300*(boughtJerryCan+1)), 500,70);

        //Draw the items
        g.drawImage(itemImages.get(1), 825, 90, io);
        g.drawImage(itemImages.get(2), 1100, 130,145,71, io);
        g.drawImage(itemImages.get(3), 820, 400,124,102, io);
        g.drawImage(itemImages.get(4), 1140, 375,59,128, io);
        g.drawImage(itemImages.get(5), 530, 83,85,118, io);


        //Writes the shopkeepers Messages
        g.drawString("Click once for a description", 100, 100);
        g.drawString("Click again to purchase", 100, 130);
        g.drawString("Money: "+(me.getWealth()), 100, 160);

        //If you've seen the launchpad you can ask about the rocket
        if(seenLaunchPad)
        {
            g.setColor(Color.black);
            g.fillRect(100,195, 182, 40);
            g.setColor(Color.white);
            g.drawString("Ask about Rocket" , 110, 222);
        }
    }




    public void shopMouseEvents(int mouseX, int mouseY, boolean seenLaunchPad){

        boolean onExit = mouseX>=10 && mouseX<=130 && mouseY>= 10 && mouseY <= 50;
        boolean onBigCoin = mouseX>=825 && mouseX<= 931 && mouseY>=90 && mouseY <= 205;
        boolean onBoots = mouseX>=1100 && mouseX<= 1245 && mouseY>=130 && mouseY <= 201;
        boolean onEngine = mouseX>=820 && mouseX<= 944 && mouseY>=400 && mouseY <= 502;
        boolean onBlackHole = mouseX>=1140 && mouseX<= 1199 && mouseY>=375 && mouseY <= 503 ;
        boolean onJerryCan = mouseX>=530 && mouseX<= 615 && mouseY>=83 && mouseY <= 201;
        boolean onQuestion = mouseX>=100 && mouseX<= 282 && mouseY>=195 && mouseY <= 235;

        boolean[] onButton = {onBigCoin, onBoots, onEngine, onBlackHole, onJerryCan};


        if(onExit) {
            inShop = false;
            Arrays.fill(messageBools, false);
        } else if(onQuestion && seenLaunchPad) {
            rocketMessage = true;
            Arrays.fill(messageBools, false);
        }


        for(boolean bool:onButton){
            if(bool){

            }
        }


    }


    /*

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


        else if(onBigCoin && boughtBigCoin < 5 && me.getWealth() - 300 * (boughtBigCoin + 1) >= 0)
        {
            me.increaseWealth(-300*(boughtBigCoin+1));
            boughtBigCoin++;
            me.increaseMoneyPower(2);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onBoots && boughtBoots < 5 && me.getWealth() - 300 * (boughtBoots + 1) >= 0)
        {
            me.increaseWealth(-300*(boughtBoots+1));
            boughtBoots++;
            me.increaseJumpPower(0.15);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onEngine && boughtEngine < 5 && me.getWealth() - 300 * (boughtEngine + 1) >= 0)
        {
            me.increaseWealth(-300*(boughtEngine+1));
            boughtEngine++;
            me.increaseJetPower(0.02);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onBlackHole && boughtBlackHole < 5 && me.getWealth() - 300 * (boughtBlackHole + 1) >= 0)
        {
            me.increaseWealth(-300*(boughtBlackHole+1));
            boughtBlackHole ++;
            me.increaseGravityPower(-0.001);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onJerryCan && boughtJerryCan < 5 && me.getWealth() - 300 * (boughtJerryCan + 1) >= 0)
        {
            me.increaseWealth(-300*(boughtJerryCan+1));
            boughtJerryCan++;
            me.increaseFuelPower(50);
            bcDescription=false;bDescription=false;eDescription=false;bhDescription=false;jcDescription=false;
            purchasedMessage = false; maxedMessage = false; brokeMessage = false;
            purchasedMessage = true;
        }
        else if(onQuestion && wantsToBuy && me.getWealth()-50000 >= 0 && !boughtRocket)
        {
            me.increaseWealth(-50000);
            boughtRocket = true;
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
     */









    private static final ArrayList<Image> itemImages = new ArrayList<>();
    private static final ArrayList<String> shopMessages = new ArrayList<>();

    private static void initGraphics(){

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/shopkeeper.png"));
        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/bigCoin.png"));
        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/boots.png"));
        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/engine.png"));
        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/blackHole.png"));
        itemImages.add(toolkit.getImage("ArcadeEngine/src/main/java/images/jerryCan2.png"));
    }

    //order goes
    //Broke, Maxed, Purchased, Fuel, Big Coin, Boots, Engine, Black Hole
    // No to rocket, yes to rocket, sold out
    private static void initMessages(){
        shopMessages.add("Don't try to scam me \n You're too broke to buy this");
        shopMessages.add("I'm out! Guess you sold me out, Huh.");
        shopMessages.add("MONEY MONEY MONEY!! \n Oh! ... .. . \n I mean.. enjoy...");
        shopMessages.add("It's a big coin! \n It increases the money from coins \n and your periodic money");
        shopMessages.add("Better boots! \n Makes you jump higher");
        shopMessages.add("It's a stronger engine \n It increases the power of your Jetpack");
        shopMessages.add("It's a pocket balck hole \n Reduces the gravity \n Be VERY careful.. \n ..Don't ask me where I got it!");
        shopMessages.add("A very nice jerry can \n Increases the fuel in your Jetpack");
        shopMessages.add("No! I will never sell you my rocket \n It's Mine! \n Don't ask about it again!");
        shopMessages.add("Hmmm. You have been a very loyal customer \n I guess I could sell it to you... \n for 50000 coins!");
        shopMessages.add("Wow! I'm all sold out! \n You really are my best customer \n Thank you!");
    }
//Draw strings at 250, 265
}
