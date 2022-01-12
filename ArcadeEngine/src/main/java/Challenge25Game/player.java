/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge25Game;

import java.util.ArrayList;

/**
 *
 * @author 22cloteauxm
 */
public class player {
    private double x;
    private double y;
    private double xVel;
    private double yVel;
    private ArrayList<Platform> platforms;
    private ArrayList<Coin> coins;
    private ArrayList<Coin> deletedCoins;
    private int ground = 552;
    private Platform thePlat;
    private int floorNumber;
    private double fuel;
    private int wealth;
    private int platDist;
    private int screenNumber;
    
    private double jumpPower;
    private double jetPower;
    private double gravityPower;
    private int moneyPower;
    private double fuelPower;
    
    public player()
    {
        x = 20;
        y = ground;
        yVel = 0.0;
        fuel = 200.0;
        platDist = 50;
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
    public int getGround(){return ground;}
    public double getYVel(){return yVel;}
    public double getFloorNumber() {return Math.floorDiv((int)(700-y-48),700);}
    public double getFuel() {return fuel;}
    public int getWealth() {return wealth;}
    public double getScreenNumber(){return Math.floorDiv((int)x, 1366);}
    public int getMoneyPower(){return moneyPower;}
    
    public void increaseYVel(double c){yVel += c;}
    public void setXVel(double c){xVel = c;}
    public void setYVel(double in){yVel = in;}
    public void setX(double in) {x=in;}
    public void setY(double in) {y=in;}
    public void setFuel(double in) {fuel = in;}
    public void setPlatDist(int in) {platDist = in;}
    public void increaseWealth(int in){wealth+=in;}
    public void increaseJumpPower(double in){jumpPower+=in;}
    public void increaseJetPower(double in){jetPower+=in;}
    public void increaseGravityPower(double in){gravityPower+=in;} 
    public void increaseMoneyPower(int in){moneyPower+=in;}
    public void increaseFuelPower(double in){fuelPower+=in;}
    
    public void clearPlatforms() {platforms.clear(); coins.clear();}
    public void setPlatforms(ArrayList<Platform> p, ArrayList<Coin> c) {platforms = p; coins = c;}
    public void createPlatforms(int startY) 
    {
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        deletedCoins = new ArrayList<>();
        
        for(int i = startY; i>-20; i-= platDist )
        {
            Platform tempPlat;
            Coin tempCoin;
            
            if(i==startY)
                tempPlat = new Platform(i,50,3);
            else
                tempPlat = new Platform(i,15,3);

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
    public void jump(){yVel=3.5+jumpPower;}
    public void jet(){yVel+=0.4+jetPower; fuel-=20-fuelPower; if(fuel<0) fuel=0.0;}
    public void moveRight() {
        xVel+=2.0;
        if(xVel>5)
            xVel = 5.0;
    }
    public void moveLeft() {
        xVel-=2.0;
        if(xVel<-5)
            xVel = -5.0;
    }
    
    public boolean isJumping(){return(yVel!=0.0);}
    
    public ArrayList<Platform> getPlatformList(){return platforms;}
    public ArrayList<Coin> getCoinList(){return coins;}
    
    public void animate()
    {   
        if(xVel<0.5 && xVel>0)
            xVel = 0;
        if(xVel>-0.5 && xVel<0)
            xVel = 0;
        
        floorNumber = Math.floorDiv((int)(700-y-48),700);
        screenNumber = Math.floorDiv((int)x, 1366);
        
        //interaction with platforms
        if(floorNumber == 0)
            ground = 552;
        else
            ground = 10000;
        
        for(Platform p : platforms)
        {
            if(yVel<=0 && y+700*floorNumber<=p.getY()-48 &&
                    x+10>=p.getMinX() && x+10<=p.getMaxX())
            {
                thePlat = p;
                ground = thePlat.getY()-48;
            }
        }
             
        //interaction with coins
        for(int i=0; i<coins.size(); i++)
        {
            Coin c = coins.get(i);
            
            if(c.getY() >= y+700*floorNumber & c.getY() <= (y+700*floorNumber)+48 &&
                    x+10>=c.getX() && x+10<=c.getX() + 10)
            {
                wealth += c.getValue();
                coins.remove(c);
                deletedCoins.add(c);
            }
        }
        

        //interaction with borders
        if(x < 2) {
            x = 2;
            xVel = 0.0;
        }
        if(x > 1333  && floorNumber > 0) {
            x = 1333;
            xVel = 0.0;
        }
        if(x > 1363+1333*screenNumber  && screenNumber == 1) {
            x = 1363+1333*screenNumber;
            xVel = 0.0;
        }
        
        //movement
        y-=yVel;
        x+= xVel;

        
        if(y+700*floorNumber < ground || yVel>0)
        {
            yVel-= gravityPower;
        }
        else
        {
            fuel+=10.0;
            if(fuel>200)
                fuel = 200;
            y = ground-700*floorNumber;
            yVel=0.0;
            xVel *= 0.97;
        }   
        
    } 
    
}
