package gui;

import actor.GameObject;
import actor.Bot;
import grid.Grid;
import grid.Location;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import javax.swing.JPanel;
import world.BotWorld;

/**
 *
 * @authors Spock, Cox, Angle
 */
public class RatBotsScoreBoard extends JPanel
{
    private ArrayList<Bot> bots = new ArrayList<Bot>(); 
    private static int maxScore = 0;
    
    //sorting variables
    public static final int POINTS = 0;
    public static final int TOTAL_POINTS = 1;
    public static final int ROUNDS_WON = 2;
    public static final int NAME = 3;
    
    private int sortBy = POINTS;
    private boolean debugMode = false;
    private Grid<GameObject> theGrid; 
    
    public RatBotsScoreBoard(Grid gr)
    {
        theGrid = gr;
    }
    
    public void findRatsInGrid()
    {
        bots.clear();     
        ArrayList<Location> occupied = theGrid.getOccupiedLocations();
        
        for(Location loc : occupied)
        {
            if(theGrid.get(loc) instanceof Bot)
                bots.add((Bot)theGrid.get(loc));
        }
    }
    
//    public void updateScores()
//    {
//        findRatsInGrid();
//        // Look at all grid locations.
//        for (int r = 0; r < theGrid.getNumRows(); r++)
//        {
//            for (int c = 0; c < theGrid.getNumCols(); c++)
//            {
//                // If there's an object at this location, draw it.
//                Location loc = new Location(r, c);
//                
//                if (theGrid.get(loc) != null) 
//                {
//                    if(theGrid.get(loc) instanceof Marker)
//                    {
//                        Marker m = (Marker)theGrid.get(loc);
//                        for(Bot b : bots)
//                        {
//                            if(b.getColor().equals(m.getColor()))
//                                b.addToScore(m.getValue());
//                        }
//                    }    
//                }
//            }
//        }
//    }
    
    
    
//    public static void add(Bot in)
//    {
//        rats.add(in);
//    }
//    public static void remove(Bot in)
//    {
//        rats.remove(in);
//    }
   
    public static int X_OFFSET = 20;
    public static int ROW_SIZE = 55; 
    
    /**
     * Paint this component.
     * @param g the Graphics object to use to render this component
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        findRatsInGrid();
        
        super.paintComponent(g2);
        
        g.setFont(g.getFont().deriveFont((float) 18));
        g.drawString("Round # "+BotWorld.getRoundNum()+"   Move # "+BotWorld.getMoveNum(), X_OFFSET, 30);

        ArrayList<Bot> sorted = sortRats();
        
        for(int rb=0; rb<bots.size(); rb++)
        {
            Bot r = sorted.get(rb);
            int w = ROW_SIZE*2;
            int h = ROW_SIZE-5;

            g.setFont(new Font("Helvetica", Font.PLAIN, 10));
            
            //draw rectangle
            g.setColor(r.getColor());
            g.fillRect(X_OFFSET, 50 + ROW_SIZE*rb, 450, ROW_SIZE - 2);

            //write text info (score, RW, etc) to screen
            g.setColor(invertColor(r.getColor()));
            String roundPoints = String.valueOf(r.getScore());
            int averagePointsPerRound = (int)(r.getTotalScore()/Math.max(BotWorld.getRoundNum()-1,1));
            String averagePoints = String.valueOf(averagePointsPerRound);
            String roundsWon = String.valueOf(r.getRoundsWon());
            String name = r.getRatBot().getName();
            
            //Round Points
            if(sortBy == POINTS) g.setFont(new Font("Helvetica", Font.PLAIN, 20));
            else g.setFont(new Font("Helvetica", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(roundPoints, X_OFFSET + 80 - fm.stringWidth(roundPoints),
                    50 + ROW_SIZE*rb + 28);
            g.setFont(new Font("Helvetica", Font.PLAIN, 12));
            fm = g.getFontMetrics();
            g.drawString("Score", X_OFFSET + 80 - fm.stringWidth("Score"), 
                    50 + ROW_SIZE*rb + h/2 + 20);
            
            //Total Points
            if(sortBy == TOTAL_POINTS) g.setFont(new Font("Helvetica", Font.PLAIN, 20));
            else g.setFont(new Font("Helvetica", Font.BOLD, 20));
            fm = g.getFontMetrics();
            g.drawString(averagePoints, X_OFFSET + 165 - fm.stringWidth(averagePoints),
                    50 + ROW_SIZE*rb + 28);
            g.setFont(new Font("Helvetica", Font.PLAIN, 12));
            fm = g.getFontMetrics();
            g.drawString("Average", X_OFFSET + 165 - fm.stringWidth("Average"), 
                    50 + ROW_SIZE*rb + h/2 + 20);
            
            //Rounds Won
            if(sortBy == ROUNDS_WON) g.setFont(new Font("Helvetica", Font.PLAIN, 30));
            else g.setFont(new Font("Helvetica", Font.BOLD, 30));
            fm = g.getFontMetrics();
            g.drawString(roundsWon, X_OFFSET + 220 - fm.stringWidth(roundsWon),
                    50 + ROW_SIZE*rb + 38);           
            
            g.setFont(new Font("Helvetica", Font.PLAIN, 20));
            g.drawString(r.getRatBot().getName(),X_OFFSET + 180 + 55, 50 + ROW_SIZE*rb + 20);
//            g.drawString(numSeeds+" seeds",X_OFFSET + 180 + 55, 50 + ROW_SIZE*rb + 40);
            if(r.getAvgMSecUsed() > 3)
                g.drawString("SLOW! "+r.getAvgMSecUsed()+"mSec per turn",X_OFFSET + 240 + 55, 50 + ROW_SIZE*rb + 40);
            
            //Special stats for 2021
            
            //Energy
            g.setFont(new Font("Helvetica", Font.PLAIN, 14));
//            g.drawString("E:"+r.getEnergy(),X_OFFSET + 180 + 55, 50 + ROW_SIZE*rb + 38);
            //Health
//            if(r.getHealth() > 0)
//                g.drawString("H:"+r.getHealth(),X_OFFSET + 180 + 105, 50 + ROW_SIZE*rb + 38);
//            else
//                g.fillRect(X_OFFSET + 180 + 55, 50 + ROW_SIZE*rb + 38, 90, -14);
//            //Strength
//            g.drawString("St:"+r.getStrength(),X_OFFSET + 180 + 155, 50 + ROW_SIZE*rb + 38);
//            //Speed
//            g.drawString("Sp:"+r.getSpeed(),X_OFFSET + 180 + 205, 50 + ROW_SIZE*rb + 38);
//           
            
                        
            if(debugMode)
            {
                g.setColor(Color.BLACK);
                g.drawString("Prev choice = "+r.getMostRecentChoice(), 
                        X_OFFSET + 180 + 55, 50 + ROW_SIZE*rb + 42);
            }
        }
        calcMaxScore();
    }
    
    public void calcMaxScore() 
    {
        maxScore = -5000;
        for(Bot r : bots)
        {
            if(r.getScore() > maxScore)
                maxScore = r.getScore();
        }
    }
    
    public static int getMaxScore() { return maxScore; }
    
    /**
     * Returns the desired size of the display, for use by layout manager.
     * @return preferred size
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(450,400);
    }

    /**
     * Returns the minimum size of the display, for use by layout manager.
     * @return minimum size
     */
    public Dimension getMinimumSize()
    {
        return new Dimension(450,150);
    }
    
    public void toggleDebugMode()
    {
        debugMode = !debugMode;
    }
    
    public void setSortOrder(int so){
        if(so <= 4 && so >= 0) sortBy = so;
        else sortBy = 0;
    }
    
    private ArrayList<Bot> sortRats(){
        ArrayList<Bot> sorted = new ArrayList();
        if(bots.size() == 0) 
            return sorted;
        
        if(sortBy == NAME){
            ArrayList<String> origNames = new ArrayList();
            for(int i = 0; i < bots.size(); i++) 
                origNames.add(bots.get(i).getRatBot().getName());
            
            ArrayList<String> sortedNames = sortAlphabetically(origNames);
            for(int i = 0; i < sortedNames.size(); i++){
                int n = origNames.indexOf(sortedNames.get(i));
                sorted.add(bots.get(n));
                origNames.remove(n);
                origNames.add(n, null);
            }
        } else {
            int val1 = 0;
            int val2 = 0;
            sorted.add(bots.get(0));
            
            for(int i = 1; i < bots.size(); i++){
                if(sortBy == POINTS){
                    val1 = bots.get(i).getScore();
                } if(sortBy == TOTAL_POINTS){
                    val1 = bots.get(i).getTotalScore();
                } if(sortBy == ROUNDS_WON){
                    val1 = bots.get(i).getRoundsWon();
                } 
                for(int k = 0; k < sorted.size(); k++){
                    if(sortBy == POINTS){
                        val2 = sorted.get(k).getScore();
                    } if(sortBy == TOTAL_POINTS){
                        val2 = sorted.get(k).getTotalScore();
                    } if(sortBy == ROUNDS_WON){
                        val2 = sorted.get(k).getRoundsWon();
                    } 
                    
                    if(val1 > val2){ 
                        sorted.add(k, bots.get(i));
                        break;
                    }
                }
                
                if(!sorted.contains(bots.get(i))) sorted.add(bots.get(i));
            }
        }
        
        return sorted;
    }
    
    private ArrayList<String> sortAlphabetically(ArrayList<String> s){
        ArrayList<String> sorted = new ArrayList();
        sorted.add(s.get(0));
        for(int i = 1; i < s.size(); i++){
            for(int k = 0; k < sorted.size(); k++){
                if(sorted.get(k).compareTo(s.get(i)) > 0 || 
                        sorted.get(k).compareTo(s.get(i)) == 0){
                    sorted.add(k, s.get(i));
                    break;
                }
            }
            
            if(!sorted.contains(s.get(i))) sorted.add(s.get(i));
        }
        
        return sorted;
    }

    private Color invertColor(Color in)
    {
        float[] vals = new float[3];
        
        Color.RGBtoHSB(in.getRed(), in.getGreen(), in.getBlue(), vals);
        
        if(vals[2] < 0.85)
            return Color.WHITE;
            
        //otherwise return the inverse of the color.    
        return new Color(255-in.getRed(), 255-in.getGreen(), 255-in.getBlue());
    }
}
