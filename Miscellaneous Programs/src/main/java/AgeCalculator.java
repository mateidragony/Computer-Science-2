import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class AgeCalculator {

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex){ ex.printStackTrace(); }

        String date = JOptionPane.showInputDialog("What is their birthday? MM/DD/YYYY");

        String[] dateSplit = date.split("/");

        int month = Integer.parseInt(dateSplit[0]);
        int day = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);


        ZoneId zonedId = ZoneId.of( "America/Montreal" );
        LocalDate today = LocalDate.now( zonedId );

        int age = today.getYear()-year;

        if((today.getMonthValue()- month) < 0)
            age--;
        else if(today.getMonthValue() == month && today.getDayOfMonth() - day < 0)
            age--;

        JOptionPane.showMessageDialog(null,"Age: "+age);
    }

}
