import java.util.ArrayList;

public class TrialsTribbleations {


    public static void main(String[] args){

        ArrayList<Integer> tribbles = new ArrayList<>();
        tribbles.add(1);

        int day=0;

        while(tribbles.size() < 1000000000){

            for(int ind=0; ind<tribbles.size(); ind++){

                int tribble = tribbles.get(ind);

                for(int i=0; i<tribble+1; i++){
                    tribbles.add(1);
                }

                tribble++;
            }

        }

    }


}
