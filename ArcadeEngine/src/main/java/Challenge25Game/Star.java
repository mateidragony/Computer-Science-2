/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge25Game;

import java.util.Random;
import java.awt.Graphics;

/**
 *
 * @author 22cloteauxm
 */
public class Star {
    
    private int starSize;
    private int starX;
    private int starY;
    private Random randy;
    
    public Star()
    {
        randy = new Random();
        
        starSize = randy.nextInt(10)+1;
        starX = randy.nextInt(1400);
        starY = randy.nextInt(700);
    }
    
    public int getX(){return starX;}
    public int getY(){return starY;}
    public int getSize(){return starSize;}
}
