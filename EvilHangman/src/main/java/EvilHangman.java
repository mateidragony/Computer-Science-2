import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class EvilHangman extends JPanel {

    private ArrayList<String> validWords;
    private final ArrayList<String> previousGuesses;
    private ArrayList<String> allLetterCombos;
    private final Scanner s;
    private String correctGuesses;
    private boolean wonGame;
    private int lives = 10;

    public EvilHangman(){
        validWords = readFile(new File("EvilHangman/src/main/java/engmix.txt"));
        s = new Scanner(System.in);
        correctGuesses="";
        previousGuesses = new ArrayList<>();
        wonGame = false;
    }

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


    /*
        Loops through every word letter combination
        example: 101100 is combo letter is "a" would give a word with
        a_aa__. Blanks are any letter that isn't "a"
        Then loops through every valid word and checks if it follows
        the combination pattern. If it does, then adds it to the list
        of lists of valid words that follow a specific combination
        Then loop through that list to find best list and that will be returned
    */

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
                guesses[i*2]=letter.charAt(0);
        }

        correctGuesses = new String(guesses);

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


    public ArrayList<String> remove(ArrayList<String> words, String letter){
        ArrayList<String> newWords = new ArrayList<>();

        for(String s:words){
            if(!s.contains(letter))
                newWords.add(s);
        }

        return newWords;
    }


    public void playEvil(){
        System.out.println("Enter a letter");
        String input = s.nextLine();

        if(input.equals("print")){
            for(String s: validWords)
                System.out.println(s);
            System.exit(0);
        }

        while(input.length()!=1 || previousGuesses.contains(input)){
            System.out.println("Enter a VALID letter");
            input = s.nextLine();
        }

        lives--;

        String oldCorrectGuesses = correctGuesses;

        ArrayList<String> removed = remove(validWords,input);
        ArrayList<String> kept = keep(validWords,input);

        if(removed.size()>=kept.size()){
            validWords = removed;
            correctGuesses = oldCorrectGuesses;
            previousGuesses.add(input);
        }else{
            validWords = kept;
            lives++;
        }

    }
    public void playRegular(){

        System.out.println("Enter a letter");
        String input = s.nextLine();

        if(input.equals("print")){
            for(String s: validWords)
                System.out.println(s);
            System.exit(0);
        }

        while(input.length()!=1 || previousGuesses.contains(input)){
            System.out.println("Enter a VALID letter");
            input = s.nextLine();
        }


        if(validWords.get(0).contains(input)){

            char[] guess = correctGuesses.toCharArray();
            for(int i=0; i<validWords.get(0).length();i++) {
                if(validWords.get(0).charAt(i)==input.charAt(0))
                    guess[i*2] = input.charAt(0);
            }
            correctGuesses = new String(guess);
            lives++;

        } else
            previousGuesses.add(input);

        lives--;

    }


    public void runGame(){

        int randLength = (int)(Math.random()*8)+3;


        validWords = onlyWordLength(validWords,randLength);
        for(int i=0;i<randLength;i++)
            correctGuesses = correctGuesses.concat("_ ");
        allLetterCombos = allLetterCombos(randLength);

        //System.out.println("Length: "+randLength);
        System.out.println("Valid Words Size: "+validWords.size());
        System.out.println(correctGuesses);

        while(validWords.size()>1){
            playEvil();
            System.out.println("Valid Words Size: "+validWords.size());

            System.out.println("Lives left: "+lives);

            String prevGuesses = "Incorrect Guesses: ";
            for(String s:previousGuesses){
                prevGuesses = prevGuesses.concat(s+" ");
            }
            System.out.println(prevGuesses);

            System.out.println(correctGuesses);
        }
        while(!wonGame){
            playRegular();

            System.out.println("Lives left: "+lives);

            String prevGuesses = "Incorrect Guesses: ";
            for(String s:previousGuesses){
                prevGuesses = prevGuesses.concat(s+" ");
            }
            System.out.println(prevGuesses);

            System.out.println(correctGuesses);
            if(!correctGuesses.contains("_"))
                wonGame = true;
        }

        System.out.println();
        System.out.println("You Win!!!");
        System.out.println("You only have "+lives+" lives left..");
    }


    public static void main(String[] args){
        EvilHangman eh = new EvilHangman();

        eh.runGame();
    }

}
