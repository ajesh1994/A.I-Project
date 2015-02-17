package src.playerTypes.ai_2;


/*
*WE DID NOT WRITE THIS CLASS. WE BORROWED IT FROM: http://www.public.iastate.edu/~cstras/cs572project/NeuralNet.java
*/


/*
 * Created on Dec 1, 2003
 * @author Shane Oldenburger
 */
import java.io.*;
import java.util.*;



public class NeuralNet {
	double output = 0.0;
	double[] weights;
	double[] hidden;
	double[][] hiddenWeights;
	double[] input;
	int inputSize = 0;
	int hiddenSize = 0;
	double eta = 0.1;	//learning rate

	//constructor
	public NeuralNet(int inputLayerSize, int hiddenLayerSize) {
		initialize(inputLayerSize, hiddenLayerSize);
	}
	//set up the network for the given size of input and hidden layers
	public void initialize(int inputLayerSize, int hiddenLayerSize) {
		inputSize = inputLayerSize;
		hiddenSize = hiddenLayerSize;
		
		hidden = new double[hiddenSize+1];
		setArrayValues(hidden,0);
		hidden[hidden.length-1] = 1;	//bias for output layer
		weights = new double[hiddenSize+1];
		
		setArrayValues(weights,0);
		
		input = new double[inputSize+1];
		
		setArrayValues(input,0);
		
		input[input.length-1] = 1;	//bias for hidden layer
		
		hiddenWeights = new double[inputSize+1][hiddenSize+1];
		for(int i=0;i<inputSize+1;i++) {
			setArrayValues(hiddenWeights[i],0);
		}
	}
	//calculate the output of the given input
	//example must have length inputSize
	
	public double[] convertStateInputToNetworkInput(double[] state, int houseIndex){
		double[] example = new double[inputSize];
		
		for(int j=1;j<inputSize;j++) {
			example[j] = state[j-1];
		}
		example[0] = (double)houseIndex;
		
		return example;
	}
	
	public double calculate(double[] example) {
		
		
		
		
		
		//copy example input
		for(int i=0;i<inputSize;i++) {
			input[i] = example[i];
		}
		//calculate hidden layer
		for(int i=0; i < hiddenSize; i++) {
			hidden[i] = 0;
			for(int j = 0; j < inputSize+1;j++) {
				hidden[i]+= input[j]*hiddenWeights[j][i];
			}
			hidden[i] = sigmoid(hidden[i]);
		}
		//calculate output
		output = 0;
		for(int i=0;i<hiddenSize+1;i++) {
			output += hidden[i]*weights[i];
		}
		return output;
	}
	//update weights to better approximate this example
	public void learnFromExample(double[] example, double desired) {
		double result = calculate(example);
		double err = desired - output;
		//adjust input to hidden weights
		for(int i=0;i<hiddenSize+1;i++) {
			double errI = err*weights[i]*(1-hidden[i])*hidden[i];
			for(int j=0;j<inputSize+1;j++) {
				hiddenWeights[j][i] += eta*errI*input[j];
			}
		}
		//adjust hidden to output weights
		for(int i=0;i<hiddenSize+1;i++) {
			weights[i] += eta*err*hidden[i];
		}
	}
	


	//non-linear function for hidden layer
	private static double sigmoid(double x) {
		//sigmoid function
		return 1/(1+Math.exp(-x));
	}
	//sets all values in array to num
	private void setArrayValues(double[] array, double num) {
		for(int i=0;i<array.length;i++) {
			array[i] = num;
		}
	}
	
}
