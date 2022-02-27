import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class CoolVisual extends JPanel implements MouseListener, KeyListener {


    private final int width = 1000, height = 600;

    private boolean drawGarbage = true;

    private int x, y, t;
    private boolean increasing;
    private Toolkit toolkit;
    private double cvx, cvy, cvXVel, cvYVel;

    private final Image cv1;
    private final Image cv2;
    private Color color;

    private ArrayList<Line2D.Double> cv1Lines;

    private char charTyped;

    public CoolVisual(){
        increasing = true;
        setPreferredSize(new Dimension(width,height));
        this.addMouseListener(this);
        this.addKeyListener(this);
        toolkit = Toolkit.getDefaultToolkit();

        cvXVel = (int)(Math.random()*10)+5;
        cvYVel = (int)(Math.random()*10)+5;

        cv1 = toolkit.getImage("Miscellaneous Programs/bruh_moment_2.png");
        cv2 = toolkit.getImage("Miscellaneous Programs/dvdVideo.png");

        cv1Lines = new ArrayList<>();
        cv1Lines.add(new Line2D.Double(0,0,0,0));
    }

    public void paintComponent(Graphics g){

        if(drawGarbage) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);

            g.setColor(Color.white);
            g.drawLine(0, height / 2, width, height / 2);
            g.drawLine(60, 0, 60, height);


            y = (int) (100* Math.sin(x / 15.0));

            g.setColor(Color.red);
            g.fillOval(x + 60 - 5, height / 2 - y - 5, 10, 10);

            for (int i = x; i >= 0; i--) {
//            g.setColor(Color.yellow);
//            int yy1 = height/2-5-(int)(100*Math.sin((x-i)/15.0));
//            int yy2 = height/2-5-(int)(100*Math.sin((x-1-i)/15.0));
//            g.drawLine(x+60-i,yy1,x+60-1-i,yy2);
                g.setColor(Color.red);
                g.fillRect(x + 60 - i, height / 2 - 5 - (int) (100*Math.sin((x - i) / 15.0)), 2, 2);

            }


            int circSize = 100;

            t = increasing ? t + 1 : t - 1;
            if (t > circSize)
                increasing = false;
            if (t < 0)
                increasing = true;

            g.setColor(Color.green);
            g.drawOval(500 - t / 2, 100, t, circSize);
            g.drawOval(500 - circSize / 2, 100 + circSize / 2 - t / 2, circSize, t);


            x++;
        }else {

            Graphics2D g2d = (Graphics2D)g;

            g.setColor(Color.black);
            g.fillRect(0,0,width,height);

            g.setColor(Color.red);
            //g2d.draw(new Line2D.Double(cvx,cvy,cv1Lines.get(cv1Lines.size()-1).x2,cv1Lines.get(cv1Lines.size()-1).y2));

            for(Line2D.Double line: cv1Lines){
            //    g2d.draw(line);
            }

            animateAndDrawCV(g2d);
        }

        g.setColor(Color.white);
        g.drawString(""+charTyped, 10,10);
    }


    public void animateAndDrawCV(Graphics g){

        int cvW = 112;
        int cvH = 60;


        if(cvx < 0){
            color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
            cvx=0;
            cvXVel*=-1;
            cv1Lines.add(new Line2D.Double(cv1Lines.get(cv1Lines.size()-1).x2,cv1Lines.get(cv1Lines.size()-1).y2,cvx,cvy));
        } else if(cvx+cvW > width){
            color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
            cvx = width-cvW;
            cvXVel*=-1;
            cv1Lines.add(new Line2D.Double(cv1Lines.get(cv1Lines.size()-1).x2,cv1Lines.get(cv1Lines.size()-1).y2,cvx,cvy));
        }

        if(cvy < 0){
            color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
            cvy=0;
            cvYVel*=-1;
            cv1Lines.add(new Line2D.Double(cv1Lines.get(cv1Lines.size()-1).x2,cv1Lines.get(cv1Lines.size()-1).y2,cvx,cvy));
        } else if(cvy+cvH > height){
            color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
            cvy = height-cvH;
            cvYVel*=-1;
            cv1Lines.add(new Line2D.Double(cv1Lines.get(cv1Lines.size()-1).x2,cv1Lines.get(cv1Lines.size()-1).y2,cvx,cvy));
        }

        cvx+=cvXVel;
        cvy+=cvYVel;

        g.setColor(color);
        g.fillRect((int)cvx,(int)cvy,cvW,cvH);
        g.drawImage(cv2,(int)cvx,(int)cvy,cvW,cvH,this);

    }



    public static void main(String[] args){
        JFrame myFrame = new JFrame("Cool Visual!");
        CoolVisual cv = new CoolVisual();
        myFrame.add(cv);
        myFrame.setSize(cv.getPreferredSize());
        myFrame.setUndecorated(true);
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocation(20,20);
        myFrame.addKeyListener(cv);

        Timer t = new Timer(1000/60, e -> myFrame.getComponent(0).repaint());
        t.start();
    }


    public void mouseClicked(MouseEvent e) {
        System.exit(1);
    }
    public void mousePressed(MouseEvent e) {    }
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }

    public void keyTyped(KeyEvent e) {
        charTyped = e.getKeyChar();

        if(charTyped == ' '){
            drawGarbage = !drawGarbage;
        }
    }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}


//Under Pressure
//Knigh laugh whne hits corner
//Shreksophone