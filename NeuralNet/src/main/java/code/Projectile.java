package code;

import java.awt.*;

public class Projectile {


    private int x,y,yVel;


    public Projectile(int x, int y){

        this.x = x;
        this.y = y;
        yVel = -10;

    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getHitBox(){
        return new Rectangle(x,y,5,20);
    }


    public void animate(){
        y+=yVel;
    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x,y,5,20);
    }

}
