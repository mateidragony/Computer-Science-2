package brain.mateiBots;

import actor.*;
import brain.astar.AStar;
import grid.Location;

import java.util.ArrayList;

public abstract class AbstractBot extends BotBrain {

    protected GameObject[][] arena;

    public abstract void initForRound();
    public abstract int chooseAction();

    public ArrayList<Scissors> getScissors(GameObject[][] arena){

        ArrayList<Scissors> scissors = new ArrayList<>();

        for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){
                if(arena[r][c] instanceof Scissors)
                    scissors.add((Scissors)arena[r][c]);
            }
        }

        return scissors;
    }
    public ArrayList<Rock> getRocks(GameObject[][] arena){

        ArrayList<Rock> rocks = new ArrayList<>();

        for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){

                if(arena[r][c] instanceof Rock) {
                    rocks.add((Rock)arena[r][c]);
                }
            }
        }

        return rocks;
    }
    public Paper getPaper(GameObject[][] arena){

        Paper paper = null;

        outer: for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){
                if(arena[r][c] instanceof Paper) {
                    paper = (Paper) arena[r][c];
                    break outer;
                }
            }
        }

        return paper;
    }
    public ArrayList<Bot> getEnemies(GameObject[][] arena){

        ArrayList<Bot> bots = new ArrayList<>();

        for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){
                if(arena[r][c] instanceof Bot && !(r==getRow() && c==getCol()))
                    bots.add((Bot)arena[r][c]);
            }
        }

        return bots;

    }
    public ArrayList<Location> getRockLocs(GameObject[][] arena){

        ArrayList<Location> rocks = new ArrayList<>();

        for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){

                if(arena[r][c] instanceof Rock) {
                    rocks.add(new Location(r,c));
                }
            }
        }

        return rocks;

    }
    public Location getPaperLoc(GameObject[][] arena){

         for(int r=0; r<24; r++){
            for(int c=0; c<24; c++){
                if(arena[r][c] instanceof Paper) {
                    return new Location(r,c);
                }
            }
        }

        return null;
    }

    public int distanceTo(Location start, Location end){

        int rowDist = Math.abs(start.getRow()-end.getRow());
        int colDist = Math.abs(start.getCol()-end.getCol());

        return Math.max(rowDist, colDist);
    }
    public int distanceTo(Location end){
        return distanceTo(new Location(getRow(),getCol()),end);
    }

    public Rock getClosestRock(ArrayList<Rock> rocks){
        Rock bestRock = null;
        int shortestDist = 9999;

        for(Rock rock : rocks){
            if (distanceTo(rock.getLocation()) < shortestDist) {
                shortestDist = distanceTo(rock.getLocation());
                bestRock = rock;
            }
        }

        return bestRock;
    }
    public Scissors getClosestScissor(ArrayList<Scissors> scissors){
        Scissors bestScissors = null;
        int shortestDist = 9999;

        for(Scissors scissor : scissors){
            if(distanceTo(scissor.getLocation()) < shortestDist){
                shortestDist = distanceTo(scissor.getLocation());
                bestScissors = scissor;
            }
        }

        return bestScissors;
    }
    public Location getClosestLoc(ArrayList<Location> locs){
        Location bestLoc = null;
        int shortestDist = 9999;

        for(Location loc : locs){
            if (distanceTo(loc) < shortestDist) {
                shortestDist = distanceTo(loc);
                bestLoc = loc;
            }
        }

        return bestLoc;
    }

    public int directionOfAdjacent(Location loc) {

        for(int dir=0; dir<360; dir+=45)
        {
            Location myLoc = new Location(getRow(),getCol());
            Location adj = myLoc.getAdjacentLocation(dir);

            if(adj.equals(loc))
                return dir;

        }
        return -1;
    }

    public int goTo(Location loc, GameObject[][] arena){
        AStar astar = new AStar(arena);

        return astar.pathFind(new Location(getRow(),getCol()), loc);
    }
    public Location getLocation(){return new Location(getRow(),getCol());}

    public boolean isInCenter(){
        return 7 < getRow() && getRow() < 15 && 7 < getCol() && getCol() < 15;
    }
}
