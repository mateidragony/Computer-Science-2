package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ShooterGame extends JPanel implements KeyListener {


    public static final int width = 600, height = 400;

    private final int playerSize = 30;
    private int playerX,playerY;

    private Projectile p = null;

    private int score = 0;


    private boolean left,right,shoot;


    public ShooterGame(){

        this.addKeyListener(this);

    }



    public void paintComponent(Graphics g){

        playerY = height-3*playerSize;

        g.setColor(Color.black);
        g.fillRect(0,0,width,height);

        g.setColor(Color.white);
        g.drawString("Score: "+score, 20,20);

        if(p!=null){
            p.animate();
            p.draw(g);

            if(p.getHitBox().intersects(new Rectangle(200,50,20,20))) {
                p = null;
                score+=1000;
            }

            if(p!=null) {
                if (p.getY() < 0)
                    p = null;
            }

        }



        if(shoot && p==null)
            p = new Projectile(playerX+playerSize/2-2, playerY);
        if(left)
            playerX-=5;
        if(right)
            playerX+=5;


        if(playerX<0)
            playerX=0;
        if(playerX+playerSize>width)
            playerX=width-playerSize;




        g.setColor(Color.green);
        g.fillRect(200,50,20,20);


        g.setColor(Color.red);
        g.fillRect(playerX,playerY,playerSize,playerSize);

    }



    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            right = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            left = true;
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            shoot = true;
    }
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            right = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            left = false;
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            shoot = false;
    }







    public static void main(String[] args){


        ShooterGame sg = new ShooterGame();

        JFrame myFrame = new JFrame("Game");
        myFrame.setSize(width+15,height);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.getContentPane().add(sg);
        myFrame.addKeyListener(sg);

        Timer t = new Timer(1000/60, e -> myFrame.getContentPane().getComponent(0).repaint());
        t.start();

    }
}
