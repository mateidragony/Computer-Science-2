import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class CoolVisual extends JPanel {

    public CoolVisual(){
        setPreferredSize(new Dimension(700,500));
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,700,500);
    }

    public static void main(String[] args){
        JFrame myFrame = new JFrame("Cool Visual!");
        CoolVisual cv = new CoolVisual();
        myFrame.add(cv);
        myFrame.setSize(cv.getPreferredSize());
        myFrame.setUndecorated(true);
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.addMouseListener(new MouseAdapter(){
            public void mouseClicked(ActionEvent e){
                System.exit(1);
            }
        });

        Timer t = new Timer(1000/60, e -> myFrame.getComponent(0).repaint());
        t.start();
    }

}
