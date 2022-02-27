package brain.pathfinding;

import actor.GameObject;
import actor.Rock;
import actor.Wall;
import grid.Location;

import java.util.ArrayList;

public class Pathfinder {

    Square[][] squareGrid = new Square[24][24];
    GameObject[][] arena;

    ArrayList<Path> paths;


    public Pathfinder(GameObject[][] theArena){
        arena = theArena;
        initSquareGrid();
        paths = new ArrayList<>();
    }


    public void initSquareGrid(){

        for(int i=0; i<squareGrid.length; i++){
            for(int j=0; j<squareGrid[0].length; j++){

                GameObject thing = arena[i][j];

                if(thing instanceof Rock || thing instanceof Wall){
                    squareGrid[i][j] = new Square(10000, new Location(i,j));
                } else {
                    squareGrid[i][j] = new Square(-1, new Location(i,j));
                }
            }
        }
    }


    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void findBestPath(Location startLoc, Location endLoc, Path path, int value){
        if(!startLoc.equals(endLoc) && value < 10) {

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    int x = startLoc.getCol() + i;
                    int y = startLoc.getRow() + j;
                    Location loc = new Location(x, y);

                    if (loc.isValidLocation() && !loc.equals(startLoc)
                            && (squareGrid[x][y].getValue() == -1
                            || value <= squareGrid[x][y].getValue())) {
                        squareGrid[x][y].setValue(value);
                        Path tempPath = path.clonePath();
                        tempPath.add(squareGrid[x][y]);
                        paths.add(tempPath);

                        //System.out.println("x: "+ x+ " , y: "+y+" value: "+value);

                        findBestPath(loc,endLoc,tempPath,value+1);
                    }
                }
            }
        }
    }
}
