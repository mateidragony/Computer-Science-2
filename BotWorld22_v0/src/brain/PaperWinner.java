package brain;

import actor.BlockedLocation;
import actor.BotBrain;
import actor.Rock;
import grid.Location;

/**
 * PaperBoy acts like MinerBot until 20 rocks, 
 * Then goes to middle and bids on Papers (bid is 1 each time)
 * @author spockm
 */
public class PaperWinner extends BotBrain
{        
    public int chooseAction()
    {
        if(this.getNumRocks() < 20)
            return minerBot();
        else if(inCenterZone())
            return BID+1;
        else //Get to the center zone
            return beenThereBot(new Location(12,12));
    }
    
    public int minerBot()
    {
        if(directionTowardsAdjacentRock() != -1)
            return MINE+directionTowardsAdjacentRock();
        
        //Choose a random direction 0, 45, 90...
        //Add one thousand to it to move one space in that direction. 
        int randomDirection = (int)(Math.random()*8)*45;
        return randomDirection;
    }
    
    /**
     * Gets the direction toward an adjacent Rock (if any)
     * @return a direction where there is an adjacent Rock, -1 if no Rocks are adjacent.
     */
    public int directionTowardsAdjacentRock()
    {
        int result = -1;
        Location myLoc = new Location(getRow(), getCol());
        
        for(int dir=0; dir<360; dir+=45) //loop through the 8 directions
        {
            Location next = myLoc.getAdjacentLocation(dir);
            if(next.isValidLocation() && getArena()[next.getRow()][next.getCol()] instanceof Rock)
                result = dir;  
        }
        
        return result;
    }
    
    public boolean inCenterZone()
    {
        return 7 < getRow() && getRow() < 15 && 7 < getCol() && getCol() < 15;
    }
    
    
    //-----BeenThereBot---------------------------------------
    int[][] timesBeenThere = new int[24][24];
    
    public void initForRound()
    {
        //reset the number of times I think I've been to spots.
        for(int row=0;row<24;row++)
            for(int col=0; col<24; col++)
                timesBeenThere[row][col]=0;
    }
    public int beenThereBot(Location target)
    {
        //Increase the count of times I have been to the spot I'm on.
        timesBeenThere[getRow()][getCol()]++; 
        
        //Choose a random direction 0, 45, 90...
        int randomDirection = (int)(Math.random()*8)*45;
        //add in a 'bias' to the random (30% of time go toward target)
        if(Math.random() < .30)
            randomDirection = new Location(getRow(),getCol()).getDirectionToward(target);
        
           
        int loopCount = 0;
        //If I can't move the direction chosen, choose again! 
        while(!shouldMove(randomDirection) && loopCount < 10)
        {
            randomDirection = (int)(Math.random()*8)*45;
            loopCount++; 
        }    
                
        //Add one thousand to it to move one space in that direction. 
        return randomDirection;
    }

    /**
     * This is like the method canMove from SmartRandom, 
     * except it also avoids spaces it has been to more
     * than 2 times.  (Which isn't super effective.) 
     * @param direction
     * @return true if the Bot should move this direction.
     */
    public boolean shouldMove(int direction)
    {
        Location myLoc = new Location(getRow(), getCol());
        Location next = myLoc.getAdjacentLocation(direction);
        if(!next.isValidLocation())
            return false;
        if(getArena()[next.getRow()][next.getCol()] instanceof BlockedLocation)
            return false;
        if(timesBeenThere[next.getRow()][next.getCol()] > 2)
            return false;
        
        return true;
    }
    


}
    

