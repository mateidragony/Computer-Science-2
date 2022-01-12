/*
 * 
 */

package gui;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class RatBotsColorScheme 
{
    private final int NUM_SCHEMES = 4;
    
    private final Color[] backgroundColor = new Color[NUM_SCHEMES];
    private Color[] wallColor = new Color[NUM_SCHEMES];
    private Color[] materialColor = new Color[NUM_SCHEMES];
    private Color[] destinationColor = new Color[NUM_SCHEMES];
    private Color[] destinationHighlight = new Color[NUM_SCHEMES];
    private Color[] powerUpColor = new Color[NUM_SCHEMES];
    private Color[] gridLineColor = new Color[NUM_SCHEMES];
    private Color[] ratAliveColor = new Color[NUM_SCHEMES];
    private Color[] ratDeadColor = new Color[NUM_SCHEMES];
    
    private int scheme;
    
    
    public RatBotsColorScheme()
    {
        scheme = 0;
        init();
    }
    
    public void changeScheme()
    {
        scheme++;
        if(scheme == NUM_SCHEMES)
            scheme = 0;
    }
    
    public Color getBackgroundColor() { return backgroundColor[scheme]; }
    public Color getWallColor() { return wallColor[scheme]; }
    public Color getMaterialColor() { return materialColor[scheme]; }
    public Color getDestinationColor() { return destinationColor[scheme]; }
    public Color getDestinationHighlight() { return destinationHighlight[scheme]; }
    public Color getPowerUpColor() { return powerUpColor[scheme]; }
    public Color getGridLineColor() { return gridLineColor[scheme]; }
    public Color getRatAliveColor() { return ratAliveColor[scheme]; }
    public Color getRatDeadColor() { return ratDeadColor[scheme]; }
    
    private void init()
    {
        //The default color scheme for 2022
        backgroundColor[0] = new Color(200,200,220);
        wallColor[0] = Color.RED;
        materialColor[0] = Color.YELLOW;
        destinationColor[0] = Color.BLUE;
        destinationHighlight[0] = new Color(100,50,255);
        powerUpColor[0] = Color.LIGHT_GRAY;
        gridLineColor[0] = Color.BLACK;
        ratAliveColor[0] = Color.BLACK;
        ratDeadColor[0] = Color.LIGHT_GRAY;
                
        //Alternate scheme
        backgroundColor[1] = Color.BLACK;
        wallColor[1] = Color.YELLOW;
        materialColor[1] = Color.BLUE;
        destinationColor[1] = Color.GREEN;
        destinationHighlight[1] = Color.BLUE;
        powerUpColor[1] = Color.LIGHT_GRAY;
        gridLineColor[1] = Color.DARK_GRAY;
        ratAliveColor[1] = Color.BLACK;
        ratDeadColor[1] = Color.LIGHT_GRAY;
                
        //Alternate (Inverse)
        backgroundColor[2] = Color.LIGHT_GRAY;
        wallColor[2] = Color.BLACK;
        materialColor[2] = Color.DARK_GRAY;
        destinationColor[2] = Color.BLACK;
        destinationHighlight[2] = Color.BLUE;
        powerUpColor[2] = Color.DARK_GRAY;
        gridLineColor[2] = Color.DARK_GRAY;
        ratAliveColor[2] = Color.BLACK;
        ratDeadColor[2] = Color.WHITE;
                
        //Alternate (Inverse)
        int n = 3;
        backgroundColor[n] = Color.WHITE;
        wallColor[n] = Color.BLACK;
        materialColor[n] = Color.DARK_GRAY;
        destinationColor[n] = Color.BLACK;
        destinationHighlight[n] = Color.BLUE;
        powerUpColor[n] = Color.LIGHT_GRAY;
        gridLineColor[n] = Color.DARK_GRAY;
        ratAliveColor[n] = Color.BLACK;
        ratDeadColor[n] = Color.WHITE;
        
    }
}
