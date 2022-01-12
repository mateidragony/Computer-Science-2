/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge25Game;

import java.awt.Graphics;
import java.awt.Color;


/**
 *
 * @author 22cloteauxm
 */
public class Coin {
    
    private int value;
    private int x;
    private int y;
    
    public Coin(int xLoc, int yLoc)
    {
        value = 0;
        x = xLoc;
        y = yLoc;
    }
    
    public int getX() {return x;}
    public int getY() {return y;}
    public int getValue() {return value;}
    
    public void setValue(int in) {value = in;}
    
    public void draw (Graphics g)
    {
        g.setColor(Color.orange);
        g.drawRect(x, y, 6, 10);
        g.setColor(Color.yellow);
        g.fillRect(x, y, 6, 10);
    }
    
}
