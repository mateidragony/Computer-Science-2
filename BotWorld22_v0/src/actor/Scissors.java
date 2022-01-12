package actor;

import grid.Location;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Scissors dart around, only changing direction when they are unable to move forward. 
 * It's hard to catch the running scissors! 
 * @author spockm
 */
public class Scissors extends GameObject implements BlockedLocation
{
    private final Color DEFAULT_COLOR = new Color(155,155,0);
    
    public Scissors()
    {
        setColor(DEFAULT_COLOR);
        
    }
    
    public Scissors(Scissors d)
    {
        super(d);
        setColor(DEFAULT_COLOR);
    }
    
    public void act()
    {
        ArrayList<Location> locs = possibleMoveLocations();
        if(locs.isEmpty()) return; //Nowhere to go!
        Location dest = locs.get((int)(Math.random()*locs.size()));
        this.setDirection(this.getLocation().getDirectionToward(dest));
        this.moveTo(dest);
    }
    
    public ArrayList<Location> possibleMoveLocations()
    {
        ArrayList<Location> locs = new ArrayList<Location>();
        Location forward = getLocation().getAdjacentLocation(getDirection());
        if(getGrid().isValid(forward) && getGrid().get(forward) == null)
        {   //If you can move forward, do so. 
            locs.add(forward);
            return locs;
        }
        //Otherwise choose any other direction.
        for(int d=0; d<360; d+=45)
        {
            Location neighbor = getLocation().getAdjacentLocation(d);
            if(getGrid().isValid(neighbor) && getGrid().get(neighbor) == null)
                locs.add(neighbor);
        }
        return locs;
    }
    
    @Override
    public String toString()
    {
        String result = "Scissors! Catch me if you can!!";
        return result;
    }    
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Scissors(this);
        return clone;
    }
    
}
