package brain.pathfinding;

import java.util.ArrayList;

public class Path {

    private ArrayList<Square> myPath;
    private boolean isValid;

    public Path(){
        myPath = new ArrayList<>();
    }

    public ArrayList<Square> getPath(){return myPath;}

    public void add(Square sq){myPath.add(sq);}

    public boolean isValid(){return isValid;}
    public void setValid(boolean valid){isValid = valid;}

    public Path clonePath(){return this;}
}
