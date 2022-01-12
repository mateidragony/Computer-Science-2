package brain;

import actor.BotBrain;
import actor.GameObject;
import actor.Rock;
import actor.Scissors;
import grid.Location;
/**
 * @author Spock
 * ScissorsTracker goes toward the Scissors!
 */
public class ScissorsTracker extends BotBrain
{        
    public int chooseAction()
    {
        Location scissorsLoc = whereAreTheScissors();
        if(scissorsLoc == null) return REST;
        
        Location myLoc = new Location(getRow(), getCol());
        //Grab the scissors if they are close enough. (in a neighboring space)
        if(scissorsLoc.distanceTo(myLoc) < 2)
        {
            return CAPTURE; //3000 
        }
        //Go toward the scissors if possible, or move randomly. 
        int dirTowardScissors = myLoc.getDirectionToward(scissorsLoc);
        Location next = myLoc.getAdjacentLocation(dirTowardScissors);
        
        if(next.isValidLocation() && getArena()[next.getRow()][next.getCol()] == null)
            return dirTowardScissors;
        
        int randomDirection = (int)(Math.random()*8)*45;
        return randomDirection;
    }
    
    
    public Location whereAreTheScissors()
    {
        GameObject[][] theArena = getArena();
        for(int r=0; r<theArena.length; r++)
            for(int c=0; c<theArena[0].length; c++)
            {
                if(theArena[r][c] instanceof Scissors)
                    return new Location(r,c);
            }
        return null; //There are no scissors this turn. 
    }
}
    

