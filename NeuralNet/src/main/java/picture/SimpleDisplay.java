package picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author spockm
 */
public class SimpleDisplay extends JPanel implements KeyListener
{
    public double [][] inputArray;
    public double [][] expectedOutputArray;
    public double [][] outputArray;
    public int displayNum;
    
    public SimpleDisplay(int width, int height)
    {
        this.setPreferredSize(new Dimension(width,height));
//        this.setLocation(80,80);    //move to the right
        this.setVisible (true);         // make it visible to the user
        this.setFocusable(true);
        this.addKeyListener(this);
        displayNum = 0;
    }
    
    public void paintComponent(Graphics g) 
    {
        this.requestFocusInWindow();
        renderScreen(g);
    }

    public void renderScreen(Graphics g)
    {
        //Fill the background square.
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,640,480);
        
        
        for(int q=0; q<10; q++)
        {
            drawTestDigitResult(g,q*40,q+displayNum);
        }
        
        
        
        //General Text (Draw this last to make sure it's on top.)
//        g.setColor(Color.BLACK);
//        g.drawString("Machine Learning 2021", 10, 12);
    }
    
    public void drawTestDigitResult(Graphics g, int offset, int num)
    {
        drawDigit(g,offset,num);
        drawProbabilityHistogram(g,offset,num);
    }
    
    public void drawDigit(Graphics g, int offset, int num)
    {
        int row=0; 
        int col=0;
        for(double d : inputArray[num])
        {
            int amount = (int)(d*255);
            g.setColor(new Color(amount,amount,amount));
            g.fillRect(col,row+offset, 1,1);
            col++;
            if(col==28)
            {
                col=0;
                row++;
            }
        }
    }
    
    public void drawProbabilityHistogram(Graphics g, int Yoffset, int num)
    {
        int xLoc = 50;
        int actualValue = 11;
        int max = 0;
        g.setColor(Color.BLACK);
        g.drawRect(xLoc, Yoffset, 100, 28);
        for(int z=0; z<outputArray[num].length; z++)
        {
            double d = outputArray[num][z];
            if(d > outputArray[num][max]) max = z; //find what I'd guess
            int height = (int)(28*d);
            g.setColor(Color.red);
            if(expectedOutputArray[num][z] > .99)
            {
                g.setColor(Color.green);
                actualValue = z;
            }
            g.fillRect(xLoc, Yoffset+(28-height), 10, height);
            xLoc+=10;
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        g.drawString(""+actualValue,34,Yoffset+24);
        if(max == actualValue)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.RED);
        g.drawString("I thought it was a "+max+" with "+(int)(outputArray[num][max]*100)+"% probability.",xLoc+10, Yoffset+28);
    }
    
    public void loadData(double[][] in, double [][] ex, double[][] out)
    {
        inputArray = in;
        expectedOutputArray = ex;
        outputArray = out;
    }
    
    // KeyListener
    public void keyPressed (KeyEvent e) {}
    public void keyTyped (KeyEvent e) 
    {
        char c = e.getKeyChar();
        //Use the IJKM keys to move the PongBall
        if(c=='w' || c=='W')
        {
            displayNum += 10;
            this.repaint();
        }
        if(c=='s' || c=='S')
        {
            displayNum -= 10;
            if(displayNum < 0) displayNum = 0;
            this.repaint();
        }
            
    }
    public void keyReleased (KeyEvent e) {}  
    
}
