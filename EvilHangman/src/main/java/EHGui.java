import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class EHGui extends JPanel implements KeyListener {

    final int width = 1000, height = 750;

    private ArrayList<Image> imgs;

    private ArrayList<String> allLetterCombos;
    private ArrayList<String> validWords;

    private String correctGuesses;
    private boolean wonGame;
    private int lives;
    private String typedKey = " ";
    private ArrayList<String> previousGuesses;
    private boolean entered;
    private int length;

    public EHGui(){
        setPreferredSize(new Dimension(width,height));
        this.addKeyListener(this);

        Toolkit t = Toolkit.getDefaultToolkit();
        imgs = new ArrayList<>();
        for(int i=0; i<20; i++)
            imgs.add(t.getImage("EvilHangman/src/main/imgs/img"+i+".png"));
        imgs.add(t.getImage("EvilHangman/src/main/imgs/img"+19+".png"));

        initGame();
    }

    public void paintComponent(Graphics g){

        g.setColor(new Color(186, 182, 182));
        g.fillRect(0,0,width,height);

        if(!previousGuesses.isEmpty())
            printPreviousGuesses(g);

        if(isGuessValid()){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced",Font.BOLD, 30));
            g.drawString("Press Enter", width/2+80, height/2+150);
        }

        if(lives>0 && correctGuesses.contains("_")) {
            g.setColor(Color.black);
            g.setFont(new Font("Monospaced", Font.BOLD, 60));
            g.drawString(typedKey.toUpperCase(), width / 2 - 30, height / 2+150);
        } else {
            g.setColor(Color.black);
            g.setFont(new Font("Monospaced", Font.BOLD, 60));
            g.drawString("Press Enter to Play Again", 45, height / 2+50);
            printPossible(g);
        }

        drawStuff(g);

        if(lives<21)
            g.drawImage(imgs.get(20-lives),width-300,0,300,230,this);
       // g.fillRect(width-300,0,300,230);

    }



    //-------------------------------------------------
    // Gui Helper Methods
    //-------------------------------------------------

    public void initGame(){
        previousGuesses = new ArrayList<>();
        entered = false;
        length = (int)(Math.random()*10)+3;

        //length=3;

        lives = 20;
        wonGame = false;
        correctGuesses = "";

        for(int i=0;i<length-1;i++)
            correctGuesses = correctGuesses.concat("_ ");
        correctGuesses = correctGuesses.concat("_");

        validWords = readFile(new File("EvilHangman/src/main/java/engmix.txt"));
        validWords = onlyWordLength(validWords,length);

        allLetterCombos = allLetterCombos(length);

        typedKey = " ";
    }

    public void printPreviousGuesses(Graphics g){

        String str = "Wrong Letters:";

        for(int i=0; i<previousGuesses.size()-1;i++)
            str = str.concat(previousGuesses.get(i)+",");
        str += previousGuesses.get(previousGuesses.size()-1);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced",Font.BOLD, 30));
        g.drawString(str,width/2-10-str.length()*9,height-70);
    }

    public boolean isGuessValid(){
        return Character.isLetter(typedKey.charAt(0)) && !previousGuesses.contains(typedKey.toUpperCase())
                    && !correctGuesses.contains(typedKey.toUpperCase());
    }

    public void drawStuff(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font("Monospaced",Font.BOLD, 60));
        g.drawString(correctGuesses, width/2-10-correctGuesses.length()*18, 200+120);

        g.setFont(new Font("Monospaced",Font.BOLD, 40));
        g.drawString("Lives: "+lives, 50,50);

        g.setFont(new Font("Monospaced",Font.BOLD, 25));
        g.drawString("Possible Words: "+validWords.size(), 50,100);
    }

    public void printPossible(Graphics g){

        String possibilities = "Possible Word(s):";
        for(int i=0;i<validWords.size()-1;i++)
            possibilities = possibilities.concat(validWords.get(i).toUpperCase()+",");
        possibilities+=validWords.get(validWords.size()-1).toUpperCase();

        g.setColor(Color.black);
        g.setFont(new Font("Monospaced",Font.BOLD, 30));
        g.drawString(possibilities, width/2-10-possibilities.length()*9, 500+100);

    }






    public static void main(String[] args){

        JFrame myFrame = new JFrame("Evil Hangman!");
        EHGui eh = new EHGui();

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.add(eh);
        myFrame.setSize(eh.getPreferredSize());

        Timer t = new Timer(1000/60, e -> myFrame.getComponent(0).repaint());
        t.start();

        myFrame.addKeyListener(eh);
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }



    //-------------------------------------------------
    // Key Listener Methods
    //-------------------------------------------------

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {

        if(Character.isLetter(e.getKeyChar()))
            typedKey = Character.toString(e.getKeyChar());

        if(isGuessValid() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            runGame();
        }

        else if(!(lives>0 && correctGuesses.contains("_")) && e.getKeyCode() == KeyEvent.VK_ENTER)
            initGame();


    }
    public void keyReleased(KeyEvent e) {}



    //-------------------------------------------------
    // Dictionary Editing Methods
    //-------------------------------------------------


    public ArrayList<String> readFile(File file){
        ArrayList<String> fileArray = new ArrayList<>();

        Scanner fileReader = new Scanner(System.in);

        try{
            fileReader = new Scanner(file);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }

        while(fileReader.hasNextLine()){
            fileArray.add(fileReader.nextLine().toLowerCase());
        }

        return fileArray;
    }


    public ArrayList<String> onlyWordLength(ArrayList<String> words, int length){

        ArrayList<String> newWords = new ArrayList<>();

        for(String s:words){
            if(s.length()==length)
                newWords.add(s);
        }

        return newWords;
    }

    public ArrayList<String> allLetterCombos(int length){
        ArrayList<String> list = new ArrayList<>();

        for(int i=0;i<Math.pow(2,length);i++){
            String bits = Integer.toBinaryString(i);
            bits = String.format("%"+length+"s", bits).replaceAll(" ", "0");
            list.add(bits);
        }
        return list;
    }





    //-------------------------------------------------
    // Evil Hangman Methods
    //-------------------------------------------------

    public ArrayList<String> remove(ArrayList<String> words, String letter){
        ArrayList<String> newWords = new ArrayList<>();

        for(String s:words){
            if(!s.contains(letter))
                newWords.add(s);
        }

        return newWords;
    }

    public ArrayList<String> keep(ArrayList<String> words, String letter){
        ArrayList<String> newWords;
        ArrayList<ArrayList<String>> wordsAllCombos = new ArrayList<>();

        for(String combo:allLetterCombos){
            ArrayList<String> wordsForCombo = new ArrayList<>();

            for(String s:words){
                boolean isGood = true;

                for(int i=0;i<s.length();i++){
                    if(combo.charAt(i)=='1' && s.charAt(i)!=letter.charAt(0)
                            || combo.charAt(i)=='0' && s.charAt(i)==letter.charAt(0)){
                        isGood = false;
                        break;
                    }
                }

                if(isGood)
                    wordsForCombo.add(s);

            }
            wordsAllCombos.add(wordsForCombo);
        }

        int biggestSize = 0;
        int bestIndex = 0;
        for(int i=0; i<wordsAllCombos.size();i++){

            if(wordsAllCombos.get(i).size()>biggestSize){
                biggestSize = wordsAllCombos.get(i).size();
                bestIndex=i;
            }

        }

        newWords = wordsAllCombos.get(bestIndex);

        String combo = allLetterCombos.get(bestIndex);
        char[] guesses = correctGuesses.toCharArray();

        for(int i=0;i<combo.length();i++){
            if(combo.charAt(i)=='1')
                guesses[i*2]=letter.toUpperCase().charAt(0);
        }

        correctGuesses = new String(guesses);

        return newWords;
    }

    public void playEvil(){
        lives--;

        String oldCorrectGuesses = correctGuesses;

        ArrayList<String> removed = remove(validWords,typedKey);
        ArrayList<String> kept = keep(validWords,typedKey);

        if(removed.size()>=kept.size()){
            validWords = removed;
            correctGuesses = oldCorrectGuesses;
            previousGuesses.add(typedKey.toUpperCase());
        }else{
            validWords = kept;
            lives++;
        }
    }
    public void playRegular(){
        if(validWords.get(0).contains(typedKey)){

            char[] guess = correctGuesses.toCharArray();
            for(int i=0; i<validWords.get(0).length();i++) {
                if(validWords.get(0).charAt(i)==typedKey.charAt(0))
                    guess[i*2] = typedKey.toUpperCase().charAt(0);
            }
            correctGuesses = new String(guess);
            lives++;

        } else
            previousGuesses.add(typedKey.toUpperCase());

        lives--;

        if(!correctGuesses.contains("_"))
            wonGame = true;
    }

    public void runGame(){
        if (validWords.size() > 1)
            playEvil();
        else if (!wonGame)
            playRegular();

        entered = false;
    }
}
