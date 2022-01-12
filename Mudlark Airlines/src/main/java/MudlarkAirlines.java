import java.util.ArrayList;

public class MudlarkAirlines {


    public static final int ticketsSold = 17;
    public static final int numTrials = 100000;

    private ArrayList<Integer> peopleInfo;

    public static ArrayList<Integer> netRevenue;
    public static ArrayList<Integer> revenueGained;

    public static ArrayList<Integer> numPeopleInfo;

    public MudlarkAirlines(){
        peopleInfo = new ArrayList<>();
        netRevenue = new ArrayList<>();
        revenueGained = new ArrayList<>();
        numPeopleInfo = new ArrayList<>();

        for(int i=0; i<ticketsSold; i++){
            numPeopleInfo.add(0);
        }
    }

    public void doComponent(){
        int rand = (int)(Math.random()*100);
        peopleInfo.add(rand);
    }

    public void doTrial(){

        for(int i=0; i<ticketsSold; i++){
            doComponent();
        }
        //printArray(peopleInfo);

        int showUp = 0;
        for(int person: peopleInfo){
            if(person > 7)
                showUp++;
        }

        int badBoy = 0;
        if(showUp > 15)
            badBoy = (showUp-15)*450;


        netRevenue.add(200*ticketsSold - badBoy);
        revenueGained.add( (200*ticketsSold - badBoy) - 200*15 );


        for(int i=0; i<numPeopleInfo.size(); i++){
            if(showUp == i+1)
                numPeopleInfo.set(i, numPeopleInfo.get(i)+1);
        }



        peopleInfo = new ArrayList<>();
    }


    public static void printArray(ArrayList<Integer> arr){

        for(int i: arr){
            System.out.print(i+", ");
        }
        System.out.println();
    }



    //Simulation done in the main method
    public static void main(String[] args){

        MudlarkAirlines sim = new MudlarkAirlines();

        for(int i=0; i<numTrials; i++){
            sim.doTrial();
        }

        int netSum = 0;
        for(int net: netRevenue){
            netSum+=net;
        }
        int avgNetRevenue = netSum/netRevenue.size();


        int gainSum = 0;
        for(int gain: revenueGained){
            gainSum+=gain;
        }
        int avgGainRevenue = gainSum/revenueGained.size();

        System.out.println();
        System.out.println("After "+ numTrials + " trials");
        System.out.println();
        System.out.println("Average Net Revenue : "+ avgNetRevenue);
        System.out.println("Average Net Gain (Revenue - 15*200) : "+ avgGainRevenue);


        System.out.println();
        for(int i=numPeopleInfo.size()-1; i>=0; i--){
            System.out.println(i+1+" : "+numPeopleInfo.get(i));
        }

        System.out.println();System.out.println();System.out.println();

        for(int i=0; i<ticketsSold; i++){
            System.out.print((int)(Math.random()*100)+", ");
        }
    }
}
