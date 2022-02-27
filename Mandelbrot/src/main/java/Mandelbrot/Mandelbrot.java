/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.Mandelbrot;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Scanner;
import javax.swing.*;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author 22cloteauxm
 */
public class Mandelbrot extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
    
    private final int MAX_PERMUTATIONS = 255;

    private static int size = 700;
    private double xStart = -1.5;
    private double yStart = -1;
    private double scale = 2;
    private int mouseX, mouseY;
    
    private BufferedImage image;
    
    public Mandelbrot(){
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        //this.addMouseWheelListener(this);
        this.setPreferredSize(new Dimension(size,size));
        
        MandelbrotDrawer drawer = new MandelbrotDrawer();
        Thread drawThread = new Thread(drawer);
        drawThread.start();
        
        image = drawer.getImage();
    }
    
    //Mouse Listeners
    public void mouseClicked(MouseEvent e) {
        scale/=1.5;
        xStart = xStart + scale * (mouseX-size/4) / size ;
        yStart = yStart + scale * ((size-mouseY)-size/4) / size ;
        
        MandelbrotDrawer drawer = new MandelbrotDrawer();
        Thread drawThread = new Thread(drawer);
        drawThread.start();
        
        //Graphics2D g = (Graphics2D)image.getGraphics();
                
        image = drawer.getImage();
    }
    public void mouseWheelMoved(MouseWheelEvent e){
        scale/=1.000000000005;
        xStart = xStart + scale * (mouseX-size/4) / size ;
        yStart = yStart + scale * ((size-mouseY)-size/4) / size ;
        
        MandelbrotDrawer drawer = new MandelbrotDrawer();
        Thread drawThread = new Thread(drawer);
        drawThread.start();
        
        //Graphics2D g = (Graphics2D)image.getGraphics();
                
        image = drawer.getImage();
    }    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}   
    public void mousePressed(MouseEvent e) {}    
    public void mouseReleased(MouseEvent e) {}     
    
    // MouseMotionListener
    public void mouseMoved(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }    
    public void mouseDragged(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }  
    
    
    
    public BufferedImage createBuffImageMatei(){        
        BufferedImage myImage = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        
        for(int pX=0;pX<size;pX++){
            for(int pY=0;pY<size;pY++){
                
                double x = xStart + scale * pX / size ;
		double y = yStart + scale * pY / size ;

                Complex complex = new Complex(x,y);
                Complex initialComplex = complex;

                for(int i=0; i<MAX_PERMUTATIONS; i++)
                {
                    if(complex.abs()>=2 | i==MAX_PERMUTATIONS-1)
                    {
                        int color = Color.getHSBColor((1-(i/(float)(MAX_PERMUTATIONS)))-.3f,1.0f,1.0f).getRGB();

                        if(i == MAX_PERMUTATIONS-1)
                            color = Color.BLACK.getRGB();
                        
                        myImage.setRGB(pX,size-1-pY,color);
                        break;
                    } else {
                        complex = complex.multiply(complex).add(initialComplex);
                    }
                }
            }
        }
        return myImage;
    }
    
    public void paint(Graphics g){       
        g.drawImage(image, 0,0,this);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        
        frame.getContentPane().add(new Mandelbrot());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(size, size);
        frame.setLocation(10,10);
        frame.setVisible(true);
        frame.setResizable(true);
        
        javax.swing.Timer t = new javax.swing.Timer(1000/60, new ActionListener() 
        {   
            public void actionPerformed(ActionEvent e) 
            {
                frame.getComponent(0).repaint();
                frame.setSize(frame.getComponent(0).getPreferredSize());
            }
        });
        t.start();


        Scanner s = new Scanner(System.in);
    }
    

    private class MandelbrotDrawer implements Runnable {
        
        private BufferedImage myImage;
        
        public MandelbrotDrawer(){
            myImage = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        }
        
        public BufferedImage getImage(){return myImage;}
        
        public void run(){        
            for(int pX=0;pX<size;pX++){
                for(int pY=0;pY<size;pY++){

                    double x = xStart + scale * pX / size ;
                    double y = yStart + scale * pY / size ;

                    Complex complex = new Complex(x,y);
                    Complex initialComplex = complex;

                    for(int i=0; i<MAX_PERMUTATIONS; i++)
                    {
                        if(complex.abs()>=2 | i==MAX_PERMUTATIONS-1)
                        {
                            int color = Color.getHSBColor((1-(i/(float)(MAX_PERMUTATIONS)))-.3f,1.0f,1.0f).getRGB();

                            if(i == MAX_PERMUTATIONS-1)
                                color = Color.BLACK.getRGB();

                            myImage.setRGB(pX,size-1-pY,color);
                            break;
                        } else {
                            complex = complex.multiply(complex).add(initialComplex);
                        }
                    }
                }
            }
        }
    }
}


//@Author Rishi Rao
//
//  public int createPermutationsRishi(Complex complex,Complex initial, int counter){
//        int count = 0;
//        
//        if(complex.abs() < 2){
//            if(counter < MAX_PERMUTATIONS){
//                Complex nextIteration = complex.multiply(complex).add(initial);
//                count = createPermutationsRishi(nextIteration,initial,counter + 1);
//            } else {
//                count = MAX_PERMUTATIONS;
//            }
//        } else {
//            count = counter;
//        }
//        
//        return count;
//    }
//
//
//  public BufferedImage createBuffImageRishi(){
//        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
//        
//        for(int i=0; i<size; i++){
//            for(int j=0; j<size; j++){
//                double x = xStart + scale * i / size ;
//		double y = yStart + scale * j / size ;
//                Complex complex = new Complex(x,y);
//                int count = createPermutationsRishi(complex,complex,0);
//                
//                int color = Color.getHSBColor(1/(count/(float)(MAX_PERMUTATIONS))-.3f,.85f,1.0f).getRGB();
//                if(count == MAX_PERMUTATIONS)
//                    color = Color.BLACK.getRGB();
//                image.setRGB(i,size-1-j,color);
//            }
//        }
//        return image;
//    }
//
//End @Author