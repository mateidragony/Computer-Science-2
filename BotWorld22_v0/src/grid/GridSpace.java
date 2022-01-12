package grid;

import actor.GameObject;

/**
 * This is just a collection class to link walls with grid spaces.
 * @author Spock
 */
public class GridSpace 
{
    public boolean wallNorth;
    public boolean wallEast;
    public boolean wallSouth;
    public boolean wallWest;
    public GameObject occupant;
    
    public GridSpace()
    {
        wallNorth = false;
        wallEast = false;
        wallSouth = false;
        wallWest = false;  
        occupant = null;
    }
    
}
