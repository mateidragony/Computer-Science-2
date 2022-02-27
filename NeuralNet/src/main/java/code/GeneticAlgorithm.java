package code;

import java.util.List;

public class GeneticAlgorithm {

    final int trainingCycles = 100000;
    final int numFramesTesting = 100;
    final int numOffspring = 10;

    NeuralNetworkMultiLevel[] nnList = new NeuralNetworkMultiLevel[numOffspring];

    public static void main(String[] args){

        GeneticAlgorithm ga = new GeneticAlgorithm();

        ga.initNeuralNets();
        ga.nnEvolution();

    }


    public void initNeuralNets(){

        //learn rate not important, mutation rate is
        for(int i=0; i<numOffspring; i++){

            int[] nodes = {2,10,1};
            nnList[i] = new NeuralNetworkMultiLevel(nodes, 0);

        }

    }

    //equivalent method of train
    //returns the index of the best nn in nnList
    public int naturalSelection(){

        double leastError = 9999999;
        int bestIndex = 0;

        for(int index=0; index<nnList.length; index++){

            double error = calculateError(nnList[index]);
            if(error<leastError){
                leastError = error;
                bestIndex = index;
            }

        }

        return bestIndex;

    }


    //assuming nnList has already been initted
    public void nnEvolution(){

        for(int i=0; i<trainingCycles; i++){

            int best = naturalSelection();

            if(i == trainingCycles-1){
                displayResults(nnList[best]);
            }

            nnList = nnList[best].mutateOffspring(numOffspring);


            if(i%2000 == 0){
                System.out.println();
                System.out.println("After "+i+" iterations:");
                System.out.println("Average error: "+calculateError(nnList[best]));
            }

        }




    }




    public void displayResults(NeuralNetworkMultiLevel nn){

        double avgError = 0;
        int count = 0;

        for(double d[]:XORinputs)
        {

            List<Double> output = nn.predict(d).toArray();

            for(int i=0; i<output.size(); i++) {
                avgError += Math.abs(output.get(i) - XORoutputs[count][0]);
            }

            System.out.println((int)XORinputs[count][0]+"⊕"+(int)XORinputs[count][1]+" = "+output.toString());
            count++;
        }
        System.out.println("Average Error: "+avgError/XORoutputs.length);

    }





    //Only useful for the XOR neural net genetic algorithm
    public double calculateError(NeuralNetworkMultiLevel nn){

        double avgError = 0;
        int count = 0;

        for(double d[]:XORinputs)
        {

            List<Double> output = nn.predict(d).toArray();

            for(int i=0; i<output.size(); i++) {
                avgError += Math.abs(output.get(i) - XORoutputs[count][0]);
            }

            //System.out.println((int)XORinputs[count][0]+"⊕"+(int)XORinputs[count][1]+" = "+output.toString());
            count++;
        }
        //System.out.println("Average Error: "+avgError/XORoutputs.length);

        return avgError/XORoutputs.length;
    }









    private final double[][] XORinputs = {
            {0,0},
            {1,0},
            {0,1},
            {1,1}
    };
    private final double[][] XORoutputs = {
            {0},{1},{1},{0}
    };



}
