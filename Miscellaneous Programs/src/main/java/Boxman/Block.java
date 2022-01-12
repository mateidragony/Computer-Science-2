package Boxman;

import java.awt.*;
import java.util.ArrayList;

public class Block {

    private int x,y,w,h;

    //0 is ground any other number is regular block
    private final int type;
    private final Color myColor = Color.black;

    public Block(int x_, int y_, int w_, int h_, int typer){
        x=x_;
        y=y_;
        w=w_;
        h=h_;
        type = typer;
    }

    public int getY(){return y;}
    public int getX(){return x;}
    public int getType(){return type;}

    public Rectangle getHitBox(){return new Rectangle(x,y,w,h);}

    public void draw(Graphics g){
        if(type == 0) {
            g.setColor(myColor);
            g.fillRect(x * 20, y * 20, w, h);
            g.setColor(Color.white);
            g.drawRect(x * 20, y * 20, w, h);
        }
        else if(type == 1){
            g.setColor(new Color(130,80,200));
            g.fillRect(x * 20, y * 20, w, h);
        }
    }


    public boolean blockBelow(ArrayList<Block> blocks){
        boolean blockBelow = false;

        for(Block b: blocks){
            if(b.getX() == x && b.getY() == y+1){
                blockBelow = true;
                break;
            }
        }

        if(y==18 || y==19)
            blockBelow = true;

        return blockBelow;
    }

    public void animate(ArrayList<Block> blocks){
        //myColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));

        if(!blockBelow(blocks) && type == 0)
            y++;
    }

}
