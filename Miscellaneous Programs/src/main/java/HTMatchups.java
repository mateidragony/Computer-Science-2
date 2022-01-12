import javax.swing.*;
import java.util.ArrayList;

public class HTMatchups {


    public static void main(String[] args){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex){ ex.printStackTrace(); }


        ArrayList<String> players = new ArrayList<>();
        players.add("Matei"); players.add("Kaushal"); players.add("Jack");
        players.add("Bryce"); players.add("Emi");

        String notPlaying = JOptionPane.showInputDialog("Who is not playing?");

        players.remove(notPlaying);

        int p1 = (int)(Math.random()*4);
        int p2 = (int)(Math.random()*3);

        System.out.println("Match Up 1: " + players.remove(p1) + " vs. "+ players.remove(p2));
        System.out.println("Match Up 2: " + players.get(0) + " vs. "+ players.get(1));


    }

}
