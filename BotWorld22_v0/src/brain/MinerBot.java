package brain; //packages are used to organize code in Java.  

//Since these classes are in a different package, they needs to be imported.
import actor.BotBrain;   
import actor.Rock;
import grid.Location;
/**
 * @author Spock
 * MinerBot chooses a random move each turn, but chooses to MINE when beside a Rock.
 */
public class MinerBot extends BotBrain
{        
    public int chooseAction()
    {
        //Mine a Rock if you are beside it.
        if(directionTowardsAdjacentRock() != -1)
            return MINE+directionTowardsAdjacentRock();
        
        //Choose a random direction 0, 45, 90...
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
}
    

