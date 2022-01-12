/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge25Game;

import java.util.ArrayList;
//import java.awt.Graphics;
//import java.awt.Color;

/**
 *
 * @author 22cloteauxm
 */
public class playerTest {
    private double x;
    private double y;
    private double xVel;
    private double yVel;
    private ArrayList<Platform> platforms;
    private ArrayList<Coin> coins;
    private int ground = 552;
    private int floorNumber;
    private double fuel;
    private int wealth;
    private int platDist;
    private int platXDist;
    private int platWidth;

    private double jumpPower;
    private double jetPower;
    private double gravityPower;
    private int moneyPower;
    private double fuelPower;

    private boolean isJumping;

    public playerTest()
    {
        x = 20;
        y = ground;
        yVel = 0.0;
        fuel = 200.0;
        platDist = 50;
        platXDist = 3;
        platWidth = 15;
        wealth = 0;
        
        jumpPower = 0;
        jetPower = 0;
        gravityPower = 0.1;
        moneyPower = 1;
        fuelPower = 0.0;
    }
    
    public double getX() {return x;}
    public double getY() {return y;}
    public double getXVel() {return xVel;}
    public double getYVel(){return yVel;}
    public double getFloorNumber() {return Math.floorDiv((int)(700-y-48),700);}
    public double getFuel() {return fuel;}
    public int getWealth() {return wealth;}
    public double getScreenNumber(){return Math.floorDiv((int)x, 1366);}
    public int getMoneyPower(){return moneyPower;}
    public double getFuelPower(){return fuelPower;}

    public void setPlatWidth(int c){platWidth = c;}
    public void setXVel(double c){xVel = c;}
    public void setYVel(double in){yVel = in;}
    public void setX(double in) {x=in;}
    public void setY(double in) {y=in;}
    public void setPlatDist(int in) {platDist = in;}
    public void setPlatXDist(int in) {platXDist = in;}
    public void increaseWealth(int in){wealth+=in;}
    public void increaseJumpPower(double in){jumpPower+=in;}
    public void increaseJetPower(double in){jetPower+=in;}
    public void increaseGravityPower(double in){gravityPower+=in;} 
    public void increaseMoneyPower(int in){moneyPower+=in;}
    public void increaseFuelPower(double in){fuelPower+=in;}

    public void setPlatforms(ArrayList<Platform> p, ArrayList<Coin> c) {platforms = p; coins = c;}
    public void createPlatforms(int startY) 
    {
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        
        for(int i = startY; i>-20; i-= platDist )
        {
            Platform tempPlat;
            
            if(i==startY)
                tempPlat = new Platform(i,50, platXDist);
            else
                tempPlat = new Platform(i,platWidth, platXDist);
                
            platforms.add(tempPlat);
        }
    }
    
    public void createCoins(ArrayList<Platform> p)
    {
        for(int i = 0; i<p.size(); i++)
        {
            Platform tempPlat = p.get(i);
            Coin tempCoin;
            
            if(i%2==0)
            {
                tempCoin = new Coin(tempPlat.getMinX()+tempPlat.getWidth()/2-3, tempPlat.getY()-20);
                tempCoin.setValue(((floorNumber+1)*moneyPower)*20);
                coins.add(tempCoin);
            }
                
        }
    }
    
    public void stop(){xVel=0.0;}
    public void jump(){yVel=1.7*(3.5+jumpPower); isJumping = true;}
    public void jet(){yVel+=(0.4+jetPower); fuel-=20; if(fuel<0) fuel=0.0;}
    public void moveRight() {
        xVel+=4.0;
        if(xVel>5)
            xVel = 5.0;
    }
    public void moveLeft() {
        xVel-=4.0;
        if(xVel<-5)
            xVel = -5.0;
    }
    
    public boolean canJump(){return !isJumping;}
    
    public ArrayList<Platform> getPlatformList(){return platforms;}
    public ArrayList<Coin> getCoinList(){return coins;}
    
    public void flyAway()
    {
        if(yVel > 30)
            yVel = 30;
        
        xVel = 0;
        yVel += 0.1;
        
        y-=yVel;
    }
    
    public void animate()
    {   
        if(xVel<0.5 && xVel>0)
            xVel = 0;
        if(xVel>-0.5 && xVel<0)
            xVel = 0;
        
        floorNumber = Math.floorDiv((int)(700-y-48),700);
        int screenNumber = Math.floorDiv((int) x, 1366);
        
        //interaction with platforms
        if(floorNumber == 0)
            ground = 552;
        else
            ground = 10000;
        if(platforms != null) {
            for(Platform p : platforms) {
                if(yVel<=0 && y+700*floorNumber<=p.getY()-48 &&
                        x+10-1366* screenNumber >=p.getMinX() && x-1366* screenNumber +10<=p.getMaxX())
                {
                    ground = p.getY()-48;
                }
            }
        }
        //interaction with coins
        if(coins != null){
            for(int i=0; i<coins.size(); i++) {
                Coin c = coins.get(i);

                if(c.getY() >= y+700*floorNumber & c.getY() <= (y+700*floorNumber)+48 &&
                        x-5<=c.getX() && x+25>=c.getX())
                {
                    wealth += c.getValue();
                    coins.remove(c);
                }
            }
        }

        //movement
        y-=yVel;
        x+= xVel;

        
        if(y+700*floorNumber < ground || yVel>0) {
            yVel-= 2.5*gravityPower;
        } else {
            fuel+=15.0;
            if(fuel>200+fuelPower)
                fuel = 200+fuelPower;
            y = ground-700*floorNumber;
            yVel=0.0;
            isJumping = false;
        }

        if(xVel > 0)
            xVel --;
        else if(xVel < 0)
            xVel++;

    } 
    
}
