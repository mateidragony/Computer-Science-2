package code;

import MnistLoader.MnistDataReader;
import MnistLoader.MnistMatrix;
import picture.SimpleDisplayFrame;

import java.io.IOException;
import java.util.List;

public class Driver {
	
    public static void main(String[] args) {

        int dataSet = 4;

        Driver wallE = new Driver();
        //wallE.generateAddCombos(20);
        wallE.loadTrainingData(dataSet);
        wallE.trainNetwork(dataSet);
    }

    public double [][] inputArray;
    public double [][] expectedOutputArray;
    int[] nodesPerLevel = {1,2,3}; //This is changed below...
    int numTrainingCycles = 1000000;
    double learnRate = 0.0011;  //0.011
    
    public void trainNetwork(int dataSet)
    {
        NeuralNetworkMultiLevel nn = new NeuralNetworkMultiLevel(nodesPerLevel,learnRate);

        nn.fit(inputArray, expectedOutputArray, numTrainingCycles);

        if(dataSet != 3)
            displayResults(nn);
        else
            displayNmistResults(nn);
    }
    
    public void displayResults(NeuralNetworkMultiLevel nn)
    {
        System.out.println();
        System.out.println("====FINAL OUTPUTS AFTER "+numTrainingCycles+" CYCLES====");
        for(double d[]:inputArray)
        {
            List<Double> output = nn.predict(d).toArray();
            for(int i=0; i<output.size(); i++)
                output.set(i,output.get(i)*10);
            System.out.println(output.toString());
        }

        System.out.println();
        System.out.println("====BONUS OUTPUT AFTER "+numTrainingCycles+" CYCLES====");

        double avgError = 0;
        int count = 0;

        for(double d[]:AddBonus)
        {

            List<Double> output = nn.predict(d).toArray();
            for(int i=0; i<output.size(); i++) {
                output.set(i, output.get(i) * 10);
                avgError += Math.abs(output.get(i) - (AddBonus[count][0]+AddBonus[count][1]));
            }
            System.out.println((int)AddBonus[count][0]+"+"+(int)AddBonus[count][1]+" = "+output.toString());
            count++;
        }
        System.out.println("Average Error: "+avgError/AddBonus.length);


    }
    public void displayNmistResults(NeuralNetworkMultiLevel nn)
    {
        System.out.println("====FINAL OUTPUTS AFTER "+numTrainingCycles+" CYCLES====");
        int numCorrect = 0;
        double[][] outputArray = new double[inputArray.length][10];
        for(int q=0; q<inputArray.length; q++)
        {
            double d[] = inputArray[q];
            List<Double> output = nn.predict(d).toArray();
            for(int n=0; n<output.size(); n++)
            {
                outputArray[q][n] = output.get(n);
            }
            int best = 0;
            for(int n=0; n<output.size(); n++)
            {
                if(output.get(n) > output.get(best))
                    best = n;
            }
            
            if(expectedOutputArray[q][best] >=.9)
                numCorrect++;
            if(Math.random() < .00005)
            {
                System.out.println("=== #"+q+" ===");
                System.out.print("Expected ");
                for(double val : expectedOutputArray[q])
                {
                    System.out.print((int)(val*100)+",");
                } System.out.println("**");
                System.out.print("Output ");
                for(double val : output)
                {
                    System.out.print((int)(val*100)+",");
                } System.out.println("**");
                
            }
        }		                        
        System.out.println(numCorrect+" out of "+inputArray.length);
        
        SimpleDisplayFrame bob = new SimpleDisplayFrame();
        bob.loadData(inputArray, expectedOutputArray, outputArray);
    }
        
    public void loadTrainingData(int dataSet) 
    {
        if(dataSet == 1)
        {
            inputArray = XORinputs;
            expectedOutputArray = XORoutputs;
            int[] nodes = {2,10,1};
            nodesPerLevel = nodes;
        }
        
        if(dataSet == 2)
        {
            inputArray = MuxInputs;
            expectedOutputArray = MuxOutputs;
            int[] nodes = {3,10,6,1};
            nodesPerLevel = nodes;
        }

        if(dataSet == 3)
        {
            int[] nodes = {784,50,50,50,10};
            nodesPerLevel = nodes;        
            try
            {
                MnistMatrix[] mnistMatrices = new MnistDataReader().readData("NeuralNet/src/main/java/data/train-images.idx3-ubyte", "NeuralNet/src/main/java/data/train-labels.idx1-ubyte");
                inputArray = new double[mnistMatrices.length][28*28];
                expectedOutputArray = new double[mnistMatrices.length][10];
                for(int z=0; z<mnistMatrices.length; z++)
                {
                    inputArray[z] = mnistMatrices[z].getDataInArrayForm();
                    int expectedOutput = mnistMatrices[z].getLabel();
                    for(int out=0; out<10; out++)
                    {
                        if(out == expectedOutput)
                            expectedOutputArray[z][out] = 1;
                        else
                            expectedOutputArray[z][out] = 0;
                    }
                }
            }
            catch(IOException e)
            {
                System.out.println("Dictionary file not found!!!  Yikes! ");
            }  
        }
        if(dataSet == 4)
        {
            generateAddCombos(1000);
           // inputArray = AddInputs;
           // expectedOutputArray = AddOutputs;

            int[] nodes = {2,10,1};
            nodesPerLevel = nodes;
        }

        
//        System.out.println("++++++++++++++++++");
//        for(int z=0; z<5; z++)
//        {
//            double[] row = inputArray[z];
//            for(double val : row)
//                System.out.print(val+",");
//            System.out.println("**");
//        }
    }

    public void generateAddCombos(int num){

        double[][] inputs = new double[num][2];
        double[][] outputs = new double[num][1];

        for(int i=0; i<num; i++){

            int first = 10-(int)(Math.random()*20);
            int second = (int)(Math.random()*9)-(first);

            //System.out.println("{"+first+","+second+"} = {"+(first+second)+"}");

            inputs[i] = new double[]{first, second};
            outputs[i][0] = (first+second)/10.0;

        }

        inputArray = inputs;
        expectedOutputArray  = outputs;

    }


    
    private double[][] XORinputs = {
                    {0,0},
                    {1,0},
                    {0,1},
                    {1,1}
        };   
    private double[][] XORoutputs = {
                    {0},{1},{1},{0}
    };
    
    private double[][] MuxInputs = {
                    {0,0,0},
                    {0,0,1},
                    {0,1,0},
                    {0,1,1},
                    {1,0,0},
                    {1,0,1},
                    {1,1,0},
                    {1,1,1}
    };
    
    private double[][] MuxOutputs = {
                    {0},{0},{0},{0},{0},{1},{0},{1}
    };

    private double[][] AddInputs = {
                    {1,2},
                    {5,3},
                    {1,8},
                    {3,6},
                    {2,6},
                    {3,4},
                    {4,3},
                    {2,-2}
    };
    private double[][] AddOutputs = {
            {.3},{.8},{.9},{.9},{.8},{.7},{.7},{0}
    };
    private double[][] AddBonus = {
                    {3,2},
                    {3,5},
                    {6,2},
                    {7,1},
                    {-2,6},
                    {3,0},
                    {6,0},
                    {7,-1}
    };
}