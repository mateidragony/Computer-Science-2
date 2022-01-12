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
 * @author Chris Nevison
 * @author Cay Horstmann
 */



//import info.gridworld.grid.Grid;
//import info.gridworld.grid.Location;
//import info.gridworld.world.World;

import actor.BotBrain;
import world.BotWorld;
import world.World;
import grid.Grid;
import grid.Location;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import util.RatBotManager;

/**
 * The WorldFrame displays a World and allows manipulation of its occupants.
 * <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */
public class RatBotsWorldFrame<T> extends JFrame
{
    private RatBotsGUIController<T> control;
    private RatBotsGridPanel display;
    private JTextArea messageArea;
    private ArrayList<JMenuItem> menuItemsDisabledDuringRun;
    private World<T> world;
    private ResourceBundle resources;
    private DisplayMap displayMap;
    private RatBotsScoreBoard scoreBoard;
    
    private Set<Class> gridClasses;
    private JMenu newGridMenu;

    private static int count = 0;

    /**
     * Constructs a WorldFrame that displays the occupants of a world
     * @param world the world to display
     */
    public RatBotsWorldFrame(World<T> world)
    {
        this.world = world;
        count++;
        resources = ResourceBundle
                .getBundle(getClass().getName() + "Resources");
        try
        {
            System.setProperty("sun.awt.exception.handler",
                    GUIExceptionHandler.class.getName());
        }
        catch (SecurityException ex)
        {
            // will fail in an applet
        }
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                count--;
                if (count == 0)
                    System.exit(0);
            }
        });

        displayMap = new DisplayMap();        
        String title = null;
        try // won't work in applets
        {
            System.getProperty("info.gridworld.gui.frametitle");
        }
        catch (SecurityException ex) {}
        if (title == null) title = resources.getString("frame.title"); 
        setTitle(title);
        setLocation(25, 15);

        URL appIconUrl = getClass().getResource("GridWorld.gif");
        if (appIconUrl != null)
        {
            ImageIcon appIcon = new ImageIcon(appIconUrl);
            setIconImage(appIcon.getImage());
        }
        
        makeMenus();

        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setLayout(new BorderLayout());
        setContentPane(content);

        display = new RatBotsGridPanel(displayMap, resources);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new
           KeyEventDispatcher() 
           {
               public boolean dispatchKeyEvent(KeyEvent event)
               {
                   if (getFocusOwner() == null) return false;
                   String text = KeyStroke.getKeyStrokeForEvent(event).toString();
//                   System.out.println("KEY! "+text);
                   final String PRESSED = "pressed ";                  
                   int n = text.indexOf(PRESSED);
                   if (n < 0) return false;
                   // filter out modifier keys; they are neither characters or actions
                   if (event.getKeyChar() == KeyEvent.CHAR_UNDEFINED && !event.isActionKey()) 
                       return false;
                   text = text.substring(0, n)  + text.substring(n + PRESSED.length());
//                   System.out.println("KEY! "+text);
//                   boolean consumed = getWorld().keyPressed(text, display.getCurrentLocation());
                   boolean consumed = keyPressed(text);
                   if (consumed) repaint();
                   return consumed;
               }
           });
        
        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setViewport(new PseudoInfiniteViewport(scrollPane));
        scrollPane.setViewportView(display);
        content.add(scrollPane, BorderLayout.CENTER);

        gridClasses = new TreeSet<Class>(new Comparator<Class>()
        {
            public int compare(Class a, Class b)
            {
                return a.getName().compareTo(b.getName());
            }
        });
        for (String name : world.getGridClasses())
            try
            {
                gridClasses.add(Class.forName(name));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        Grid<T> gr = world.getGrid();
        gridClasses.add(gr.getClass());

//        makeNewGridMenu();

        control = new RatBotsGUIController<T>(this, display, displayMap, resources);
        content.add(control.controlPanel(), BorderLayout.SOUTH);

        messageArea = new JTextArea(1, 35);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setBackground(new Color(0xFAFAD2));
        content.add(new JScrollPane(messageArea), BorderLayout.NORTH);

        //FOR RATBOTS...
        scoreBoard = new RatBotsScoreBoard(gr);
        
        
        content.add(scoreBoard, BorderLayout.EAST);
        
        pack();
        repaint(); // to show message
        display.setGrid(gr);
    }

    public void repaint()
    {
        String message = getWorld().getMessage();
        if (message == null)
            message = resources.getString("message.default");
        messageArea.setText(message);
        messageArea.repaint();
        display.repaint(); // for applet
        scoreBoard.repaint();
        super.repaint();
    }

    /**
     * Gets the world that this frame displays
     * @return the world
     */
    public World<T> getWorld()
    {
        return world;
    }

//    public void updateScores()
//    {
//        scoreBoard.updateScores();
//    }
    
    /**
     * Sets a new grid for this world. Occupants are transferred from
     * the old world to the new.
     * @param newGrid the new grid
     */
    public void setGrid(Grid<T> newGrid)
    {
        Grid<T> oldGrid = world.getGrid();
        Map<Location, T> occupants = new HashMap<Location, T>();
        for (Location loc : oldGrid.getOccupiedLocations())
            occupants.put(loc, world.remove(loc));

        world.setGrid(newGrid);
        for (Location loc : occupants.keySet())
        {
            if (newGrid.isValid(loc))
                world.add(loc, occupants.get(loc));
        }

        display.setGrid(newGrid);
        repaint();
    }

    /**
     * Displays an error message
     * @param t the throwable that describes the error
     * @param resource the resource whose .text/.title strings 
     * should be used in the dialog
     */
    public void showError(Throwable t, String resource)
    {
        String text;
        try
        {
            text = resources.getString(resource + ".text");
        }
        catch (MissingResourceException e)
        {
            text = resources.getString("error.text");
        }

        String title;
        try
        {
            title = resources.getString(resource + ".title");
        }
        catch (MissingResourceException e)
        {
            title = resources.getString("error.title");
        }

        String reason = resources.getString("error.reason");
        String message = text + "\n"
                + MessageFormat.format(reason, new Object[]
                    { t });

        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    // Creates the drop-down menus on the frame.

    private JMenu makeMenu(String resource)
    {
        JMenu menu = new JMenu();
        configureAbstractButton(menu, resource);
        return menu;
    }

    private JMenuItem makeMenuItem(String resource, ActionListener listener)
    {
        JMenuItem item = new JMenuItem();
        configureMenuItem(item, resource, listener);
        return item;
    }

    private void configureMenuItem(JMenuItem item, String resource,
            ActionListener listener)
    {
        configureAbstractButton(item, resource);
        item.addActionListener(listener);
        try
        {
            String accel = resources.getString(resource + ".accel");
            String metaPrefix = "@";
            if (accel.startsWith(metaPrefix))
            {
                int menuMask = getToolkit().getMenuShortcutKeyMask();
                KeyStroke key = KeyStroke.getKeyStroke(KeyStroke.getKeyStroke(
                        accel.substring(metaPrefix.length())).getKeyCode(),
                        menuMask);
                item.setAccelerator(key);
            }
            else
            {
                item.setAccelerator(KeyStroke.getKeyStroke(accel));
            }
        }
        catch (MissingResourceException ex)
        {
            // no accelerator
        }
    }

    private void configureAbstractButton(AbstractButton button, String resource)
    {
        String title = resources.getString(resource);
        int i = title.indexOf('&');
        int mnemonic = 0;
        if (i >= 0)
        {
            mnemonic = title.charAt(i + 1);
            title = title.substring(0, i) + title.substring(i + 1);
            button.setText(title);
            button.setMnemonic(Character.toUpperCase(mnemonic));
            button.setDisplayedMnemonicIndex(i);
        }
        else
            button.setText(title);
    }

    public void addRatBotToMenu(final Class c)
    {
        JMenuItem item = new JMenuItem();
        item.setFont(new Font("SansSerif", Font.PLAIN, 12));
//        BotBrain rb = (BotBrain)c.newInstance();
        String name = c.getName();
        if(name.contains("."))
        {
            String names[] = name.split("\\.");
            name = names[names.length-1];
        }
        item.setText("" + name);
        //add this to the allRats list...
        try 
        {
            Object o = c.newInstance();
            if (o instanceof BotBrain) 
            {
                ((BotWorld)world).addToAllRats((BotBrain) o);
//                world.setMessage("Added "+c.getName()+" to List.");
            }
        } 
        catch (InstantiationException ex) { ex.printStackTrace(); }
        catch (IllegalAccessException ex) { ex.printStackTrace(); }
        
        
        item.addActionListener(new ActionListener()
        {
            Class myClass = c;
            public void actionPerformed(ActionEvent e)
            {
                BotWorld rbworld = (BotWorld) world;
            
                try {
                    Object o = myClass.newInstance();
                    if (o instanceof BotBrain) {
                        //Changed this in 2018 to automatically name each BotBrain the same as its class name.
                        BotBrain b = (BotBrain) o;
                        b.setName(item.getText());
                        rbworld.add(b);
//                        rbworld.add((BotBrain) o);  //old version ->2017
                        world.setMessage("Added "+c.getName()+" to Arena.");
                        repaint();
                    }
                } 
                catch (InstantiationException ex) { ex.printStackTrace(); }
                catch (IllegalAccessException ex) { ex.printStackTrace(); }
////                rbworld.add(new SmartRat());
            }
        });
        
        if(item.getText().compareTo("G") < 0)
            firstSub.add(item);
        else if (item.getText().compareTo("Q") < 0)
            secondSub.add(item);
        else
            thirdSub.add(item);
//        rbMenu.add(item);     
        
    }
    
        JMenuBar mbar = new JMenuBar();
        JMenu rbMenu;
        JMenu firstSub = new JMenu("A-F");
        JMenu secondSub = new JMenu("H-Q");
        JMenu thirdSub = new JMenu("R-Z +");
        
    private void makeMenus()
    {
        JMenu menu;
        rbMenu = makeMenu("menu.rats");
        rbMenu.add(firstSub);
        rbMenu.add(secondSub);
        rbMenu.add(thirdSub);

        menuItemsDisabledDuringRun = new ArrayList<JMenuItem>();

        /* ===============================================
                Arena Menu
         ================================================= */
        mbar.add(menu = makeMenu("menu.arena"));

        menu.add(makeMenuItem("menu.arena.challenge1", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlayMode(RatBotsArena.CHALLENGE_1);
            }
        }));
        menu.add(makeMenuItem("menu.arena.challenge2", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlayMode(RatBotsArena.CHALLENGE_2);
            }
        }));
        menu.add(makeMenuItem("menu.arena.challenge3", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlayMode(RatBotsArena.CHALLENGE_3);
            }
        }));
        menu.add(makeMenuItem("menu.arena.challenge4", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlayMode(RatBotsArena.SANDBOX);
            }
        }));
//        menu.add(makeMenuItem("menu.arena.challenge5", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                setPlayMode(RatBotsArena.CHALLENGE_5);
//            }
//        }));
        menu.add(makeMenuItem("menu.arena.freePlay", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlayMode(RatBotsArena.NORMAL);
            }
        }));
        menu.add(makeMenuItem("menu.arena.gridlines", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                display.toggleShowGridLines();
            }
        }));
        menu.add(makeMenuItem("menu.arena.blocks", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                BotWorld aw = (BotWorld)world; 
//                aw.getArena().toggleShowBlocks(world);
//                aw.clearAllObjectsFromGrid();
                aw.initializeGridForRound();
                display.repaint();
            }
        }));
        menu.add(makeMenuItem("menu.arena.reset", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ((BotWorld)world).zeroScoreAllRats();
                ((BotWorld)world).initializeMatch();
                display.repaint();
            }
        }));
        
        menu.add(makeMenuItem("menu.file.quit", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        }));
        
        /* ===============================================
                RatBots Menu
         ================================================= */
        mbar.add(rbMenu);
//        rbMenu.setAutoscrolls(true);
        Font f = new Font("SansSerif", Font.PLAIN, 12);
        rbMenu.setFont(f);
        
//        rbMenu.add(makeMenuItem("menu.rats.frompackage", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                // Load RatBots from the 'ratbot' package
//                RatBotManager.loadRatBotsFromClasspath("ratbot", (RatBotWorld)world);
//            }
//        }));
        rbMenu.add(makeMenuItem("menu.rats.clear", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                control.stop();
                ((BotWorld)world).clearAllRats();
                repaint();
            }
        }));
        
//        rbMenu.add(makeMenuItem("menu.rats.fromfile", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                // Prompt for additional BotBrain loading
//                RatBotManager.loadRatBotsFromDirectoryIntoWorld((RatBotWorld)world, false);              
//            }
//        }));
        
        
        /* ===============================================
                Scoreboard Menu
         ================================================= */        
        mbar.add(menu = makeMenu("menu.scoreboard"));
        
        menu.add(makeMenuItem("menu.scoreboard.rounds", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scoreBoard.setSortOrder(RatBotsScoreBoard.ROUNDS_WON);
            }
        }));        
        menu.add(makeMenuItem("menu.scoreboard.points", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scoreBoard.setSortOrder(RatBotsScoreBoard.POINTS);
            }
        }));        
        menu.add(makeMenuItem("menu.scoreboard.totalpoints", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scoreBoard.setSortOrder(RatBotsScoreBoard.TOTAL_POINTS);
            }
        }));        
        menu.add(makeMenuItem("menu.scoreboard.name", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                scoreBoard.setSortOrder(RatBotsScoreBoard.NAME);
            }
        }));        

        
        /* ===============================================
                Tourney Menu
         ================================================= */
        mbar.add(menu = makeMenu("menu.tourney"));
        
        menu.add(makeMenuItem("menu.tourney.begin", new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ((BotWorld)world).startRoundRobin();
                control.run();
                control.setConsoleMode(true);
                control.setWarpSpeed();
            }
        }));        
        
        
        

//        mbar.add(menu = makeMenu("menu.help"));
//        menu.add(makeMenuItem("menu.help.about", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                showAboutPanel();
//            }
//        }));
//        menu.add(makeMenuItem("menu.help.help", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                showHelp();
//            }
//        }));
//        menu.add(makeMenuItem("menu.help.license", new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                showLicense();
//            }
//        }));

        setRunMenuItemsEnabled(true);
        setJMenuBar(mbar);
    }

    private void makeNewGridMenu()
    {
        newGridMenu.removeAll();
        MenuMaker<T> maker = new MenuMaker<T>(this, resources, displayMap);
        maker.addConstructors(newGridMenu, gridClasses);
    }

    /**
     * Sets the enabled status of those menu items that are disabled when
     * running.
     * @param enable true to enable the menus
     */
    public void setRunMenuItemsEnabled(boolean enable)
    {
        if(menuItemsDisabledDuringRun != null)
        for (JMenuItem item : menuItemsDisabledDuringRun)
            item.setEnabled(enable);
    }

    /**
     * Brings up a simple dialog with some general information.
     */
    private void showAboutPanel()
    {
        String html = MessageFormat.format(resources
                .getString("dialog.about.text"), new Object[]
            { resources.getString("version.id") });
        String[] props = { "java.version", "java.vendor", "java.home", "os.name", "os.arch", "os.version", "user.name", "user.home", "user.dir" };
        html += "<table border='1'>";
        for (String prop : props)
        {
            try
            {
                String value = System.getProperty(prop);
                html += "<tr><td>" + prop + "</td><td>" + value + "</td></tr>";
            }
            catch (SecurityException ex)
            {
                // oh well...
            }           
        }
        html += "</table>";
        html = "<html>" + html + "</html>";
        JOptionPane.showMessageDialog(this, new JLabel(html), resources
                .getString("dialog.about.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Brings up a window with a scrolling text pane that display the help
     * information.
     */
    private void showHelp()
    {
        JDialog dialog = new JDialog(this, resources
                .getString("dialog.help.title"));
        final JEditorPane helpText = new JEditorPane();
        try
        {
            URL url = getClass().getResource("GridWorldHelp.html");

            helpText.setPage(url);
        }
        catch (Exception e)
        {
            helpText.setText(resources.getString("dialog.help.error"));
        }
        helpText.setEditable(false);
        helpText.addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(HyperlinkEvent ev)
            {
                if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    try
                    {
                        helpText.setPage(ev.getURL());
                    }
                    catch (Exception ex)
                    {
                    }
            }
        });
        JScrollPane sp = new JScrollPane(helpText);
        sp.setPreferredSize(new Dimension(650, 500));
        dialog.getContentPane().add(sp);
        dialog.setLocation(getX() + getWidth() - 200, getY() + 50);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Brings up a dialog that displays the license.
     */
    private void showLicense()
    {
        JDialog dialog = new JDialog(this, resources
                .getString("dialog.license.title"));
        final JEditorPane text = new JEditorPane();
        try
        {
            URL url = getClass().getResource("GNULicense.txt");

            text.setPage(url);
        }
        catch (Exception e)
        {
            text.setText(resources.getString("dialog.license.error"));
        }
        text.setEditable(false);
        JScrollPane sp = new JScrollPane(text);
        sp.setPreferredSize(new Dimension(650, 500));
        dialog.getContentPane().add(sp);
        dialog.setLocation(getX() + getWidth() - 200, getY() + 50);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Nested class that is registered as the handler for exceptions on the
     * Swing event thread. The handler will put up an alert panel and dump the
     * stack trace to the console.
     */
    
    public class GUIExceptionHandler
    {
        public void handle(Throwable e)
        {
            e.printStackTrace();

            JTextArea area = new JTextArea(10, 40);
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            area.setText(writer.toString());
            area.setCaretPosition(0);
            String copyOption = resources.getString("dialog.error.copy");
            JOptionPane pane = new JOptionPane(new JScrollPane(area),
                    JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null,
                    new String[]
                        { copyOption, resources.getString("cancel") });
            pane.createDialog(RatBotsWorldFrame.this, e.toString()).setVisible(true);
            if (copyOption.equals(pane.getValue()))
            {
                area.setSelectionStart(0);
                area.setSelectionEnd(area.getText().length());
                area.copy(); // copy to clipboard
            }
        }
    }    
    
    public boolean keyPressed(String description)
    {
        if(description.equals("F1"))
        { setPlayMode(RatBotsArena.CHALLENGE_1); return true; }
        if(description.equals("F2"))
        { setPlayMode(RatBotsArena.CHALLENGE_2); return true; }
        if(description.equals("F3"))
        { setPlayMode(RatBotsArena.CHALLENGE_3); return true; }
        if(description.equals("F4"))
        { setPlayMode(RatBotsArena.SANDBOX); return true; }
//        if(description.equals("F5"))
//        { setPlayMode(RatBotsArena.CHALLENGE_5); return true; }
        if(description.equals("F6"))
        { setPlayMode(RatBotsArena.NORMAL); return true; }
        if(description.equals("F7"))
        {   scoreBoard.toggleDebugMode(); return true; }        
        if(description.equals("F8"))
        {   display.changeColorScheme(); return true; }
        if(description.equals("F9"))
        {   display.toggleShowGridLines(); return true; }      
        if(description.equals("F10"))
        {   control.setWarpSpeed(); control.setConsoleMode(true); control.run(); return true; }
        if(description.equals("F11"))
        {   control.setSlowSpeed(); control.setConsoleMode(false); control.stop(); return true; }        
        if(description.equals("F12"))
        {   control.setWarpSpeed(); control.setConsoleMode(false); control.run(); return true; }
        
        if(description.equals("1"))
        { ((BotWorld)world).addFromAllRats(0); return true; }
        if(description.equals("2"))
        { ((BotWorld)world).addFromAllRats(1); return true; }
        if(description.equals("3"))
        { ((BotWorld)world).addFromAllRats(2); return true; }
        if(description.equals("4"))
        { ((BotWorld)world).addFromAllRats(3); return true; }
        if(description.equals("5"))
        { ((BotWorld)world).addFromAllRats(4); return true; }
        if(description.equals("6"))
        { ((BotWorld)world).addFromAllRats(5); return true; }
        if(description.equals("7"))
        { ((BotWorld)world).addFromAllRats(6); return true; }
        if(description.equals("8"))
        { ((BotWorld)world).addFromAllRats(7); return true; }
        
//        if(description.equals("T") || description.equals("t"))
//        { Tail.toggleDURATION(); return true; }
        if(description.equals("R") || description.equals("r"))
        {     
            ((BotWorld)world).resetRatsInMaze();
            ((BotWorld)world).initializeMatch();
            display.repaint();
            return true; 
        }
        if(description.equals("Q") || description.equals("q") ||description.equals("ESCAPE") )
        { System.exit(0); return true; }
        //TODO:
        /*
        Add shortcuts for:
        ==================
        scoreboard settings
        */
        return false;
    }
    
    private void setPlayMode(int mode)
    {
        BotWorld aw = (BotWorld)world; 
        aw.getArena().setPlayMode(mode);
        aw.initializeGridForRound();
        display.repaint();
    }
}
