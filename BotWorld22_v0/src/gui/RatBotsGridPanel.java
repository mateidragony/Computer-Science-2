package gui;

/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Julie Zelenski
 * @author Cay Horstmann
 */



//import info.gridworld.grid.Grid;
//import info.gridworld.grid.Location;

import actor.Bot;
import grid.Grid;
import grid.Location;
import grid.RatBotsGrid;
import grid.ScoringNotification;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.*;

/**
 * A <code>GridPanel</code> is a panel containing a graphical display of the
 * grid occupants. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */

public class RatBotsGridPanel extends JPanel implements Scrollable
{
    private static final int MIN_CELL_SIZE = 10;
    public static final int DEFAULT_CELL_SIZE = 23;
    public static final int DEFAULT_CELL_COUNT = 24;
    private static final int TIP_DELAY = 1000;

    private RatBotsGrid<?> grid;
    private int numRows, numCols, originRow, originCol;
    private int cellSize; // the size of each cell, EXCLUDING the gridlines
    private boolean toolTipsEnabled;
    private Color backgroundColor = Color.DARK_GRAY;
    private ResourceBundle resources;
    private DisplayMap displayMap;
    private Location currentLocation;
    private Timer tipTimer;
    private JToolTip tip;
    private JPanel glassPane;
    
    
    private RatBotsColorScheme cs = new RatBotsColorScheme(); 
    private boolean showGridLines = true;
    public void toggleShowGridLines() { showGridLines = !showGridLines; repaint();}
    public void changeColorScheme() { cs.changeScheme(); repaint(); }
    
    /**
     * Construct a new GridPanel object with no grid. The view will be
     * empty.
     */
    public RatBotsGridPanel(DisplayMap map, ResourceBundle res)
    {
        displayMap = map;
        resources = res;
        setToolTipsEnabled(true);    
    }

    /**
     * Paint this component.
     * @param g the Graphics object to use to render this component
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g2);
        if (grid == null)
            return;

        Insets insets = getInsets();
        g2.setColor(cs.getBackgroundColor()); 
        g2.fillRect(insets.left, insets.top, numCols * (cellSize + 1) + 1, numRows
                * (cellSize + 1) + 1);
        
//        drawMarkerBackgrounds(g2);
//        drawHighlightLines(g2);
        drawWatermark(g2);
        if(showGridLines) drawGridlines(g2);
        drawOccupants(g2);
        drawScoringNotifications(g2);
        drawTicTacToe(g2);
//        drawWalls(g2);
    }

    /**
     * Draw one occupant object. First verify that the object is actually
     * visible before any drawing, set up the clip appropriately and use the
     * DisplayMap to determine which object to call upon to render this
     * particular Locatable. Note that we save and restore the graphics
     * transform to restore back to normalcy no matter what the renderer did to
     * to the coordinate system.
     * @param g2 the Graphics2D object to use to render
     * @param xleft the leftmost pixel of the rectangle
     * @param ytop the topmost pixel of the rectangle
     * @param obj the Locatable object to draw
     */
    private void drawOccupant(Graphics2D g2, int xleft, int ytop, Object obj)
    {
        Rectangle cellToDraw = new Rectangle(xleft, ytop, cellSize, cellSize);

        // Only draw if the object is visible within the current clipping
        // region.
        if (cellToDraw.intersects(g2.getClip().getBounds()))
        {
            Graphics2D g2copy = (Graphics2D) g2.create();
            g2copy.clip(cellToDraw);
            // Get the drawing object to display this occupant.
                if(obj instanceof Bot)
                {
                Bot r = (Bot)obj;
//                System.out.println(r.getRatBot().getClass());
                obj = r.getRatBot();
                }
                Display displayObj = displayMap.findDisplayFor(obj.getClass());
                displayObj.draw(obj, this, g2copy, cellToDraw);
            g2copy.dispose();
        }
    }
    /**
     * Draw one occupant object. 
     * @param g2 the Graphics2D object to use to render
     * @param xleft the leftmost pixel of the rectangle
     * @param ytop the topmost pixel of the rectangle
     * @param obj the Locatable object to draw
     */
//    private void drawOccupant(Graphics2D g2, int xleft, int ytop, Object obj)
//    {
////        Rectangle cellToDraw = new Rectangle(xleft, ytop, cellSize, cellSize);
////        Graphics2D g2copy = (Graphics2D) g2.create();
////        g2copy.clip(cellToDraw);
//
//        if(obj instanceof Bot) drawRat(g2,xleft,ytop,(Bot)obj);
//        if(obj instanceof Prize) drawMaterial(g2,xleft,ytop,(Prize)obj);
//        if(obj instanceof Tail) drawTail(g2,xleft,ytop,(Tail)obj);
//        //TODO: MORE TO PUT HERE
//        
////        // Get the drawing object to display this occupant.
////        Display displayObj = displayMap.findDisplayFor(obj.getClass());
////        displayObj.draw(obj, this, g2copy, cellToDraw);
////        g2copy.dispose();
//    }
//    
//    private void drawRat(Graphics2D page, int x, int y, Bot rat)
//    {
//        Color myColor = rat.getColor();
//        
//        page.setColor (myColor);
//        //page.setColor(new Color(200,200,200)); //background highlight
//        page.fillRect(x+2,y+2,cellSize-4,cellSize-4);
//        
//        page.setColor (cs.getRatAliveColor());
//        if(rat.getEnergyLevel() == 0)
//            page.setColor(cs.getRatDeadColor());
//        page.fillOval (x+6, y+5, 5, 5);             //right ear
//        page.fillOval (x+15, y+5, 5, 5);            //left ear
//        page.fillOval(x+7, y+ 6, 12, 12 );
//        page.fillOval (x+9, y+10, 8, 12);            //eye 
//        page.drawLine (x+6, y+20, x+9, y+20);     //bottom
//        page.drawLine (x+16, y+20, x+19, y+20);    //top
//        page.drawLine (x+6, y+18, x+8, y+18);     //bottom
//        page.drawLine (x+17, y+18, x+19, y+18);    //top
//        page.drawLine (x+6, y+16, x+8, y+16);     //bottom
//        page.drawLine (x+17, y+16, x+19, y+16);    //top
//        
//        page.setColor (myColor);
//        page.fillOval (x+10, y+10, 2, 2);             //eye
//        page.fillOval (x+14, y+10, 2, 2);            //eye         
//        
//        
//        
//        if(rat.getEnergyLevel() == 0)
//        {
//            page.setColor(cs.getRatAliveColor());
//            page.drawLine(x,y,x+cellSize,y+cellSize);
//            page.drawLine(x, y+cellSize, x+cellSize, y);
//        }
//        
//    } //-- end of drawRat() --
// 
//    private void drawMaterial(Graphics2D g2, int xLeft, int yTop, Prize m)
//    {
////        Color myColor = m.getColor();
//        g2.setColor(cs.getMaterialColor());
//        final int PADDING = 6;
//        g2.drawRect(xLeft+PADDING, yTop+PADDING, 
//                cellSize-2*PADDING, cellSize-2*PADDING);
////        g2.setColor(Color.BLACK);
//        g2.drawString(m.getType()+"",xLeft+cellSize/2-1,yTop+cellSize/2+5);
//
//    }
//    private void drawTail(Graphics2D g2, int xLeft, int yTop, Tail t)
//    {
//        Color myColor = t.getColor();
//        g2.setColor(myColor);
//        final int PADDING = 2;
//        g2.fillRect(xLeft+PADDING, yTop+PADDING, 
//                cellSize-2*PADDING, cellSize-2*PADDING);
//        
//    }
//    private void drawDestination(Graphics2D g2, int xLeft, int yTop, Destination d)
//    {
////        Color myColor = d.getColor();
//        g2.setColor(cs.getDestinationColor());
//        final int PADDING = 5;
//        for(int radius=2; radius<(cellSize-PADDING)/2; radius +=2)
//        {
//            int xx = xLeft + cellSize/2 - radius;
//            int yy = yTop + cellSize/2 - radius;
//            g2.drawOval(xx, yy, radius*2, radius*2);
//        }
//
//        int special = 10-(RatBotWorld.getMoveNum()/3)%9;
//        g2.setColor(cs.getDestinationHighlight());
//            int xx = xLeft + cellSize/2 - special;
//            int yy = yTop + cellSize/2 - special;
//            g2.drawOval(xx, yy, special*2, special*2);
//        
//    }
    
//    private void drawMarker(Graphics2D g2, int xLeft, int yTop, Marker m)
//    {
////        g2.setColor(m.getColor());
////        Font font = g2.getFont();
////        Font newFont = font.deriveFont((float)DEFAULT_CELL_SIZE);
////        g2.setFont(newFont);
////        g2.drawString(String.valueOf(m.getValue()), xLeft, yTop+DEFAULT_CELL_SIZE);
//        
//        
//        
//        String numberText = String.valueOf(m.getValue());
////        g2.setPaint(m.getColor());
//////        g2.setStroke(new BasicStroke(5));
////        g2.fillRect(xLeft, yTop, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
//        g2.setPaint(Color.BLACK);
//        
//        final int MARKER_FONT_SIZE = DEFAULT_CELL_SIZE-4;
//        g2.setFont(new Font("Arial", Font.BOLD, MARKER_FONT_SIZE));
//        FontRenderContext frc = g2.getFontRenderContext();
//        Rectangle2D bounds = g2.getFont().getStringBounds(numberText, frc);
//        float centerX = xLeft + DEFAULT_CELL_SIZE / 2;
//        float centerY = yTop + DEFAULT_CELL_SIZE / 2;
//        float leftX = centerX - (float) bounds.getWidth() / 2;
//        LineMetrics lm = g2.getFont().getLineMetrics(numberText, frc);
//        float baselineY = centerY + lm.getAscent()/2; 
//        g2.drawString(numberText, leftX, baselineY);
//        
//    }
    /**
     * Draws the occupants of the grid.
     * @param g2 the graphics context
     */
    private void drawOccupants(Graphics2D g2)
    {
//        g2.setFont(new Font(30));
        // Look at all grid locations.
        for (int r = 0; r < grid.getNumRows(); r++)
        {
            for (int c = 0; c < grid.getNumCols(); c++)
            {
                // If there's an object at this location, draw it.
                Location loc = new Location(r, c);
                int xleft = colToXCoord(loc.getCol());
                int ytop = rowToYCoord(loc.getRow());
                
                if (grid.get(loc) != null) 
                {
                    drawOccupant(g2, xleft, ytop, grid.get(loc));
                }
            }
        }
    }
//    private void drawMarkerBackgrounds(Graphics2D g2)
//    {
//        // Look at all grid locations.
//        for (int r = 0; r < grid.getNumRows(); r++)
//        {
//            for (int c = 0; c < grid.getNumCols(); c++)
//            {
//                // If there's an object at this location, draw it.
//                int xleft = colToXCoord(c);
//                int ytop = rowToYCoord(r);
//                
//                if (grid.getMarker(r, c) != null) 
//                {
//                        Marker m = grid.getMarker(r, c);
//                        g2.setPaint(m.getColor());
//                        g2.fillRect(xleft, ytop, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
//                   
//                }
//            }
//        }
//    }
    private void drawTicTacToe(Graphics2D g2)
    {
        for(int zoneR=0; zoneR<3; zoneR++)
            for(int zoneC=0; zoneC<3; zoneC++)
            {
                if(grid.getTicTacToeBoard()[zoneR][zoneC] != null)
                {
                    Color c = grid.getTicTacToeBoard()[zoneR][zoneC].getColor();
                    int xx = this.colToXCoord(zoneC*8);
                    int yy = this.rowToYCoord(zoneR*8);
                    int xxx= this.colToXCoord((zoneC+1)*8);
                    int yyy= this.rowToYCoord((zoneR+1)*8);
                    g2.setColor(c);
                    BasicStroke bs = new BasicStroke(6);
                    g2.setStroke(bs);
                    g2.drawRect(xx, yy, xxx-xx-2, yyy-yy-2);
                }
            }

    }
    
    private void drawScoringNotifications(Graphics2D g2)
    {
        ArrayList<ScoringNotification> snAList = grid.getScoringNotifications();
        for (int q = snAList.size()-1; q >= 0 ; q--)
        {
            ScoringNotification sn = snAList.get(q);
            sn.fadeAway();
            if(sn.getDuration()<=0) 
                snAList.remove(q);
            else
            {
                g2.setColor(sn.getColor());
                int xx = this.colToXCoord(sn.getLocation().getCol());
                int yy = this.rowToYCoord(sn.getLocation().getRow());
                g2.fillOval(xx+4, yy+4, DEFAULT_CELL_SIZE-8, DEFAULT_CELL_SIZE-8);
                if(sn.getAmount()<0)
                {
                    g2.setColor(Color.WHITE);
                    int size = 3*sn.getDuration();
                    g2.fillOval(xx+size, yy+size, DEFAULT_CELL_SIZE-2*size, DEFAULT_CELL_SIZE-2*size);
                    g2.setColor(sn.getColor());
                    
//                    g2.setFont(new Font("Arial", Font.BOLD, 16));
//                    g2.setPaint(Color.BLACK);
//                    String numberText = ""+sn.getAmount();
//                    g2.drawString(numberText, xx, yy+size*5);
                }
                else
                {
                    g2.setFont(new Font("Arial", Font.BOLD, 16));
                    g2.setPaint(Color.BLACK);
                    String numberText = ""+sn.getAmount();
                        FontRenderContext frc = g2.getFontRenderContext();
                        Rectangle2D bounds = g2.getFont().getStringBounds(numberText, frc);
                        float centerX = xx + DEFAULT_CELL_SIZE / 2;
                        float centerY = yy + DEFAULT_CELL_SIZE / 2;
                        float leftX = centerX - (float) bounds.getWidth() / 2;
                        LineMetrics lm = g2.getFont().getLineMetrics(numberText, frc);
                        float baselineY = centerY + lm.getAscent()/2; 
                        g2.drawString(numberText, leftX, baselineY);                   
                }
//                g2.drawString("10"+sn.getAmount(), xx+10, yy+14);
            }
//                        final int MARKER_FONT_SIZE = DEFAULT_CELL_SIZE-4;
//                        g2.setFont(new Font("Arial", Font.BOLD, MARKER_FONT_SIZE));
//                        FontRenderContext frc = g2.getFontRenderContext();
//                        Rectangle2D bounds = g2.getFont().getStringBounds(numberText, frc);
//                        float centerX = xleft + DEFAULT_CELL_SIZE / 2;
//                        float centerY = ytop + DEFAULT_CELL_SIZE / 2;
//                        float leftX = centerX - (float) bounds.getWidth() / 2;
//                        LineMetrics lm = g2.getFont().getLineMetrics(numberText, frc);
//                        float baselineY = centerY + lm.getAscent()/2; 
//                        g2.drawString(numberText, leftX, baselineY);                   
            
        }
    }
    
//    public void drawHighlightLines(Graphics2D g2)
//    {
//        final int BORDER = 6;
//        for(int r=0;r<this.numRows;r++)
//        {
//            g2.setColor(Color.WHITE);
////            int rowVar = Math.abs(18-rowTotal);
////            if(rowVar < 7)
////            {
//////                g2.setColor(new Color(150+(4-rowVar)*25,150+(4-rowVar)*25,150));
//////                g2.setColor(Color.getHSBColor((float)(rowVar/10.0), 0.9f, 0.9f));
////                g2.fillRect(colToXCoord(0)+BORDER+rowVar, rowToYCoord(r)+BORDER+rowVar, (DEFAULT_CELL_SIZE+1)*numCols-2*(BORDER+rowVar), DEFAULT_CELL_SIZE-2*(BORDER+rowVar));
////            }
////            int colVar = Math.abs(18-colTotal);
////            if(colVar < 7)
////            {
//////                g2.setColor(new Color(150+(4-colVar)*25,150+(4-colVar)*25,150));
//////                g2.setColor(Color.getHSBColor((float)(colVar/10.0), 0.9f, 0.9f));
////                g2.fillRect(colToXCoord(r)+BORDER+colVar, rowToYCoord(0)+BORDER+colVar, (DEFAULT_CELL_SIZE)-2*(BORDER+colVar), (DEFAULT_CELL_SIZE+1)*numRows-2*(BORDER+colVar));
////            }
//            
//            for(int type=1; type<=6; type++)
//            {
//                int CENTER = 15;
//                int total = grid.getLineTotal(r,type);
//                int var = Math.abs(18-total);
//                if(var < 4)
//                {
//                    g2.setStroke(new BasicStroke(15-4*var));
//                
//                    if(type == RatBotsGrid.ROW)
//                        g2.drawLine(colToXCoord(0)+CENTER, rowToYCoord(r)+CENTER, 
//                                colToXCoord(DEFAULT_CELL_COUNT-1)+CENTER, rowToYCoord(r)+CENTER);
//                    if(type == RatBotsGrid.COLUMN)
//                        g2.drawLine(colToXCoord(r)+CENTER, rowToYCoord(0)+CENTER,
//                                colToXCoord(r)+CENTER, rowToYCoord(DEFAULT_CELL_COUNT-1)+CENTER);
//                    if(type == RatBotsGrid.DIAG_RIGHT_FROM_TOP)
//                        g2.drawLine(colToXCoord(r)+CENTER, rowToYCoord(0)+CENTER,
//                                colToXCoord(DEFAULT_CELL_COUNT-1)+CENTER, rowToYCoord(DEFAULT_CELL_COUNT-1-r)+CENTER);
//                    if(type == RatBotsGrid.DIAG_LEFT_FROM_TOP)
//                        g2.drawLine(colToXCoord(r)+CENTER, rowToYCoord(0)+CENTER,
//                                colToXCoord(0)+CENTER, rowToYCoord(r)+CENTER);
//                    if(type == RatBotsGrid.DIAG_RIGHT_FROM_LEFT)
//                        g2.drawLine(colToXCoord(0)+CENTER, rowToYCoord(r)+CENTER,
//                                colToXCoord(DEFAULT_CELL_COUNT-1-r)+CENTER, rowToYCoord(DEFAULT_CELL_COUNT-1)+CENTER);
//                    if(type == RatBotsGrid.DIAG_LEFT_FROM_RIGHT)
//                        g2.drawLine(colToXCoord(DEFAULT_CELL_COUNT-1)+CENTER, rowToYCoord(r)+CENTER,
//                                colToXCoord(r)+CENTER, rowToYCoord(DEFAULT_CELL_COUNT-1)+CENTER);
//                    g2.setStroke(new BasicStroke(1));
//                }
//            }
//        }
//    }
//    /**
//     * Draws the occupants of the grid.
//     * @param g2 the graphics context
//     */
//    private void drawWalls(Graphics2D g2)
//    {
//        g2.setColor(cs.getWallColor());
//        // Look at all grid locations.
//        for (int r = 0; r < grid.getNumRows(); r++)
//        {
//            for (int c = 0; c < grid.getNumCols(); c++)
//            {
//                // If there's an object at this location, draw it.
//                Location loc = new Location(r, c);
//                int xleft = colToXCoord(loc.getCol());
//                int ytop = rowToYCoord(loc.getRow());
//                //DRAWING WALLS-------------------------------------
//                final int THICK = 2;
//                if(grid.isWall(loc, Location.NORTH)) //NORTH
//                {
//                    g2.fillRect(xleft,ytop,cellSize,THICK);    
////                    System.out.println("Drawing a wall at "+loc.toString());
//                }
//                if(grid.isWall(loc, Location.EAST)) //EAST
//                {
////                    g2.setColor(Color.RED);
//                    g2.fillRect(xleft+cellSize-THICK,ytop,THICK,cellSize);    
////                    System.out.println("Drawing a wall at "+loc.toString());
//                }
//                if(grid.isWall(loc, Location.SOUTH)) //SOUTH
//                {
////                    g2.setColor(Color.RED);
//                    g2.fillRect(xleft,ytop+cellSize-THICK,cellSize,THICK);    
////                    System.out.println("Drawing a wall at "+loc.toString());
//                }
//                if(grid.isWall(loc, Location.WEST)) //WEST
//                {
////                    g2.setColor(Color.RED);
//                    g2.fillRect(xleft,ytop,THICK,cellSize);    
////                    System.out.println("Drawing a wall at "+loc.toString());
//                }
//            }
//        }
//    }



    /**
     * Draw the gridlines for the grid. We only draw the portion of the
     * lines that intersect the current clipping bounds.
     * @param g2 the Graphics2 object to use to render
     */
    private void drawGridlines(Graphics2D g2)
    {
        Rectangle curClip = g2.getClip().getBounds();
        int top = getInsets().top, left = getInsets().left;

        int miny = Math.max(0, (curClip.y - top) / (cellSize + 1)) * (cellSize + 1) + top;
        int minx = Math.max(0, (curClip.x - left) / (cellSize + 1)) * (cellSize + 1) + left;
        int maxy = Math.min(numRows, 
                (curClip.y + curClip.height - top + cellSize) / (cellSize + 1)) 
                * (cellSize + 1) + top;
        int maxx = Math.min(numCols, 
                (curClip.x + curClip.width - left + cellSize) / (cellSize + 1))
                * (cellSize + 1) + left;

//        g2.setColor(Color.GREEN);
//        for (int y = miny; y <= maxy; y += cellSize + 1)
//            for (int x = minx; x <= maxx; x += cellSize + 1)
//            {
//                Location loc = locationForPoint(
//                        new Point(x + cellSize / 2, y + cellSize / 2));
//                if (loc != null && !grid.isValid(loc))
//                    g2.fillRect(x + 1, y + 1, cellSize, cellSize);
//            }

        g2.setColor(cs.getGridLineColor());
        for (int y = miny; y <= maxy; y += cellSize + 1)
            // draw horizontal lines
            g2.drawLine(minx, y, maxx, y);

        for (int x = minx; x <= maxx; x += cellSize + 1)
            // draw vertical lines
            g2.drawLine(x, miny, x, maxy);
    }
    

    /**
     * Draws a watermark that shows the version number if it is < 1.0
     * @param g2 the graphics context
     */
    private void drawWatermark(Graphics2D g2)
    {
        String versionId = resources.getString("version.id");
        if(RatBotsArena.getPlayMode() == RatBotsArena.CHALLENGE_1)
            versionId = "Challenge #1";
        else if(RatBotsArena.getPlayMode() == RatBotsArena.CHALLENGE_2)
            versionId = "Challenge #2";
        else if(RatBotsArena.getPlayMode() == RatBotsArena.CHALLENGE_3)
            versionId = "Challenge #3";
        else if(RatBotsArena.getPlayMode() == RatBotsArena.SANDBOX)
            versionId = "Sandbox Mode";
        else
            versionId = "BotWorld 22";
        
        if ("1.00".compareTo(versionId) == 0) return; // TODO: Better mechanism

        try
        {
            if ("hide".equals(System.getProperty("info.gridworld.gui.watermark")))
                return;
        }
        catch (SecurityException ex)
        {
            // oh well...
        }

        g2 = (Graphics2D) g2.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle rect = getBounds();
        g2.setPaint(new Color(210,210,230));
        final int WATERMARK_FONT_SIZE = 48;
        g2.setFont(new Font("Castellar", Font.BOLD, WATERMARK_FONT_SIZE));
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = g2.getFont().getStringBounds(versionId, frc);
        float centerX = rect.x + rect.width / 2 -3;
        float centerY = rect.y + rect.height/2 -5;
        float leftX = centerX - (float) bounds.getWidth() / 2;
        LineMetrics lm = g2.getFont().getLineMetrics(versionId, frc);
        float baselineY = centerY + lm.getAscent()/2;
        g2.drawString(versionId, leftX, baselineY);
    }

    /**
     * Enables/disables showing of tooltip giving information about the
     * occupant beneath the mouse.
     * @param flag true/false to enable/disable tool tips
     */
    public void setToolTipsEnabled(boolean flag)
    {
        try
        {
            if ("hide".equals(System.getProperty("info.gridworld.gui.tooltips")))
                flag = false;
        }
        catch (SecurityException ex)
        {
            // oh well...
        }
        if (flag)
            ToolTipManager.sharedInstance().registerComponent(this);
        else
            ToolTipManager.sharedInstance().unregisterComponent(this);
        toolTipsEnabled = flag;
    }

    /**
     * Sets the grid being displayed. Reset the cellSize to be the
     * largest that fits the entire grid in the current visible area (use
     * default if grid is too large).
     * @param gr the grid to display
     */
    public void setGrid(Grid<?> gr)
    {
        currentLocation = new Location(0, 0);
        JViewport vp = getEnclosingViewport(); // before changing, reset
        // scroll/pan position
        if (vp != null)
            vp.setViewPosition(new Point(0, 0));

        grid = (RatBotsGrid)gr;
        originRow = originCol = 0;

        if (grid.getNumRows() == -1 && grid.getNumCols() == -1)
        {
            numRows = numCols = 2000; 
            // This determines the "virtual" size of the pan world
        }
        else
        {
            numRows = grid.getNumRows();
            numCols = grid.getNumCols();
        }
        recalculateCellSize(MIN_CELL_SIZE);        
    }

    // private helpers to calculate extra width/height needs for borders/insets.
    private int extraWidth()
    {
        return getInsets().left + getInsets().right;
    }

    private int extraHeight()
    {
        return getInsets().top + getInsets().left;
    }

    /**
     * Returns the desired size of the display, for use by layout manager.
     * @return preferred size
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(numCols * (cellSize + 1) + 1 + extraWidth(), 
                numRows * (cellSize + 1) + 1 + extraHeight());
    }

    /**
     * Returns the minimum size of the display, for use by layout manager.
     * @return minimum size
     */
    public Dimension getMinimumSize()
    {
        return new Dimension(numCols * (MIN_CELL_SIZE + 1) + 1 + extraWidth(), 
                numRows * (MIN_CELL_SIZE + 1) + 1 + extraHeight());
    }

    /**
     * Zooms in the display by doubling the current cell size.
     */
    public void zoomIn()
    {
        cellSize *= 2;
        revalidate();
    }

    /**
     * Zooms out the display by halving the current cell size.
     */
    public void zoomOut()
    {
        cellSize = Math.max(cellSize / 2, MIN_CELL_SIZE);
        revalidate();
    }

    /**
     * Pans the display back to the origin, so that 0, 0 is at the the upper
     * left of the visible viewport.
     */
    public void recenter(Location loc)
    {
        originRow = loc.getRow();
        originCol = loc.getCol();
        repaint();
        JViewport vp = getEnclosingViewport();
        if (vp != null)
        {
            showPanTip();
        }
    }

    /**
     * Given a Point determine which grid location (if any) is under the
     * mouse. This method is used by the GUI when creating Fish by clicking on
     * cells in the display.
     * @param p the Point in question (in display's coordinate system)
     * @return the Location beneath the event (which may not be a
     * valid location in the grid)
     */
    public Location locationForPoint(Point p)
    {
        return new Location(yCoordToRow(p.y), xCoordToCol(p.x));
    }

    public Point pointForLocation(Location loc)
    {
        return new Point(colToXCoord(loc.getCol()) + cellSize / 2,
                rowToYCoord(loc.getRow()) + cellSize / 2);
    }

    // private helpers to convert between (x,y) and (row,col)
    private int xCoordToCol(int xCoord)
    {
        return (xCoord - 1 - getInsets().left) / (cellSize + 1) + originCol;
    }

    private int yCoordToRow(int yCoord)
    {
        return (yCoord - 1 - getInsets().top) / (cellSize + 1) + originRow;
    }

    private int colToXCoord(int col)
    {
        return (col - originCol) * (cellSize + 1) + 1 + getInsets().left;
    }

    private int rowToYCoord(int row)
    {
        return (row - originRow) * (cellSize + 1) + 1 + getInsets().top;
    }
    private int colToXCoord(double col)
    {
        return (int)((col - originCol) * (cellSize + 1) + 1 + getInsets().left);
    }

    private int rowToYCoord(double row)
    {
        return (int)((row - originRow) * (cellSize + 1) + 1 + getInsets().top);
    }

    /**
     * Given a MouseEvent, determine what text to place in the floating tool tip
     * when the the mouse hovers over this location. If the mouse is over a
     * valid grid cell. we provide some information about the cell and
     * its contents. This method is automatically called on mouse-moved events
     * since we register for tool tips.
     * @param evt the MouseEvent in question
     * @return the tool tip string for this location
     */
    public String getToolTipText(MouseEvent evt)
    {
        Location loc = locationForPoint(evt.getPoint());
        return getToolTipText(loc);
    }

    private String getToolTipText(Location loc)
    {
        if (!toolTipsEnabled || loc == null || !grid.isValid(loc))
            return null;
        Object f = grid.get(loc);
        if (f != null)
            return MessageFormat.format(resources
                    .getString("cell.tooltip.nonempty"), new Object[]
                { loc, f });
        else
            return MessageFormat.format(resources
                    .getString("cell.tooltip.empty"), new Object[]
                { loc, f });
    }

    /**
     * Sets the current location.
     * @param loc the new location
     */
    public void setCurrentLocation(Location loc)
    {
        currentLocation = loc;
    }

    /**
     * Gets the current location.
     * @return the currently selected location (marked with a bold square)
     */
    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    /**
     * Moves the current location by a given amount.
     * @param dr the number of rows by which to move the location
     * @param dc the number of columns by which to move the location
     */
    public void moveLocation(int dr, int dc)
    {
        Location newLocation = new Location(currentLocation.getRow() + dr,
                currentLocation.getCol() + dc);
        if (!grid.isValid(newLocation))
            return;

        currentLocation = newLocation;

        JViewport viewPort = getEnclosingViewport();
        if (isPannableUnbounded())
        {
            if (originRow > currentLocation.getRow())
                originRow = currentLocation.getRow();
            if (originCol > currentLocation.getCol())
                originCol = currentLocation.getCol();
            Dimension dim = viewPort.getSize();
            int rows = dim.height / (cellSize + 1);
            int cols = dim.width / (cellSize + 1);
            if (originRow + rows - 1 < currentLocation.getRow())
                originRow = currentLocation.getRow() - rows + 1;
            if (originCol + rows - 1 < currentLocation.getCol())
                originCol = currentLocation.getCol() - cols + 1;
        }
        else if (viewPort != null)
        {
            int dx = 0;
            int dy = 0;
            Point p = pointForLocation(currentLocation);
            Rectangle locRect = new Rectangle(p.x - cellSize / 2, p.y
                    - cellSize / 2, cellSize + 1, cellSize + 1);

            Rectangle viewRect = viewPort.getViewRect();
            if (!viewRect.contains(locRect))
            {
                while (locRect.x < viewRect.x + dx)
                    dx -= cellSize + 1;
                while (locRect.y < viewRect.y + dy)
                    dy -= cellSize + 1;
                while (locRect.getMaxX() > viewRect.getMaxX() + dx)
                    dx += cellSize + 1;
                while (locRect.getMaxY() > viewRect.getMaxY() + dy)
                    dy += cellSize + 1;

                Point pt = viewPort.getViewPosition();
                pt.x += dx;
                pt.y += dy;
                viewPort.setViewPosition(pt);
            }
        }
        repaint();
        showTip(getToolTipText(currentLocation),
                pointForLocation(currentLocation));
    }

    /**
     * Show a tool tip.
     * @param tipText the tool tip text
     * @param pt the pixel position over which to show the tip
     */
    public void showTip(String tipText, Point pt)
    {
        if (getRootPane() == null)
            return;
        // draw in glass pane to appear on top of other components
        if (glassPane == null)
        {
            getRootPane().setGlassPane(glassPane = new JPanel());
            glassPane.setOpaque(false);
            glassPane.setLayout(null); // will control layout manually
            glassPane.add(tip = new JToolTip());
            tipTimer = new Timer(TIP_DELAY, new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    glassPane.setVisible(false);
                }
            });
            tipTimer.setRepeats(false);
        }
        if (tipText == null)
            return;

        // set tip text to identify current origin of pannable view
        tip.setTipText(tipText);

        // position tip to appear at upper left corner of viewport
        tip.setLocation(SwingUtilities.convertPoint(this, pt, glassPane));
        tip.setSize(tip.getPreferredSize());

        // show glass pane (it contains tip)
        glassPane.setVisible(true);
        glassPane.repaint();

        // this timer will hide the glass pane after a short delay
        tipTimer.restart();
    }

    /**
     * Calculate the cell size to use given the current viewable region and the
     * the number of rows and columns in the grid. We use the largest
     * cellSize that will fit in the viewable region, bounded to be at least the
     * parameter minSize.
     */
    private void recalculateCellSize(int minSize)
    {
        if (numRows == 0 || numCols == 0)
        {
            cellSize = 0;
        }
        else
        {
            JViewport vp = getEnclosingViewport();
            Dimension viewableSize = (vp != null) ? vp.getSize() : getSize();
            int desiredCellSize = Math.min(
                    (viewableSize.height - extraHeight()) / numRows,
                    (viewableSize.width - extraWidth()) / numCols) - 1;
            // now we want to approximate this with 
            // DEFAULT_CELL_SIZE * Math.pow(2, k)
            cellSize = DEFAULT_CELL_SIZE;
            if (cellSize <= desiredCellSize)                
                while (2 * cellSize <= desiredCellSize)
                    cellSize *= 2;
            else
                while (cellSize / 2 >= Math.max(desiredCellSize, MIN_CELL_SIZE))
                    cellSize /= 2;
        }
        revalidate();
    }

    // helper to get our parent viewport, if we are in one.
    private JViewport getEnclosingViewport()
    {
        Component parent = getParent();
        return (parent instanceof JViewport) ? (JViewport) parent : null;
    }

    // GridPanel implements the Scrollable interface to get nicer behavior in a
    // JScrollPane. The 5 methods below are the methods in that interface

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction)
    {
        return cellSize + 1;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction)
    {
        if (orientation == SwingConstants.VERTICAL)
            return (int) (visibleRect.height * .9);
        else
            return (int) (visibleRect.width * .9);
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return new Dimension(DEFAULT_CELL_COUNT * (DEFAULT_CELL_SIZE + 1) + 1 + extraWidth(), 
                DEFAULT_CELL_COUNT * (DEFAULT_CELL_SIZE + 1) + 1 + extraHeight());
    }

    // GridPanel implements the PseudoInfiniteViewport.Pannable interface to
    // play nicely with the pan behavior for unbounded view.
    // The 3 methods below are the methods in that interface.

    public void panBy(int hDelta, int vDelta)
    {
        originCol += hDelta / (cellSize + 1);
        originRow += vDelta / (cellSize + 1);
        repaint();
    }

    public boolean isPannableUnbounded()
    {
        return grid != null && (grid.getNumRows() == -1 || grid.getNumCols() == -1);
    }

    /**
     * Shows a tool tip over the upper left corner of the viewport with the
     * contents of the pannable view's pannable tip text (typically a string
     * identifiying the corner point). Tip is removed after a short delay.
     */
    public void showPanTip()
    {
        String tipText = null;
        Point upperLeft = new Point(0, 0);
        JViewport vp = getEnclosingViewport();
        if (!isPannableUnbounded() && vp != null)
            upperLeft = vp.getViewPosition();
        Location loc = locationForPoint(upperLeft);
        if (loc != null)
            tipText = getToolTipText(loc);

        showTip(tipText, getLocation());
    }
}
