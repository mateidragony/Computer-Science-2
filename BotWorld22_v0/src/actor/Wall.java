package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Wall extends GameObject implements BlockedLocation
{
    
    public Wall()
    {
        setColor(new Color(155,86,35));
    }
    public Wall(Color initialColor)
    {
//        setColor(initialColor);
        setColor(Color.DARK_GRAY);
    }
    public Wall(Wall b)
    {
//        setLocation(b.getLocation());
        setColor(Color.BLACK);
    }
    
    
    
    @Override
    public String toString()
    {
        String result = "Boulder: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Wall(this);
        return clone;
    }
}
