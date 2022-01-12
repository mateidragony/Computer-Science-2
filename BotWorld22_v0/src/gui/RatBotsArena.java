package gui; 

import actor.Wall;
import actor.Paper;
import actor.Rock;
import actor.Scissors;
import grid.Location;
import grid.RatBotsGrid;
import java.util.Random;
import world.World;

/**
 * The Arena class includes all of the methods needed to setup the arena 
 * according to the rules of the game.  
 * @author Spock
 */
public class RatBotsArena 
{
    /**
     * The size of a side of the central starting room in the arena. 
     */
    private Random randy = new Random();
    
    public static final int CHALLENGE_1 = 1; //Get the single Crop
    public static final int CHALLENGE_2 = 2; //Plant Seed by Lake
    public static final int CHALLENGE_3 = 3; //Connect Tree to Water with Canals
    public static final int SANDBOX = 4; //SandBox Mode, 1 of everything to start
    public static final int START = 5;
    public static final int NORMAL = 6;
    
    private int SIZE = RatBotsGridPanel.DEFAULT_CELL_COUNT;
    private static int playMode = START; 
    public void setPlayMode(int in) 
    { 
        playMode = in;
    }
    public static int getPlayMode()
    {
        return playMode;
    }
    
    /**
     * Toggles whether the grid will include Blocks or not.  
     * This is an option in the Arena menu.
     */
//    public void toggleShowBlocks(World world) 
//    { 
//        withBlocks = ! withBlocks; 
//    }

    /**
     * Initializes the Arena based on the selected rules.  
     * @param world the world that the Arena is within
     */
    public void initializeArena(World world)
    {
        if(playMode == NORMAL)
        {
            addSectorWalls(world);
            for(int z=0; z<40; z++)
                addRandomRock(world, 0);
            for(int z=0; z<6; z++)
                addRandomRock(world, 80);
            addRandomScissors(world);
            addRandomPapers(1, world);
        }
        else if(playMode == CHALLENGE_1)
        {   //Single Rock to mine
            RatBotsGrid grid = (RatBotsGrid)world.getGrid();
            Location corner = getRandomEmptyCornerZoneLocation(grid);
            new Rock(1).putSelfInGrid(grid, corner);
        }
        else if(playMode == CHALLENGE_2)
        {   //Get a Scissors
            RatBotsGrid grid = (RatBotsGrid)world.getGrid();
            Location edge = getRandomEmptySideZoneEdgeLocation(grid);
            new Scissors().putSelfInGrid(grid, edge);
        }
        else if(playMode == CHALLENGE_3)
        {   //Complete a set.
            RatBotsGrid grid = (RatBotsGrid)world.getGrid();
            Location corner = getRandomEmptyCornerZoneLocation(grid);
            new Rock(2).putSelfInGrid(grid, corner);
            Location edge = getRandomEmptySideZoneEdgeLocation(grid);
            new Scissors().putSelfInGrid(grid, edge);
            Location center = getRandomEmptyCenterZoneLocation(grid); 
            new Paper(1,400).putSelfInGrid(grid, center);
        }
        else if(playMode == SANDBOX)
        {
            addOneOfEverything(world);            
        }
        else if(playMode == START)
        {
            addOneOfEverything(world);            
//            playMode = NORMAL;
        }
    }
    
    private void addSectorWalls(World world)
    {      
        for(int z=0; z<2; z++)
            addSymmetricWall(world,z);
        for(int z=4; z<11; z++)
            addSymmetricWall(world,z);
        for(int z=13; z<20; z++)
            addSymmetricWall(world,z);
        for(int z=22; z<24; z++)
            addSymmetricWall(world,z);
    }
    
    private void addSymmetricWall(World world, int z)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        new Wall().putSelfInGrid(grid, new Location(7,z));
        new Wall().putSelfInGrid(grid, new Location(z,7));
        new Wall().putSelfInGrid(grid, new Location(SIZE-8,z));
        new Wall().putSelfInGrid(grid, new Location(z,SIZE-8));        
    }
    
    public void addRandomRock(World world, int extraAmt)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Location loc = getRandomEmptyCornerZoneLocation(grid);
        int amt = randy.nextInt(6) + randy.nextInt(6) + extraAmt; 
        new Rock(amt).putSelfInGrid(grid, loc);
    }
    
    public void addRandomScissors(World world)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Location loc = getRandomEmptySideZoneEdgeLocation(grid);
        Scissors bob = new Scissors();
        bob.setDirection(loc.getDirectionToward(new Location(SIZE/2, SIZE/2)));
        bob.putSelfInGrid(grid, loc);
    }

    public void addRandomPapers(int num, World world)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        for(int q=0; q<num; q++)
        {
            Location center = getRandomEmptyCenterZoneLocation(grid); 
            int amt = randy.nextInt(3)+2;
            new Paper(amt,25).putSelfInGrid(grid, center);            
        }
    }
    
    
    private void addOneOfEverything(World world)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Paper seed = new Paper();
        seed.putSelfInGrid(grid, new Location(0,0));
        Wall b = new Wall();
        b.putSelfInGrid(grid, new Location(0,7));
    }
    
    private Location getRandomEmptyCenterZoneLocation(RatBotsGrid grid)
    {
        int r=SIZE/2; int c=SIZE/2;
        Location loc = new Location(-1,-1);
        do
        {
            r = randy.nextInt(6)+9;
            c = randy.nextInt(6)+9;
            loc = new Location(r,c);
        }while(grid.get(loc) != null);
        return loc;
    }
    
    private Location getRandomEmptySideZoneEdgeLocation(RatBotsGrid grid)
    {
        int place = randy.nextInt(8)+8;
        int whichWall = randy.nextInt(4);
        Location loc = new Location(-1,-1);
        do
        {
            place = randy.nextInt(8)+8;
            whichWall = randy.nextInt(4);
            if(whichWall == 0)
                loc = new Location(place,0);
            else if (whichWall == 1)
                loc = new Location(place,SIZE-1);
             else if (whichWall == 2)
                loc = new Location(0,place);
             else 
                loc = new Location(SIZE-1,place);
        } while (grid.get(loc) != null);
        return loc;
    }
    
    private Location getRandomEmptyCornerZoneLocation(RatBotsGrid grid)
    {
        int r=0, c=0;
        Location loc;
        do
        {
            r=randy.nextInt(SIZE);
            c=randy.nextInt(SIZE);
            loc = new Location(r,c);
        } while (grid.get(loc) != null || (5 < r && r < SIZE-6) || (5 < c && c < SIZE-6));
        return loc;
    }
    
}
