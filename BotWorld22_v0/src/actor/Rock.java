package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Rock extends GameObject implements BlockedLocation
{
    private final Color DEFAULT_COLOR = new Color(25,25,25);
    int amountRemaining;
    
    public Rock()
    {
        setColor(DEFAULT_COLOR);
        amountRemaining = 3;
    }
    public Rock(int amount)
    {
        setColor(DEFAULT_COLOR);
        amountRemaining = amount;
    }
    public Rock(Rock b)
    {
        amountRemaining = b.getAmountRemaining();
    }
    
    public int getAmountRemaining()
    {
        return amountRemaining;
    }
    
    public void mineRock()
    {
        if(amountRemaining > 25) //You get double for big Rocks
        {
            amountRemaining--;
        }
        if(amountRemaining > 0)
        {
            amountRemaining--;
        }
        if(amountRemaining == 0)
            this.removeSelfFromGrid();
    }
    
    @Override
    public String toString()
    {
        String result = "Rock: able to be mined "+amountRemaining+" more times.";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Rock(this);
        return clone;
    }
}
