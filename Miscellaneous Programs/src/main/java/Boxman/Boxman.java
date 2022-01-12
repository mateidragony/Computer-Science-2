package Boxman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Boxman extends JPanel implements KeyListener, MouseListener, MouseMotionListener {


    private static JFrame myFrame;
    private static Timer t;

    private final int width = 600;
    private final int height = 450;

    private int mouseX, mouseY;
    private int blocksLeft = 200;

    private Player player;
    private ArrayList<Block> blocks;

    private boolean left,right,jump, placeBlock;

    private int mapR,mapG,mapB;

    private int blockTimer = 80;
    private int gameTimer = 700;

    public Boxman(){
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);

        player = new Player(size);
        blocks=new ArrayList<>();
        blocks.add(new Block(0,19,100*20,60,394523623));

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        mapR = (int)(Math.random()*255);
        mapG = (int)(Math.random()*255);
        mapB = (int)(Math.random()*255);

    }

    public void paintComponent(Graphics g) {

        drawWorld(g);

        player.animate(left,right,jump,blocks);
        player.draw(g);

        for(Block b: blocks){
            b.animate(blocks);
            b.draw(g);
        }

        if(placeBlock)
            blocks.add(new Block(mouseX/20,mouseY/20,20,20,1));

        handleDeathAndWin(g);

        if(blockTimer>0)
            blockTimer--;
        else if(blocksLeft>0){
            blockTimer = 2;
            for(int i=0; i<1; i++) {
                blocks.add(new Block(1 + (int) (Math.random() * 27), -2, 20, 20, 0));
                blocksLeft--;
            }
        }


        if(blockTimer>2){
            g.setFont(new Font(Font.SERIF,Font.BOLD,30));
            g.setColor(Color.black);
            g.drawString("The rules of this game are simple.",80,50);
            g.drawString("Don't get squashed. Good luck!",90,100);
        } else if (blocksLeft > 0){
            g.setFont(new Font(Font.SERIF,Font.BOLD,20));
            g.setColor(Color.black);
            g.drawString("Blocks remaining: "+blocksLeft,60,50);
        }

        gameTimer--;
    }



    public void drawWorld(Graphics g){
        //draw bg
        g.setColor(new Color(mapR,mapG,mapB,170));
        g.fillRect(0,0,width,height);

        //draw floor
        g.setColor(new Color(mapR/3,mapG*7/10,mapB*7/10,170));
        g.fillRect(0,height-70,width,100);

        //draw walls
        g.setColor(new Color(mapR/10,mapG*4/10,mapB*7/10,170));
        g.fillRect(0,0,20,height-70);
        g.fillRect(width-40,0,40,height-70);
    }
    public void handleDeathAndWin(Graphics g){
        if(player.isDead()) {
            g.setColor(Color.black);
            g.fillRect(0,0,width,height);
            g.setColor(Color.white);
            g.setFont(new Font(Font.SERIF,Font.BOLD,90));
            g.drawString("JOO LOSE!!",50,200);

            g.setFont(new Font(Font.SERIF,Font.BOLD,30));
            g.drawString("Press Space to Play Again",130,300);
        }
        else if(gameTimer <= 0) {
            g.setColor(Color.white);
            paintTextWithOutline(g,"YOU WIN!!" ,110, 80, 70, 4.0f);
            paintTextWithOutline(g,"Press Space to Play Again" ,170, 150, 20, 2.0f);
            player.setY(-1000);
        }
    }
    public void resetGame(Dimension size){
        player = new Player(size);
        blocks=new ArrayList<>();
        blocks.add(new Block(0,19,100*20,60,394523623));

        mapR = (int)(Math.random()*255);
        mapG = (int)(Math.random()*255);
        mapB = (int)(Math.random()*255);

        blockTimer = 80;
        gameTimer = 700;
        blocksLeft = 200;
    }

    public void paintTextWithOutline(Graphics g, String text, int x, int y, int fontSize, float strokeSize) {

        Color outlineColor = Color.white;
        Color fillColor = Color.black;
        BasicStroke outlineStroke = new BasicStroke(strokeSize);

        Graphics2D g2 = (Graphics2D) g;

        // remember original settings
        Color originalColor = g2.getColor();
        Stroke originalStroke = g2.getStroke();
        RenderingHints originalHints = g2.getRenderingHints();

        setFont(new Font(Font.SERIF,Font.BOLD,fontSize));

        // create a glyph vector from your text
        GlyphVector glyphVector = getFont().createGlyphVector(g2.getFontRenderContext(), text);

        for(int i=0; i<text.length(); i++){
            double glyphX = glyphVector.getGlyphPosition(i).getX();
            glyphVector.setGlyphPosition(i,new Point2D.Double(glyphX+x,y));
        }
        // get the shape object
        Shape textShape = glyphVector.getOutline();


        // activate anti aliasing for text rendering (if you want it to look nice)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(outlineColor);
        g2.setStroke(outlineStroke);
        g2.draw(textShape); // draw outline

        g2.setColor(fillColor);
        g2.fill(textShape); // fill the shape

        // reset to original settings after painting
        g2.setColor(originalColor);
        g2.setStroke(originalStroke);
        g2.setRenderingHints(originalHints);

    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if((key==KeyEvent.VK_LEFT || key==KeyEvent.VK_A))
            left=true;
        else if((key==KeyEvent.VK_RIGHT || key==KeyEvent.VK_D))
            right=true;

        if((key==KeyEvent.VK_UP || key==KeyEvent.VK_W))
            jump=true;
        if(key==KeyEvent.VK_SPACE) {
            jump = true;

            if(gameTimer <= 0 || player.isDead()){
                resetGame(new Dimension(width,height));
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_R)
            resetGame(new Dimension(width,height));

    }
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if((key==KeyEvent.VK_LEFT || key==KeyEvent.VK_A))
            left=false;
        else if((key==KeyEvent.VK_RIGHT || key==KeyEvent.VK_D))
            right=false;

        if((key==KeyEvent.VK_W || key==KeyEvent.VK_UP))
            jump=false;
        if(key==KeyEvent.VK_SPACE)
            jump = false;
    }





    public static void startGame(){
        Boxman boxman = new Boxman();

        myFrame = new JFrame();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        myFrame.setSize(boxman.getPreferredSize());
        myFrame.add(boxman);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.addKeyListener(boxman);
        myFrame.addMouseListener(boxman);
        myFrame.addMouseMotionListener(boxman);

        t = new Timer(1000/20, e -> myFrame.getComponent(0).repaint());

        t.start();
    }

    public static void main(String[] args){
        startGame();
    }


    public void mouseClicked(MouseEvent e) {
       blocks.add(new Block(mouseX/20,mouseY/20,20,20,0));
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {mouseX = e.getX(); mouseY=e.getY();}
}
