/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Font;

/**
 *
 * @author 22cloteauxm
 */
public class MovingWord {
    
    int x;
    int newX;
    int y;
    int xVel;
    String phrase;
    String newPhrase;
    Font myFont;
    
    double t;
    
    public MovingWord()
    {
        x = 0;
        y = 220;
        xVel = 2;
        phrase = "https://soundcloud.com/gooshal-padmoo/jemi-jumex";
        t = 0;
        myFont = new Font("Serif", Font.PLAIN, 24);
        
    }
    
    
    public void animate(Rectangle screenBounds)
    {
        x+=xVel;
        
        if(x > screenBounds.getMaxX()-503)
        {
            t+=0.2;
            newPhrase = phrase.substring(phrase.length()-(int)t, phrase.length());
            //newX += xVel;
            
            if(t>=phrase.length())
            {
                newX=0;
                x=0;
                phrase = newPhrase;
                t = 0;
                newPhrase = null;
            }
        }
        
        
    }
    
    public void draw(Graphics g)
    {
        g.setFont(myFont);
        g.setColor(Color.BLACK);
        g.drawString(phrase, x, y);
        if(newPhrase != null)
            g.drawString(newPhrase, newX, y);
    }
}
