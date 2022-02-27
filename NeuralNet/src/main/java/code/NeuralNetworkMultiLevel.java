package code;
//From: https://towardsdatascience.com/understanding-and-implementing-neural-networks-in-java-from-scratch-61421bb6352c

import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkMultiLevel {
	
    List<Matrix> weights, bias;	
    double l_rate=0.01;

    double m_rate=0.1;

    public NeuralNetworkMultiLevel(int[] nodesPerLevel, double learnRate) {

        weights = new ArrayList<>();
        bias = new ArrayList<>();
        for(int z=1; z<nodesPerLevel.length; z++)
        {
            weights.add(new Matrix(nodesPerLevel[z],nodesPerLevel[z-1]));
            bias.add(new Matrix(nodesPerLevel[z],1));
        }
        l_rate = learnRate;
    }


    public NeuralNetworkMultiLevel(List<Matrix> weights, List<Matrix> bias){
        this.weights = weights;
        this.bias = bias;
    }





    public Matrix predict(double[] X)
    {
        Matrix input = Matrix.fromArray(X);
        Matrix result = input;
        for(int z=0; z<weights.size(); z++)
        {
            result = Matrix.multiply(weights.get(z), result);
            result.add(bias.get(z));
            result.sigmoid();
        }
        return result;
    }
    public List<Matrix> predictAllLayers(double[] X)
    {
        Matrix input = Matrix.fromArray(X);
        List<Matrix> result = new ArrayList<Matrix>();
        result.add(input);

        for(int z=0; z<weights.size(); z++)
        {
            Matrix newResult = Matrix.multiply(weights.get(z), result.get(z));
            newResult.add(bias.get(z));
            newResult.sigmoid();
            result.add(newResult);
        }

        return result;
    }


    public void fit(double[][]X,double[][]Y,int epochs)
    {
        for(int i=0;i<epochs;i++)
        {	
            int sampleN =  (int)(Math.random() * X.length );
            this.train(X[sampleN], Y[sampleN]);
            if(i%5000 == 0)
            {
                System.out.println("After "+i+" iterations.");
                displayNmistResults(X,Y);
//                for(double d[]:X)
//                {
//                    List<Double> output = predict(d).toArray();
//                    System.out.println(output.toString());
//                }		

            }
        }
    }

    public void train(double [] X,double [] Y)
    { //The comments will pretend the network layers are 4,3,2,1
        Matrix input = Matrix.fromArray(X);//[4x1] this line not needed...
        List<Matrix> eachLayerOutput = predictAllLayers(X);//[4x1][3x1][2x1][1x1]

        Matrix target = Matrix.fromArray(Y);//[1,1]
        Matrix finalOutput = eachLayerOutput.get(eachLayerOutput.size()-1);//[1,1]
        Matrix error = Matrix.subtract(target,finalOutput);//[1,1]
        Matrix gradient = finalOutput.dsigmoid();//[1,1]

        for(int z=weights.size()-1; z>=0; z--)
        {
            gradient.multiply(error);//[1,1]
            gradient.multiply(l_rate);//[1,1]

            Matrix hidden = eachLayerOutput.get(z); //[2x1]
            //change hidden to be the 'eachLayerOutput'
            Matrix hidden_T = Matrix.transpose(hidden);//becomes[1x2]
            Matrix who_delta =  Matrix.multiply(gradient, hidden_T);//[1x1][1x3]=[1x2]
            weights.get(z).add(who_delta);//[1x2]
            bias.get(z).add(gradient);//[1x1]

            target = Matrix.transpose(weights.get(z));//becomes[2x1]
            error = Matrix.multiply(target, error);//[2x1][1x1]=[2x1]
            gradient = hidden.dsigmoid();//[2x1]
        }

    }

    public void displayNmistResults(double[][] inputArray, double[][] expectedOutputArray)
    {
        int numCorrect = 0;
        for(int q=0; q<inputArray.length; q++)
        {
            double d[] = inputArray[q];
            List<Double> output = this.predict(d).toArray();
            int best = 0;
            for(int n=0; n<output.size(); n++)
            {
                if(output.get(n) > output.get(best))
                    best = n;
            }
            
            if(expectedOutputArray[q][best] >=.9)
                numCorrect++;
        }		                        
        System.out.println(numCorrect+" out of "+inputArray.length);
    }


    public void printBowels(){

        for(int i=0; i<weights.size();i++){
            System.out.println("Weights: "+i);
            weights.get(i).print();
        }

        for(int i=0; i<bias.size();i++){
            System.out.println("Bias: "+i);
            bias.get(i).print();
        }

    }





    public NeuralNetworkMultiLevel[] mutateOffspring(int num){

        NeuralNetworkMultiLevel[] offspring = new NeuralNetworkMultiLevel[num];

        for(int index=0; index<offspring.length; index++){
            List<Matrix> mutatedWeights = new ArrayList<>();
            List<Matrix> mutatedBias= new ArrayList<>();

            for(Matrix weight: weights){

                double[][] data = new double[weight.rows][weight.cols];

                for(int r=0; r<weight.rows; r++){
                    for(int c=0; c<weight.cols; c++){
                        data[r][c] = weight.data[r][c] * ((Math.random()*m_rate)-(m_rate/2.0));
                    }
                }

                mutatedWeights.add(new Matrix(data));
            }


            for(Matrix b: bias){

                double[][] data = new double[b.rows][b.cols];

                for(int r=0; r<b.rows; r++){
                    for(int c=0; c<b.cols; c++){
                        data[r][c] = b.data[r][c] * ((Math.random()*m_rate)-(m_rate/2.0));
                    }
                }

                mutatedBias.add(new Matrix(data));
            }

            offspring[index] = new NeuralNetworkMultiLevel(mutatedWeights,mutatedBias);

        }

        return offspring;

    }


}