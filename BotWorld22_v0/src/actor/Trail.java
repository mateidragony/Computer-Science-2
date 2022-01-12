package actor;

import grid.Grid;
import grid.Location;
import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Trail extends GameObject 
{
    private final Color DEFAULT_COLOR = Color.WHITE;
    private final int TAIL_LENGTH = 21;
    
    private int turnsRemaining;

    public Trail()
    {
        setColor(DEFAULT_COLOR);
        turnsRemaining = TAIL_LENGTH;
    }
    public Trail(Color initialColor)
    {
        setColor(initialColor);
        turnsRemaining = TAIL_LENGTH;
    }
    public Trail(Trail b)
    {
        setLocation(b.getLocation());
        turnsRemaining = b.getTurnsRemaining();
        setColor(DEFAULT_COLOR);
    }
    
    public int getTurnsRemaining()
    {
        return turnsRemaining;
    }
    
    public void act()
    {
        turnsRemaining--;
        if(turnsRemaining == 0)
            this.removeSelfFromGrid();
    }
    
    

    @Override
    public String toString()
    {
        String result = "";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Trail(this);
        return clone;
    }

    
}
