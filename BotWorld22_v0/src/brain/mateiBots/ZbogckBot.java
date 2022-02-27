package brain.mateiBots;

import actor.Paper;
import actor.Rock;
import actor.Scissors;
import grid.Location;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ZbogckBot extends AbstractBot{


    private int totalRock,totalPaper,totalScissors;

    private boolean shouldBid = true;

    @Override
    public void initForRound(){
        shouldBid = true;
    }

    @Override
    public int chooseAction() {

        //try {

            if (getMoveNumber() == 499) {
                //System.out.println("Totals   Rock:"+getNumRocks()+" , Paper:"+ getNumPapers()+" , Scissors:"+getNumScissors());
                totalRock += getNumRocks();
                totalScissors += getNumScissors();
                totalPaper += getNumPapers();
            }
            if (getRoundNumber() == 99 && getMoveNumber() == 499) {
                System.out.println();
                System.out.println("Rock:" + totalRock / 99 + " , Paper:" + totalPaper / 99 + " , Scissors:" + totalScissors / 99);
            }

            arena = getArena();

            Location rockLoc = getClosestLoc(getRockLocs(arena));
            Scissors closestScissors = getClosestScissor(getScissors(arena));
            Paper paper = getPaper(arena);

            ArrayList<Location> phaseTwoLocs = new ArrayList<>();
            phaseTwoLocs.add(new Location(10, 12));
            phaseTwoLocs.add(new Location(11, 10));
            phaseTwoLocs.add(new Location(13, 11));
            phaseTwoLocs.add(new Location(12, 13));
            if (closestScissors != null)
                phaseTwoLocs.add(closestScissors.getLocation());
            else
                phaseTwoLocs.add(null);

            if (paper == null)
                shouldBid = true;

            //----------------------------------
            //first get closest (rock or scissor) (33 magic number)
            //----------------------------------
            if (rockLoc != null && this.getNumRocks() < 40 && paper != null && paper.getCurrentTopBid() == 0) {
                if (directionOfAdjacent(rockLoc) != -1)
                    return MINE + directionOfAdjacent(rockLoc);
                else if (closestScissors != null
                        && this.directionOfAdjacent(closestScissors.getLocation()) != -1)
                    return CAPTURE;

                Location goal = rockLoc;
                if (closestScissors != null
                        && distanceTo(closestScissors.getLocation()) < distanceTo(rockLoc))
                    goal = closestScissors.getLocation();

                return goTo(goal, arena);
            }

            //----------------------------------
            //Then go bid  (31 magic number)
            //----------------------------------
            else if (paper != null && shouldBid && getNumPapers() <= 42 && paper.getLocation() != null) {
                if (isInCenter()) {
                    shouldBid = false;
                    return BID + paper.getCurrentTopBid() + 1;
                } else if (closestScissors != null
                        && this.directionOfAdjacent(closestScissors.getLocation()) != -1)
                    return CAPTURE;

                Location goal = phaseTwoLocs.get(0);

                for (Location loc : phaseTwoLocs) {
                    if (loc != null && distanceTo(loc) < distanceTo(goal))
                        goal = loc;
                }

                return goTo(goal, arena);
            }

            //----------------------------------
            //Then get closest (rock or scissor)
            //----------------------------------
            else if (closestScissors != null && closestScissors.getLocation() != null && rockLoc!=null) {
//            if (this.directionOfAdjacent(closestScissors.getLocation()) != -1) {
//                return CAPTURE;
//            } else
//                return goTo(closestScissors.getLocation(), arena);

                if (directionOfAdjacent(rockLoc) != -1)
                    return MINE + directionOfAdjacent(rockLoc);
                else if (this.directionOfAdjacent(closestScissors.getLocation()) != -1)
                    return CAPTURE;

                Location goal = rockLoc;
                if (distanceTo(closestScissors.getLocation()) < distanceTo(rockLoc) * 100)
                    goal = closestScissors.getLocation();

                return goTo(goal, arena);

            }


            //Otherwise, do nothing
            else
                return REST;
//        } catch(FileNotFoundException ex){
//            System.out.println("Poo!");
//            return REST;
//        }
    }

}
