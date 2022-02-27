package brain.mateiBots;

import actor.Paper;
import actor.Rock;
import actor.Scissors;
import grid.Location;

public class DeoBot extends AbstractBot{


    private boolean shouldBid = true;

    @Override
    public void initForRound(){
        shouldBid = true;
    }

    @Override
    public int chooseAction() {

        if(getMoveNumber() == 499)
            System.out.println("Rock:"+getNumRocks()+" , Paper:"+ getNumPapers()+" , Scissors:"+getNumScissors());

        arena = getArena();

        Location rockLoc = getClosestLoc(getRockLocs(arena));
        //Rock closestRock = getClosestRock(getRocks(arena));
        Scissors closestScissors = getClosestScissor(getScissors(arena));
        Paper paper = getPaper(arena);

        if(paper == null)
            shouldBid = true;

        //----------------------------------
        //first get closest (rock or scissor)
        //----------------------------------
        if(this.getNumRocks() < 30 && paper!= null && paper.getCurrentTopBid()==0){
            if(directionOfAdjacent(rockLoc) != -1)
                return MINE + directionOfAdjacent(rockLoc);
            else if (closestScissors != null
                    && this.directionOfAdjacent(closestScissors.getLocation()) != -1)
                return CAPTURE;

            Location goal = rockLoc;
            if(closestScissors != null
                    && distanceTo(closestScissors.getLocation()) < distanceTo(rockLoc))
                goal = closestScissors.getLocation();

            return goTo(goal, arena);
        }

        //----------------------------------
        //Then go bid
        //----------------------------------
        else if(paper != null && shouldBid && getNumPapers() <= 28){
            if(isInCenter()) {
                shouldBid = false;
                return BID + paper.getCurrentTopBid() + 1;
            }
            else
                return goTo(new Location(12,12), arena);
        }

        //----------------------------------
        //Then get scissors
        //----------------------------------
        else if(closestScissors != null){
            if (this.directionOfAdjacent(closestScissors.getLocation()) != -1) {
                return CAPTURE;
            } else
                return goTo(closestScissors.getLocation(), arena);
        }
        //Otherwise, do nothing
        else
            return REST;
    }

}
