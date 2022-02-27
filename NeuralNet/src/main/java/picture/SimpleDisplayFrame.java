package picture;


import javax.swing.JFrame;

public class SimpleDisplayFrame 
{
    SimpleDisplay world = new SimpleDisplay(640,480);
    
    JFrame myFrame;


    public SimpleDisplayFrame() 
    {
        myFrame = new JFrame();
        myFrame.addWindowListener(new Closer());
        myFrame.setTitle("SimpleDisplayPanel - for Machine Learning demo");
        
        myFrame.add(world);
        myFrame.setSize(world.getPreferredSize());
        myFrame.setVisible(true);
        
                myFrame.getComponent(0).repaint();
                myFrame.setSize(myFrame.getComponent(0).getPreferredSize());
    }
        
    private static class Closer extends java.awt.event.WindowAdapter 
    {   
        public void windowClosing (java.awt.event.WindowEvent e) 
        {   System.exit (0);
        }   //======================
    }      
    
    public void loadData(double[][] inputArray, double [][] expectedOutputArray, double[][] outputArray)
    {
        world.loadData(inputArray, expectedOutputArray, outputArray);
                myFrame.getComponent(0).repaint();
                myFrame.setSize(myFrame.getComponent(0).getPreferredSize());
    }
    
}
