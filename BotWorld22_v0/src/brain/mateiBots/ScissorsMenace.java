package brain.mateiBots;

import actor.GameObject;
import actor.Scissors;

public class ScissorsMenace extends AbstractBot{

    @Override
    public void initForRound() {

    }

    @Override
    public int chooseAction() {

        arena = getArena();

        Scissors closestScissors = this.getClosestScissor(getScissors(arena));

        if(closestScissors != null) {
            if (this.directionOfAdjacent(closestScissors.getLocation()) != -1) {
                return CAPTURE;
            } else
                return goTo(closestScissors.getLocation(), arena);
        } else
            return REST;
    }
}
