package brain.mateiBots;

import actor.BotBrain;
import actor.GameObject;
import actor.Rock;
import actor.Scissors;
import brain.astar.AStar;
import grid.Location;

public class NgaujaBot01 extends AbstractBot {

    GameObject[][] theArena;

    @Override
    public void initForRound() {

    }

    @Override
    public int chooseAction() {

        theArena = getArena();

        AStar astar = new AStar(theArena);

        return astar.pathFind(new Location(getRow(),getCol()),new Location(23,23));
    }
}
