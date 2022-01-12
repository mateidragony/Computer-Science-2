package main;

import brain.RandomMover;
import gui.RatBotsArena;
import util.PAGuiUtil;
import util.RatBotManager;
import world.BotWorld;

public class BotWorldRunner
{
    public static void main(String[] args)
    {
        PAGuiUtil.setLookAndFeelToOperatingSystemLookAndFeel();
        BotWorld world = new BotWorld();
        world.show();  
        
        // Load Bots from the 'brain' package
        RatBotManager.loadRatBotsFromClasspath("brain", world);

    /*
     * This is another place where you can add Bots to the match. 
     * Loading them here will save the time of selecting from the menu.  
     */
//        RandomFarmer bob = new RandomFarmer();
//        bob.setName("RandomFarmer");
//        world.add(bob);
        

        
        if(RatBotsArena.getPlayMode() == RatBotsArena.START)
            world.getArena().setPlayMode(RatBotsArena.NORMAL);
        world.initializeGridForRound();
        world.show();
    }
}
