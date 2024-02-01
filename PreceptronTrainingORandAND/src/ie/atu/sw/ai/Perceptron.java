package ie.atu.sw.ai;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
public class Perceptron {
	private float[] weights;
	private float theta = 0.2f;
	
	public Perceptron(int connection) {
		weights = new float[connection];
		
		//Initialise weights
		var rand = ThreadLocalRandom.current();
		for (int i = 0; i < weights.length; i++) {
	      weights[i] = rand.nextFloat(-0.5f, 0.5f);
		}

		System.out.println(this);
	}
	
	public float[] getWeights() {
		return weights; 
	}
	
	public int activate(float[] inputs) {
	    float sum = 0;
	    for (int i = 0; i < weights.length; i++) {
	      sum += inputs[i] * weights[i];
	    }
	    
	    return sum - theta >= 0 ? 1 : 0;
	}
	
	public String toString() {
		return "Perceptron [weights=" + Arrays.toString(weights) + "]";
	}

	
	//Trainer...
	private float alpha = 0.1f; //Alpha learning rate
	public void train(float[][] inputs, float[] expected, int max_epochs) {
		boolean hasError = true;
		float error = 1000;
		int epoch = 0;
		
		while (epoch < max_epochs && hasError){
			hasError = false;
			
			for (int row = 0; row < inputs.length; row++){
				int actual = activate(inputs[row]);
				error = expected[row] - actual;
				if (error != 0) hasError = true;

				//Adjust weights based on weightChange * input
				for (int i = 0; i < weights.length; i++) {
					weights[i] += alpha * error * inputs[row][i];
				}
			    System.out.println(Arrays.toString(weights));
			}
			epoch++;
		}
		
		System.out.println("Training complete in " + epoch + " epochs.");
		System.out.println(this);
	}
}