package brain.mateiBots;

import actor.BlockedLocation;
import actor.GameObject;
import actor.Scissors;
import grid.Location;

import java.util.ArrayList;

public class PeugeotBot extends AbstractBot{


    ArrayList<Location> tikkyTakkyToe;
    private boolean shouldBid = true;
    private boolean mineTime = true;
    
    private Location closestScissors;
    private Location closestRock;

    private int tR, tP, tS;

    public PeugeotBot(){
        initTikkyTakkyToe();
    }

    @Override
    public void initForRound() {
        shouldBid = true;
        mineTime = true;
        initTikkyTakkyToe();
        closestScissors = null;
    }

    //init locations for tik tac toe
    public void initTikkyTakkyToe(){
        tikkyTakkyToe = new ArrayList<>();
        tikkyTakkyToe.add(new Location(3,8));
        tikkyTakkyToe.add(new Location(6,3));
        tikkyTakkyToe.add(new Location(15,3));
        tikkyTakkyToe.add(new Location(20,6));
        tikkyTakkyToe.add(new Location(20,15));
        tikkyTakkyToe.add(new Location(17,20));
        tikkyTakkyToe.add(new Location(8,20));
        tikkyTakkyToe.add(new Location(6,20));
    }

    @Override
    public int chooseAction() {

        GameObject[][] arena = getArena();

        closestRock = getClosestLoc(getRockLocs(arena));
        Location prevScissor = closestScissors;

        //try catch because i didn't want to constantly check == null
        try{
            closestScissors = getClosestScissor(getScissors(arena)).getLocation();
        } catch(NullPointerException ignored){}

        //if the paper moved, bid
        if(getPaper(arena)==null)
            shouldBid = true;


        //troubleshooting
        if(getMoveNumber() == 499){
            tR+=getNumRocks();
            tP+=getNumPapers();
            tS+=getNumScissors();
        }



        //-----------------------------
        //First get Tic-Tac-Toe
        //-----------------------------
        if(!tikkyTakkyToe.isEmpty()){

            if(closestScissors!=null){
                if(directionOfAdjacent(closestScissors)!=-1)
                    return CAPTURE;
                //else if(distanceTo(closestScissors)*1.25 < distanceTo(tikkyTakkyToe.get(0)))
                //    return goToScissors(scissors,arena); //return goTo(closestScissors, arena);
            }


            if(this.directionOfAdjacent(tikkyTakkyToe.get(0))!=-1)
                tikkyTakkyToe.remove(0);

            if(tikkyTakkyToe.isEmpty())
                return REST;
            else
                return goTo(tikkyTakkyToe.get(0), arena);

        }


        //-----------------------------
        //Get Some Rocks
        //-----------------------------
        if(mineTime && closestRock != null){

            if(closestScissors!=null) {
                if (directionOfAdjacent(closestScissors) != -1)
                    return CAPTURE;
                else if(distanceTo(closestScissors) < distanceTo(closestRock))
                    return goToScissors(closestScissors,prevScissor,arena); //return goTo(closestScissors, arena);
            }

            if(getNumRocks() > 55)
                mineTime = false;

            if(directionOfAdjacent(closestRock)!=-1)
                return MINE + directionOfAdjacent(closestRock);
            else
                return goTo(closestRock,arena);

        }


        //-----------------------------
        //Go for Scissors If in center and not bid, bid
        //-----------------------------
        if(closestScissors!=null){

            if(directionOfAdjacent(closestScissors)!=-1)
                return CAPTURE;
            else if(shouldBid && isInCenter()){
                shouldBid = false;
                return 1+BID;
            }
            else
                return goToScissors(closestScissors,prevScissor,arena); //return goTo(closestScissors, arena);
        }

        //Else rest
        return REST;
    }

    public Location scissorsNextMove(Location scissors, Location prevScissors, GameObject[][] arena){

        if(prevScissors.getDirectionToward(scissors) == -1)
            return scissors;
        else {

            Location goal = scissors.getAdjacentLocation(prevScissors.getDirectionToward(scissors));

            if (goal.isValidLocation() && !(arena[goal.getRow()][goal.getCol()] instanceof BlockedLocation))
                return goal;

            else
                return scissors;
        }



    }
    public int goToScissors(Location scissors, Location prevScissors, GameObject[][] arena){


        //return goTo(scissors,arena);

        try {
            Location goal = scissorsNextMove(scissors, prevScissors, arena);

            return goTo(goal, arena);

        } catch(NullPointerException ex){
            return REST;
        }

    }

}
