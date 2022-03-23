package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

public class ShooterGame extends JPanel implements KeyListener {


    public static final int width = 600, height = 400;

    private final int playerSize = 30;
    public int playerX=400,playerY;
    public int enemyX=200, enemyY=50;

    private Projectile p = null;

    public int score = 0;

    private NeuralNetworkMultiLevel nn;

    public boolean left,right,shoot;


    public ShooterGame(){

        this.addKeyListener(this);
        try{
            nn = GeneticAlgorithm.loadNN("gameNNTest05");
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }

    }



    public void paintComponent(Graphics g){

        playerY = height-3*playerSize;

        g.setColor(Color.black);
        g.fillRect(0,0,width,height);

        animate();

        if(p!=null)
            p.draw(g);

        g.setColor(Color.white);
        g.drawString("Score: "+score, 20,20);
        g.drawString("MyX: "+playerX, 20,50);

        g.setColor(Color.green);
        g.fillRect(enemyX,enemyY,20,20);


        g.setColor(Color.red);
        g.fillRect(playerX,playerY,playerSize,playerSize);

    }


    public void animate(){
        if(p!=null){
            p.animate();

            if(p.getHitBox().intersects(new Rectangle(200,50,20,20))) {
                p = null;
                score+=500000;
            }

            if(p!=null) {
                if (p.getY() < 0)
                    p = null;
            }

        }



        if(shoot && p==null){
            p = new Projectile(playerX+playerSize/2-2, playerY);
            score += 50000;
        }
        if(left)
            playerX-=5;
        if(right)
            playerX+=5;


        if(playerX<0)
            playerX=0;
        if(playerX+playerSize>width)
            playerX=width-playerSize;

        score+=300-2*Math.abs(playerX-enemyX);



        Matrix m = nn.predict(new double[]{playerX,playerY,enemyX,enemyY});
        java.util.List<Double> out = m.toArray();

        right = out.get(0)>0.5;
        left = out.get(1)>0.5;
        shoot = out.get(2)>0.5;

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
