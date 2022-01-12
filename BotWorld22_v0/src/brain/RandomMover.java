package brain;

import actor.BotBrain;
/**
 * @author Spock
 * RandomKnight chooses a random move each turn.
 */
public class RandomMover extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random direction 0, 45, 90...
        //Add one thousand to it to move one space in that direction. 
        int randomDirection = (int)(Math.random()*8)*45;
        return randomDirection;
    }
}
    

