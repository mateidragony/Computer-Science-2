package gui;

import java.awt.Color;

/**
 * The ColorAssigner class is a utility that keeps track of colors used in 
 * the game.  
 * @author Spock
 */
public class RatBotsColorAssigner 
{
    private static int count = 0;
    
    public static void resetCount() { count = 0; }
    /**
     * This method assigns a new color every time it is asked.  
     * Each time a RatBot is added it is assigned a color.  
     * @return the next color on the list.  
     */
    public static Color getAssignedColor()
    {
        count++;
        switch(count) { // One more than count to include Grey.
            case 1: return new Color(239, 16, 5);       // Bright Red
            case 2: return new Color(0, 98, 250);       // Bright Blue
            case 3: return new Color(48, 242, 48);      // Green
            case 4: return new Color(252, 241, 25);     // Yellow
            case 5: return new Color(124, 45, 155);     // Purple
            case 6: return new Color(255, 165, 0);    // Orange  
            case 7: return new Color(255, 105, 180);      // Hot Pink
            case 10: return new Color(139, 69, 19);      // Brown
            case 12: return new Color(123,234,45);      // Lime Green
            case 8: return new Color(0,202,222);      // Turquoise
            case 14: return new Color(255, 255, 255);      // White
            case 11: return new Color(45, 42, 145);      // Navy Blue
            case 9: return new Color(120, 120, 120);      // Gray
            case 13: return new Color(0, 0, 0);      // Black
            case 15: return new Color(65, 142, 105);      // ????
        }
        return new Color(count*2, count, 200);                              // 

        //        switch(count%16)
//        {
//            case 1: return Color.RED;
//            case 2: return Color.BLUE;
//            case 3: return Color.GREEN;
//            case 4: return Color.MAGENTA;
//            case 5: return Color.PINK;
//            case 6: return Color.CYAN;
//            case 7: return Color.ORANGE;
//            case 8: return Color.WHITE;
//            case 9: return new Color(255,100,100);
//            case 10: return new Color(128,50,90);
//            case 11: return new Color(50,120,190);
//            case 12: return new Color(0,200,100);
//                    
//        }
//        return new Color(123,234,45);
    }
}
