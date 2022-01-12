/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge25Game;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

/**
 *
 * @author 22cloteauxm
 */
public class Platform {
    
    private int x;
    private final int y;
    private final int width;
    private final int height;
    private Color col;
    private Color col2;
    
    public Platform(int yLoc, int w, int xDist)
    {
        Random randy = new Random();
        
        y = yLoc;
        
        x = (int)(randy.nextInt(xDist)+4-(xDist/5)) * 100;
        
        if(x>1000)
            x = 1000;
        if(x<0)
            x=0;
        
        height = 10;
        width = (randy.nextInt(4) + 4) * w;
        col = Color.BLACK;
        col2 = Color.white;
    }
    
    public Platform(int xLoc, int yLoc, int h, int w)
    {
        x = xLoc;
        y = yLoc;
        height = h;
        width = w;
        
        col = Color.BLACK;
        col2 = Color.white;
    }
    
    public Platform(int xLoc, int yLoc, int h, int w, Color c, Color c2)
    {
        x = xLoc;
        y = yLoc;
        height = h;
        width = w;
        
        col = c;
        col2 = c2;
    }
    
    
    public int getY(){return y;}
    public int getMinX(){return x;}
    public int getMaxX(){return x+width;}
    public int getWidth() {return width;}
    
    public void draw(Graphics g)
    {
        g.setColor(col);
        g.fillRect(x, y, width, height);
        g.setColor(col2);
        g.drawRect(x, y, width, height);
    }
}
