package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Paper extends GameObject implements BlockedLocation
{
    private final Color DEFAULT_COLOR = new Color(80,80,80);
    
    private int numPages;
    private int turnsUntilAuctionEnds;
    private int currentTopBid;
    private Bot whichBotHasBestBid = null;

    public Paper()
    {
        setColor(DEFAULT_COLOR);
        numPages = 5;
        turnsUntilAuctionEnds = 25;
        currentTopBid = 0;
    }
    public Paper(int amount, int duration)
    {
        setColor(DEFAULT_COLOR);
        numPages = amount;
        turnsUntilAuctionEnds = duration;
        currentTopBid = 0;
        
    }
    public Paper(Paper b)
    {
//        setLocation(b.getLocation());
        numPages = b.getNumPages();
        turnsUntilAuctionEnds = b.getTurnsUntilAuctionEnds();
        setColor(DEFAULT_COLOR);
    }    

    public int getNumPages() { return numPages; }
    public int getTurnsUntilAuctionEnds() { return turnsUntilAuctionEnds; }
    public int getCurrentTopBid() { return currentTopBid; }
    
    public void act()
    {
        turnsUntilAuctionEnds--;
        
        if(turnsUntilAuctionEnds == 0)
        {
            if(currentTopBid > 0) //there was a bid
            {
                whichBotHasBestBid.wonBid(this); 
            }
            
            this.removeSelfFromGrid();
        }
    }
    
    public void recieveBid(Bot b, int amount)
    {
        if(amount > currentTopBid)
        {
            currentTopBid = amount;
            whichBotHasBestBid = b;
        }
    }   
    
    
    @Override
    public String toString()
    {
        String result = "Paper with "+numPages+" pages. Top bid of "+currentTopBid+". Auction ends in "+turnsUntilAuctionEnds+" turns.";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Paper(this);
        return clone;
    }


}
