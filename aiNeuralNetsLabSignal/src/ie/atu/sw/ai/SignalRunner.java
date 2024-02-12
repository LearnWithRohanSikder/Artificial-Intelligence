package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class SignalRunner {
	double[][] data = { 
			{ 1, 1, 1, 0 }, { 1, 1, 0, 0 }, { 0, 1, 1, 0 }, { 1, 0, 1, 0 }, { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
			{ 0, 0, 1, 0 }, { 1, 1, 1, 1 }, { 1, 1, 0, 1 }, { 0, 1, 1, 1 }, { 1, 0, 1, 1 }, { 1, 0, 0, 1 },
			{ 0, 1, 0, 1 }, { 0, 0, 1, 1 } 
		};

	double expected[][] = { 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } 
		};
	
	
	public SignalRunner() throws Exception {	
        NeuralNetwork net = trainNetwork();
        testNetwork(net);
    }
	
	 private NeuralNetwork trainNetwork() throws Exception {
	        var builder = NetworkBuilderFactory.getInstance().newNetworkBuilder();
	        NeuralNetwork net = builder
	                .inputLayer("Input", 4)
	                .hiddenLayer("Hidden1", Activation.TANH, 6)
	                .outputLayer("Output", Activation.TANH, 14)
	                .train(data, expected, 0.01, 0.95, 1000000, 0.00001, Loss.SSE)
					.save("./signal.data")
	                .build();
	        return net;
	  }
	 
	 private void testNetwork(NeuralNetwork net) throws Exception {
	        double[] test = {1, 1, 0, 1}; // test signal
	        System.out.println("Testing with original signal:");
	        System.out.println(net.process(test, Output.LABEL_INDEX)); 

	        // noise by flipping one bit
	        if (test[2] == 0) {
	            test[2] = 1;
	        } else {
	            test[2] = 0;
	        }
	        System.out.println("Testing with noisy signal:");
	        System.out.println(net.process(test, Output.LABEL_INDEX)); 
	    }
	    
	    public static void main(String[] args) throws Exception {
	        new SignalRunner();
	    }

}
