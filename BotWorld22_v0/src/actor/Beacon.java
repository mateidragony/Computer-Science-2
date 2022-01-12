package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Beacon extends GameObject implements BlockedLocation
{
    int amountRemaining;
    
    public Beacon()
    {
        setColor(Color.DARK_GRAY);
        amountRemaining = 3;
    }
    public Beacon(int amount)
    {
        amountRemaining = amount;
    }
    public Beacon(Beacon b)
    {
        amountRemaining = b.getAmountRemaining();
    }
    
    public int getAmountRemaining()
    {
        return amountRemaining;
    }
    
    public boolean mineRock()
    {
        if(amountRemaining > 0)
        {
            amountRemaining--;
            this.removeSelfFromGrid();
            return true;
        }
        return false; //shouldn't ever happen, the rock is gone! 
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
        GameObject clone = new Beacon(this);
        return clone;
    }
}
