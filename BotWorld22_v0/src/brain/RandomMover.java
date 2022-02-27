package brain;

import actor.BotBrain;
/**
 * @author Spock
 * RandomMover moves in a random direction each turn.
 */
public class RandomMover extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random direction 0, 45, 90...
        int randomDirection = (int)(Math.random()*8)*45;
        return randomDirection;
    }
}
    

