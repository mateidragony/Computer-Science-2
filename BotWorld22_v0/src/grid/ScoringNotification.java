package grid;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class ScoringNotification 
{
    Color color; 
    Location location;
    int amount;
    int duration;
    
    public ScoringNotification(Color c, Location loc, int a)
    {
        color = c;
        location = new Location(loc.getRow(), loc.getCol());
        amount = a;
        duration = 5;
    }
    
    public Color getColor() { return color; }
    public Location getLocation() { return location; }
    public int getAmount() { return amount; }
    public int getDuration() { return duration; }
    
    public void fadeAway()
    {
        duration--;
    }
}
