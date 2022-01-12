package actor;

import grid.Location;
import grid.ScoringNotification;
import gui.RatBotsColorAssigner;
import java.awt.Color;
import java.util.ArrayList;
import world.BotWorld;
/**
 * Rats are the competitors in the RatBots game.  Every Bot has a BotBrain 
 that acts as its 'brain' by making decisions for its actions.  
 * @author Spock
 */
public class Bot extends GameObject implements BlockedLocation
{       

    public static final int BUILD_COST = 5; //number of rocks used to build a Wall
    public static final int POINTS_PER_ROCK = 1;
    public static final int POINTS_PER_PAPER = 10;
    public static final int POINTS_PER_SCISSORS = 20;
    public static final int POINTS_PER_RPS_SET = 100;
    public static final int POINTS_PER_TIC_TAC_TOE_EACH_TURN = 1;
    public static final int POINTS_FOR_RPS_WIN = 5000;
    public static final int PENALTY_FOR_BANKRUPTING = 10000;
    
    /**
     * Each Bot has a BotBrain as it's 'brain'.  The Bot 'tells' the BotBrain 
        what it 'sees' and the BotBrain makes the decision on how to act.  
     */
    private BotBrain botBrain; 
            
    private int score;
    private int roundsWon;
    private int matchesWon;
    private int matchesTied;
    private int matchesLost;
    private int totalScore = 0;
    
    private int mostRecentChoice = 0;
    
    private int mSecUsed = 0;
    
    private int numRocks;
    private int numPapers;
    private int numScissors;
    
    /**
     * Constructs a red Rat without a BotBrain.  
     */
    public Bot()
    {
//        botBrain = new BotBrain();
        setColor(Color.RED);
        clearScores();
        resetInventory();
    }
    /**
     * Constructs a Rat with the given color and given BotBrain for its 'brain'.
     * All rats have a BotBrain that chooses their action each turn.  
     * @param rb the BotBrain that makes decisions for this Rat.
     */
    public Bot(BotBrain rb)
    {
        botBrain = rb;
        setColor(RatBotsColorAssigner.getAssignedColor());
        clearScores();        
        resetInventory();
    }
    /**
     * Constructs a copy of this Rat (that does not include the BotBrain.)
     * @param in the Rat being copied.
     */
    public Bot(Bot in)
    {
        super(in);
        setLocation(in.getLocation()); //Is This Needed???? 
        setColor(in.getColor());
        score = in.getScore();
    }
    
    public void setColor(Color in)
    {
        super.setColor(in);
        if(botBrain != null)
            botBrain.setColor(in);        
    }
      
    public void resetInventory()
    {
        numRocks = 0;
        numPapers = 0;
        numScissors = 0;
    }
    
    public int getNumRocks() { return numRocks; }
    public int getNumPapers() { return numPapers; }
    public int getNumScissors() { return numScissors; }
    
    
    public int getMostRecentChoice() { return mostRecentChoice; }
    
    
    /**
     * Overrides the <code>act</code> method in the <code>GameObject</code> 
 class to act based on what the BotBrain decides.
     */
    @Override
    public void act()
    {
        //Ask the BotBrain what to do. 
        giveDataToBotBrain();
//        System.out.println("starting: "+botBrain.getName()+botBrain.getMoveNumber());
        int choice = BotBrain.REST;
        if(botBrain != null)
            choice = botBrain.chooseAction();
//        System.out.println("done: "+botBrain.getName()+botBrain.getMoveNumber());
        mostRecentChoice = choice;
        
        int moveType = choice/1000;
//        int distance = (choice/1000)%10;
////        if(distance > getSpeed()) distance = getSpeed();
        int subChoice = choice%1000;
                       
        if(moveType == 0) //MOVE 
        {
            if(subChoice%45 == 0) //only move in valid directions
            {
                turn(subChoice);
                if(canMove()) 
                    move();
            }
        }
        else if(moveType == 1) //MINE
        {
            Location mineLoc = getLocation().getAdjacentLocation(subChoice);
            mine(mineLoc); 
        }
        else if(moveType == 2) //BID
        {            
            bid(subChoice); 
        }
        else if(moveType == 3) //CAPTURE
        {
            captureNeighboringScissors();
        }
        else if(moveType == 4) //THROW
        {
            startRPSbattle(subChoice);
        }
        else if(moveType == 5) //BUILD
        {
            Location buildLoc = getLocation().getAdjacentLocation(subChoice);
            buildWall(buildLoc); 
        }
        else //REST 
        {
            
        }
        
    } //end of act() method
    
    /**
     * Turns the Bot
     * @param newDirection the direction to turn to.   
     */
    public void turn(int newDirection)
    {
        setDirection(newDirection);
    }
    
    private Location nextLocation()
    {
        return getLocation().getAdjacentLocation(getDirection());
    }
    
    private boolean nextLocationValid()
    {
        return getGrid().isValid(nextLocation());
    }
        
    private boolean canMove()
    {
        return canMove(getLocation(), getDirection());
    }
   
    private boolean canMove(Location loc, int dir)
    {
        Location next = loc.getAdjacentLocation(dir);
        if(!getGrid().isValid(next)) 
            return false;
        //Make sure destination is not occupied by a Blocked Location
        if(getGrid().get(next) instanceof BlockedLocation )
            return false;
        
        return true;
    }
    
    /**
     * Moves the Bot forward, putting a Farm into the location it previously
     * occupied.
     */
    public void move()
    {        
        if(canMove())
        {
            Location old = getLocation();
            Location next = old.getAdjacentLocation(getDirection());

            moveTo(next);
            new Trail(getColor()).putSelfInGrid(getGrid(), old);
        }
    }
    
    private void mine(Location loc)
    {
        if(getGrid().isValid(loc))
        {
            GameObject go = getGrid().get(loc);
            if(go instanceof Rock)
            {
                Rock r = (Rock)go;
                r.mineRock();
                numRocks++;
            }
        }
    }
    
    private void bid(int amount)
    {
        if(numRocks < amount) 
            return; //Can't bid more than you have! 
        
        if(isInCenterSquare())
        {
            //Find the Paper that is up for auction... 
            Paper p = getThePaper();
            if(p != null)
            {
                //place the bid
                p.recieveBid(this, amount);
            }
        }
    }
    private boolean isInCenterSquare()
    {
        if(7 < this.getRow() && this.getRow() < 15 && 7 < this.getCol() && this.getCol() < 15)
            return true;
        return false;
    }
    private Paper getThePaper()
    {
        for(int r=8; r<16; r++)
            for(int c=8; c<16; c++)
            {
                GameObject go = getGrid().get(new Location(r,c));
                if(go instanceof Paper)
                {
                    Paper p = (Paper)go;
                    return p;
                }
            }
        return null;
    }
    public void wonBid(Paper p)
    {
        int bidAmount = p.getCurrentTopBid();
        if(numRocks >= bidAmount)
        {
            numRocks -= bidAmount;
            numPapers += p.getNumPages();
            return;
        }
        //Not enough Rocks to pay, try Paper next. 
        bidAmount -= numRocks;
        numRocks = 0; 
        if(numPapers >= bidAmount)
        {
            numPapers -= bidAmount;
            numPapers += p.getNumPages();
            return;           
        }
        //Not enough Rocks and Papers to pay, try Scissors next. 
        bidAmount -= numPapers;
        numPapers = 0;
        if(numScissors >= bidAmount)
        {
            numScissors -= bidAmount;
            numPapers += p.getNumPages();
            return;           
        }
        //Can't pay the bid - BANKRUPT! 
        numScissors = 0;
        score -= PENALTY_FOR_BANKRUPTING;        
    }
    
    
    private void captureNeighboringScissors()
    {
        for(int dir = 0; dir<360; dir +=45)
        {
            Location loc = getLocation().getAdjacentLocation(dir);
            if(loc.isValidLocation())
            {
                GameObject go = getGrid().get(loc);
                if(go instanceof Scissors)
                {
                    numScissors++;
                    go.removeSelfFromGrid();
                }
            }
        }
    }
    
    private void startRPSbattle(int choice)
    {
        //Find an opponent in the same Zone (if many, randomly select)
        ArrayList<Bot> otherBotsInZone = new ArrayList<Bot>();
        int myZoneR = getRow()/8;
        int myZoneC = getCol()/8;
        for(int r=myZoneR*8; r<(myZoneR+1)*8; r++)
            for(int c=myZoneC*8; c<(myZoneC+1)*8; c++)
            {
                GameObject go = getGrid().get(new Location(r,c));
                if(go instanceof Bot && !(getRow()==r && getCol()==c))
                {
                    otherBotsInZone.add((Bot)go);
                }
            }
        
        if(otherBotsInZone.isEmpty()) return; //no one to RPS battle.
        int i = (int)(Math.random()*otherBotsInZone.size());
        Bot otherBot = otherBotsInZone.get(i);
        
        //Call both RPS methods to get choices (it doesn't take away their turn) 
        int myChoice = this.chooseRPS();
        int oppChoice = otherBot.chooseRPS();
        
        //If it is a tie, it just ends. (Nothing happens)
        if(myChoice == oppChoice) 
        {
            //Give a message
            System.out.println("RPS Tie");
            return;
        }
        
        //There is a winner... 
        boolean didIWin = false;
        if(oppChoice == 0) //didn't have materials (or forfeited) 
            didIWin = true;
        else if((myChoice == botBrain.ROCK && oppChoice == botBrain.SCISSORS) || 
                (myChoice == botBrain.PAPER && oppChoice == botBrain.ROCK) || 
                (myChoice == botBrain.SCISSORS && oppChoice == botBrain.PAPER) )
            didIWin = true;
        
        System.out.println("RPS Battle "+this.botBrain.getName()+" win = "+didIWin);
        if(didIWin)
            this.addToScore(POINTS_FOR_RPS_WIN);
        else
            otherBot.addToScore(POINTS_FOR_RPS_WIN);
            
        //Is there an option to re-throw?
        
    }
    
    public int chooseRPS()
    {
        int choice = botBrain.chooseRPS();
        //This uses a material, if you don't have the material, you automatically lose! 
        if(choice == botBrain.ROCK && numRocks == 0) return 0;
        if(choice == botBrain.PAPER && numPapers == 0) return 0;
        if(choice == botBrain.SCISSORS && numScissors == 0) return 0;
        //Use the material
        if(choice == botBrain.ROCK ) numRocks--;
        if(choice == botBrain.PAPER ) numPapers--;
        if(choice == botBrain.SCISSORS ) numScissors--;
        return choice;
    }

    private void buildWall(Location loc)
    {
        if(numRocks > BUILD_COST)
        {
            //Build a wall at loc
            if(getGrid().isValid(loc) && !(getGrid().get(loc) instanceof Bot))
            {
                new Wall(getColor()).putSelfInGrid(getGrid(), loc);
                numRocks -= BUILD_COST;
            }
        }
    }
    
    @Override
    public String toString()
    {
        if(botBrain != null)
            return "Bot: "+botBrain.getName();
        else
            return "Bot!";
    }

    /**
     * Updates the most recent data (location, grid and status)
 information to the BotBrain.  This allows the BotBrain to make a decision
 based on current data every turn.  
     */
    private final void giveDataToBotBrain()
    {
        //score, energy, col, row, myStuff ================
        botBrain.setScore(score);
        botBrain.setLocation(getLocation().getCol(), getLocation().getRow());
        //====specific for 2021=====
        botBrain.setNumRocks(numRocks);
        botBrain.setNumPapers(numPapers);
        botBrain.setNumScissors(numScissors);
        //match stuff: bestScore, roundsWon ===================
        botBrain.setBestScore(this.calculateBestScore());
        botBrain.setRoundsWon(this.getRoundsWon());
        //world stuff: moveNumber, roundNumber ================
        botBrain.setMoveNumber(BotWorld.getMoveNum());
        botBrain.setRoundNumber(BotWorld.getRoundNum());

        //theArena!============================================
        int numRows = getGrid().getNumRows();
        int numCols = getGrid().getNumCols();
        GameObject[][] theArena = new GameObject[numRows][numCols];
        for(int row=0; row<numRows; row++)
        {
            for(int col=0; col<numCols; col++)
            {
                GameObject a = getGrid().get(new Location(row, col));
                if(a != null && !(a instanceof Trail))
                    theArena[row][col] = a.getClone();
                //Might need to do each with instanceof here...
                
            }
        }
        botBrain.setArena(theArena);
            
        
    } //end of giveDataToRatBot() -----------------------------
    
//    public int myScore()
//    {
//        int score =0;
//        return score;
//    }
    
    private int calculateBestScore()
    {
        int bestScore = getScore();
        ArrayList<Bot> rats = getGrid().getAllRats();
        for(Bot r : rats)
        {
            if(r.getScore() > bestScore)
            {
                bestScore = r.getScore();
            }
        }  
        return bestScore;
    }
        
    /**
     * Accessor method to get the BotBrain from a Bot.
     * @return the BotBrain 'brain' of this BotBrain.
     */
    public BotBrain getRatBot()
    {
        return botBrain;
    }

    /**
     * Gets the current score (from this round).  
     * @return the score
     */
    public int getScore() 
    { 
        int RPSsets = Math.min(numScissors, Math.min(numRocks, numPapers));
        int RPSpoints = POINTS_PER_RPS_SET*RPSsets + 
                        POINTS_PER_ROCK*numRocks + 
                        POINTS_PER_PAPER*numPapers + 
                        POINTS_PER_SCISSORS*numScissors;
        return score+RPSpoints; 
    }
    /**
     * Sets the current score of this Bot.
     * @param in the score
     */
    public void setScore(int in) { score = in; }
    /**
     * Adds the given amount to score of this Bot.  
     * @param add the amount to add
     */
    public void addToScore(int add) { score += add; }
    /**
     * Gets the total points scored over all rounds in this match for this Bot.
     * @return the total score
     */
    public int getTotalScore() { return totalScore; }

    /**
     * Gets the number of rounds won by this Bot in the match.
     * @return the rounds won
     */
    public int getRoundsWon() { return roundsWon; }
        
    /**
     * Sets the number of rounds won by this Bot in the match.
     * @param in the rounds won
     */
    public void setRoundsWon(int in) { roundsWon = in; }
    /**
     * Increases the number of rounds won in this match by this Bot by one.
     */
    public void increaseRoundsWon() { roundsWon++; }

    public int getAvgMSecUsed() { return mSecUsed/BotWorld.getMoveNum(); }
    public void increaseMSecUsed(int in) { mSecUsed += in; }
    
    // These methods are used for the RoundRobin tourney.
    public int getMatchesWon() { return matchesWon; }
    public int getMatchesTied() { return matchesTied; }
    public int getMatchesLost() { return matchesLost; }
    public void increaseMatchesWon() { matchesWon++; }
    public void increaseMatchesTied() { matchesTied++; }
    public void increaseMatchesLost() { matchesLost++; }
     /**
     * Initializes this Bot for a new round.  
     */
    public final void initialize()
    {
//        System.out.println("Bot:initialize()");
        totalScore += getScore();
        score = 0;
        mSecUsed = 0;
        numRocks = 0;
        numScissors = 0;
        numPapers = 0;
        botBrain.initForRound();
    }
    
    public void clearScores()
    {
        score = 0;
        roundsWon = 0;
        matchesWon = 0;
        matchesTied = 0;
        matchesLost = 0;
        totalScore = 0;
    }

    @Override
    public GameObject getClone()
    {
        GameObject clone = new Bot(this);
        return clone;
    }
    
    
}