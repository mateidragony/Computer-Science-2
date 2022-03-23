package code;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneticAlgorithm {

    final int trainingCycles = 10000;
    final int numFramesTesting = 200;
    final int numOffspring = 5;
    final double mutationRate = 5;

    final int numPrints = 50;

    static final int dataSet = 3;

    int bestScore;


    ShooterGame sg = new ShooterGame();

    NeuralNetworkMultiLevel[] nnList = new NeuralNetworkMultiLevel[numOffspring];

    public static void main(String[] args){

        GeneticAlgorithm ga = new GeneticAlgorithm();

        ga.initNeuralNets(dataSet);
        ga.nnEvolution(dataSet);

    }


    public void initNeuralNets(int dataSet){

        int[] nodes;

        if(dataSet==1)
            nodes = new int[]{2, 10, 1};
        else if(dataSet == 2) {
            generateAddCombos(20);
            nodes = new int[]{2,10,1};
        } else if(dataSet == 3){
            nodes = new int[]{2,10,3};
        }
        else
            nodes = null;


        //learn rate not important, mutation rate is
        for(int i=0; i<numOffspring; i++){

            assert nodes != null;
            nnList[i] = new NeuralNetworkMultiLevel(nodes, 0,mutationRate);

        }

    }

    //equivalent method of train
    //returns the index of the best nn in nnList
    public int naturalSelection(int dataSet){

        int bestIndex = 0;

        if(dataSet!=3) {
            double leastError = 9999999;

            for (int index = 0; index < nnList.length; index++) {

                double error = calculateError(nnList[index], dataSet);
                if (error < leastError) {
                    leastError = error;
                    bestIndex = index;
                }

            }
        } else{
            bestScore = 0;

            for (int index = 0; index < nnList.length; index++) {

                int score = getGameScore(nnList[index]);
                if (score > bestScore) {
                    bestScore = score;
                    bestIndex = index;
                }

            }
        }

        return bestIndex;
    }


    //assuming nnList has already been initted
    public void nnEvolution(int dataSet){

        NeuralNetworkMultiLevel bestNN = null;

        int printEvery = trainingCycles/numPrints;

        for(int i=0; i<trainingCycles; i++){

            int best = naturalSelection(dataSet);
            bestNN = nnList[best];

            nnList = bestNN.mutateOffspring(numOffspring);

//            if(bestScore == 0 && dataSet == 3)
//                bestNN.setMutation(100);
//            else
                bestNN.setMutation(mutationRate);


            if(i%printEvery == 0){
                System.out.println();
                System.out.println("After "+i+" iterations ("+ (((double)i/trainingCycles)*100)+ "%):");
                if(dataSet!=3)
                    System.out.println("Average error: "+calculateError(bestNN,dataSet));
                else
                    System.out.println("Score: "+bestScore);
            }

        }

        System.out.println();
        System.out.println();
        System.out.println("========After "+trainingCycles+" iterations========");
        System.out.println();
        displayResults(bestNN,dataSet);
        System.out.println();

//        try {
//            NeuralNetworkMultiLevel loaded = loadNN("geneticAdder");
//            loaded.printBowels();
//        }catch(FileNotFoundException ex){
//            ex.printStackTrace();
//        }

        Scanner s = new Scanner(System.in);
        if(s.nextLine().equals("save"))
            saveNN(bestNN, s.nextLine());

    }


    public void displayResults(NeuralNetworkMultiLevel nn, int dataSet){

        double avgError = 0;
        int count = 0;

        if(dataSet == 1) {
            for (double d[] : XORinputs) {

                List<Double> output = nn.predict(d).toArray();

                for (Double aDouble : output) {
                    avgError += Math.abs(aDouble - XORoutputs[count][0]);
                }

                System.out.println((int) XORinputs[count][0] + " xor " + (int) XORinputs[count][1] + " = " + output.toString());
                count++;
            }
            System.out.println("Average Error: " + avgError / XORoutputs.length);
        } else if(dataSet == 2){
            for (double d[] : AddInputs) {

                List<Double> output = nn.predict(d).toArray();

                for (Double aDouble : output) {
                    avgError += Math.abs(aDouble - AddOutputs[count][0]);
                }

                System.out.println((int) AddInputs[count][0] + "+" + (int)AddInputs[count][1]
                        + " = " + output.get(0)*10 + " = " + AddOutputs[count][0]*10);
                count++;
            }
            System.out.println("Average Error: " + avgError / AddOutputs.length);

        } else if(dataSet == 3){

            System.out.println("Score: "+getGameScore(nn));
            Matrix m = nn.predict(new double[]{sg.playerX,sg.playerY,sg.enemyX,sg.enemyY});
            List<Double> out = m.toArray();

            System.out.println("MyX: "+sg.playerX);
            System.out.println("Right: "+out.get(0));
            System.out.println("Left: "+out.get(1));
            System.out.println("Shoot: "+out.get(2));
        }

    }



    public int getGameScore(NeuralNetworkMultiLevel nn){

        sg=new ShooterGame();

        for(int i=0; i<numFramesTesting;i++){

            Matrix m = nn.predict(new double[]{sg.playerX,sg.enemyX});
            List<Double> out = m.toArray();

            sg.right = out.get(0)>0.5;
            sg.left = out.get(1)>0.5;
            sg.shoot = out.get(2)>0.5;

            sg.animate();
        }

        return sg.score;
    }



    //Only useful for the XOR neural net genetic algorithm
    public double calculateError(NeuralNetworkMultiLevel nn,int dataSet){

        double avgError = 0;
        int count = 0;

        if(dataSet == 1) {

            for (double d[] : XORinputs) {

                List<Double> output = nn.predict(d).toArray();

                for (Double out : output) {
                    avgError += Math.abs(out - XORoutputs[count][0]);
                }

                //System.out.println((int)XORinputs[count][0]+"⊕"+(int)XORinputs[count][1]+" = "+output.toString());
                count++;
            }
            //System.out.println("Average Error: "+avgError/XORoutputs.length);

            return avgError / XORoutputs.length;
        } else if(dataSet == 2){

            for(double d[]: AddInputs){

                List<Double> output = nn.predict(d).toArray();

                for (Double aDouble : output) {
                    avgError += Math.abs(aDouble - AddOutputs[count][0]);
                }

                count++;
            }

            return avgError / AddOutputs.length;

        }

        return 0;
    }



    public void generateAddCombos(int num){

        double[][] inputs = new double[num][2];
        double[][] outputs = new double[num][1];

        for(int i=0; i<num; i++){

            int first = (int)(Math.random()*10);
            int second = (int)(Math.random()*(10-first));

            //System.out.println("{"+first+","+second+"} = {"+(first+second)+"}");

            inputs[i] = new double[]{first, second};
            outputs[i][0] = (first+second)/10.0;

        }

        AddInputs = inputs;
        AddOutputs  = outputs;

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

    private double[][] AddInputs;
    private double[][] AddOutputs;


    public static void saveNN(NeuralNetworkMultiLevel nn, String fileName){

        //Weights separated by comma
        //Biases separated by semicolon

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("NeuralNet/src/main/java/data/saves/"+fileName+".txt"));

            List<Matrix> weights = nn.weights;
            List<Matrix> bias = nn.bias;

            for(Matrix m: weights) {

                double[][] data = m.data;

                for(double[] dList: data){
                    for(double d: dList){
                        writer.write(d+",");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }

            for(Matrix m: bias) {

                double[][] data = m.data;

                for(double[] dList: data){
                    for(double d: dList){
                        writer.write(d+";");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }

            writer.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public static NeuralNetworkMultiLevel loadNN(String fileName) throws FileNotFoundException {

        File file = new File("NeuralNet/src/main/java/data/saves/"+fileName+".txt");
        Scanner s = new Scanner(file);

        List<Matrix> weights = new ArrayList<>();
        List<Matrix> biases = new ArrayList<>();

        List<List<Double>> rows = new ArrayList<>();
        boolean isWeight = true;

        while(s.hasNextLine()){

            String line = s.nextLine();

            if(!line.isEmpty()){

                if(line.split(",").length != 1){
                    isWeight = true;
                    String[] ws = line.split(",");
                    List<Double> doubles = new ArrayList<>();

                    for(String w:ws){
                        if(w.contains(","))
                            w=w.replaceAll(",","");
                        doubles.add(Double.parseDouble(w));
                    }
                    rows.add(doubles);
                }else{
                    isWeight = false;
                    String[] ws = line.split(";");
                    List<Double> doubles = new ArrayList<>();

                    for(String w:ws){
                        if(w.contains(","))
                            w=w.replaceAll(";","");
                        doubles.add(Double.parseDouble(w));
                    }
                    rows.add(doubles);
                }


            }else {
                if(!rows.isEmpty()) {
                    Matrix data = new Matrix(rows);

                    if (isWeight)
                        weights.add(data);
                    else
                        biases.add(data);

                    rows = new ArrayList<>();
                }
            }
        }


        return new NeuralNetworkMultiLevel(weights,biases);

    }

}
