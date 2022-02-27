package brain.mateiBots;

import actor.BlockedLocation;
import actor.GameObject;
import actor.Wall;
import grid.Location;

public class Honeycomber extends AbstractBot{

    private boolean getRocks;
    private GameObject[][] arena;

    @Override
    public void initForRound() {
        getRocks = true;
    }

    @Override
    public int chooseAction() {

        setName("R:"+getNumRocks());

        int move = REST;


        arena = getArena();

        if(getNumRocks() >= 200)
            getRocks = false;

        if(getRocks) {
            Location closest = this.getClosestLoc(this.getRockLocs(arena));

            if(directionOfAdjacent(closest)!=-1)
                move = MINE+ directionOfAdjacent(closest);

            else
                move = goTo(closest,arena);
        }
        else if(buildHere()!=-1){
            move = BUILD + buildHere();
        }
        else {
            Location closest = this.getClosestLoc(this.getRockLocs(arena));

            if(directionOfAdjacent(closest)!=-1)
                move = MINE+ directionOfAdjacent(closest);
            else
                move = randomMove();
        }

        setName("R:"+getNumRocks()+"Move: "+move);

        return move;

    }
    public int randomMove(){

        for(int i=0; i<20; i++){

            int randomDirection = (int)(Math.random()*8)*45;
            if(getLocation().getAdjacentLocation(randomDirection).isValidLocation()
                    && !(getLocation().getAdjacentLocation(randomDirection) instanceof BlockedLocation)){
                return randomDirection;
            }

        }

        return REST;
    }
    public int buildHere(){
        for(int dir=0; dir<360; dir+=90){

            Location loc = getLocation().getAdjacentLocation(dir);

            if(loc.isValidLocation() && !(arena[loc.getRow()][loc.getCol()] instanceof BlockedLocation))
                return dir;
        }
        return -1;
    }

}
