package Boxman;

import java.awt.*;
import java.util.ArrayList;

public class Player {

    private int playerX;
    private int playerY;
    private final int width, height;
    private boolean isDead;

    private int yVel;
    private int ground;

    int mapWidth;
    int mapHeight;

    public Player(Dimension size){
        playerX = 1;

        mapWidth = size.width;
        mapHeight = size.height;

        width = 20;
        height = 20;

        ground = 19;
    }


    //Accessors
    public int getX(){return playerX;}
    public int getY(){return playerY;}

    //Modifiers
    public void setX(int c){playerX=c;}
    public void setY(int c){playerY=c;}

    public void jump(){yVel = -5;}
    public boolean isOnGround(){return playerY >= ground-1;}
    public boolean isDead(){return isDead;}

    public void determineGround(ArrayList<Block> blocks){

        ground = 19;

        for(Block b: blocks){
            if((playerY < b.getY()  && playerX == b.getX()) || b.getType() > 1){
                if(b.getY() <= ground)
                    ground = b.getY();
            }
        }

    }
    public boolean isBlockAboveMe(ArrayList<Block> blocks){

        boolean blockAbove = false;

        for(Block b:blocks){
            if(b.getY() == playerY-1 && b.getX() == playerX){
                blockAbove = true;
                break;
            }
        }

        return blockAbove;

    }


    public Rectangle getHitBox(){return new Rectangle(playerX,playerY,width,height);}

    public void animate(boolean left, boolean right, boolean jump, ArrayList<Block> blocks){

        int prevX = playerX;
        int prevY = playerY;


        //gravity
        determineGround(blocks);
        if(isOnGround() && yVel>0) {
            playerY = ground-1;
            yVel=0;
        }
        else {
            yVel++;
            playerY+=yVel/2;

            if(yVel >= 3)
                yVel = 2;
        }


        //handle player movement
        if(left && getX()>1)
            setX(getX()-1);
        else if(right && getX()<27)
            setX(getX()+1);
        if(jump && isOnGround() && !isBlockAboveMe(blocks))
            jump();


        //Block collision
        for(Block b: blocks){
            if(getHitBox().contains(b.getHitBox())){
                playerX = prevX;
                if(yVel<0)
                    playerY = prevY;
            }

            //Am I in a block!?
            if(getHitBox().contains(b.getHitBox()) && b.getHitBox().contains(prevX,prevY)) {
                playerY--;
                isDead = true;
            }
        }
    }

    public void draw(Graphics g){

        //draw player
        g.setColor(Color.RED);
        g.fillRect(playerX*20,playerY*20,width,height);

    }
}
