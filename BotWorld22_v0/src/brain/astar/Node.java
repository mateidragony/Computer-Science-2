package brain.astar;


public class Node {

    private final int c, r;
    private final int initialDirection;


    public Node(int r, int c, int dir){
        this.c = c;
        this.r = r;
        initialDirection = dir;
    }

    public int getR() {
        return r;
    }
    public int getC() {
        return c;
    }
    public int getInitialDirection() {
        return initialDirection;
    }



    @Override
    public String toString(){
        return "(R:"+r+", C:"+c+", Dir: "+initialDirection+")";
    }
}
