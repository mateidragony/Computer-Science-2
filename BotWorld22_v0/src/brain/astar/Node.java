public class Node{

    private int r,c,d;

    public Node(int row, int col, int dir){

       r=row;
       c=col;
       d=dir;

    }

    public int getR(){
        return r;
    }

    public int getC(){
        return c;
    }

    public int getInitialDirection(){
        return d;
    }


}