package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Aicme4jUtils;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class GameRunner {
	
	//Seems to generalize well across a wide range of number of enemies as it produces the same output from 10 to 10,000 enemies. meaning stability is good it also raises the question of whether the network is too generalized and not sensitive enough.
	
	
	
	
	double[][] data = { // Health, Sword, Gun, Enemies
			{ 2, 0, 0, 0 }, { 2, 0, 0, 1 }, { 2, 0, 1, 1 }, { 2, 0, 1, 2 }, { 2, 1, 0, 2 }, { 2, 1, 0, 1 },
			{ 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 1 }, { 1, 0, 1, 2 }, { 1, 1, 0, 2 }, { 1, 1, 0, 1 },
			{ 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 }, { 0, 0, 1, 2 }, { 0, 1, 0, 2 }, { 0, 1, 0, 1 } };

	double[][] expected = { // Panic, Attack, Hide, Run
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };
	
	

	public GameRunner() throws Exception {
		
		 double[][] normalizationRanges = {
		            {-1, 1}, // Standard range
		            {-2, 2}, // More extreme
		            {-5, 5}, // Even more extreme
		            {-10, 10}, // Highly extreme
		        };
		 
		
		NeuralNetwork net = trainNetwork();
	    Aicme4jUtils.normalise(data, -1, 1);
		testNetwork(net);
		testWithIncreasingEnemies(net);

        for (double[] range : normalizationRanges) {
            System.out.println("Testing with normalization range: " + range[0] + " to " + range[1]);
            normalizeAndTrain(range[0], range[1]);
        }
	}

	private NeuralNetwork trainNetwork() throws Exception {
		var builder = NetworkBuilderFactory.getInstance().newNetworkBuilder();
		NeuralNetwork net = builder.inputLayer("Input", 4).hiddenLayer("Hidden1", Activation.TANH, 2)
				.outputLayer("Output", Activation.TANH, 4).train(data, expected, 0.01, 0.95, 100000, 0.00001, Loss.SSE)
				.save("./game.data").build();
		return net;
	}
	
	 private void normalizeAndTrain(double min, double max) throws Exception {
	        // Normalize data
	        Aicme4jUtils.normalise(data, min, max);
	        NeuralNetwork net = trainNetwork();
	        testNetworkMinMax(net, min, max);
	    }

	private void testNetwork(NeuralNetwork net) throws Exception {
		double[] test1 = { 0, 1, 0, 1}; // Panic
		//Aicme4jUtils.normalise(test1, -1, 1);
        Aicme4jUtils.standardise(test1);
        System.out.println("2,0,1,1=>" + net.process(test1, Output.LABEL_INDEX));
	}
	
	private void testWithIncreasingEnemies(NeuralNetwork net) throws Exception {
	    int[] enemies = {1, 10, 100, 1000, 10000};
	    for (int enemy : enemies) {
	        double[] test = {2, 0, 1, enemy}; 
            //Aicme4jUtils.normalise(test, -1, 1);
	        Aicme4jUtils.standardise(test);
	        System.out.println("Testing with enemies: " + enemy + " => " + net.process(test, Output.LABEL_INDEX));
	    }
	}
	
	private void testNetworkMinMax(NeuralNetwork net, double min, double max) throws Exception {
        double[] test = {2, 0, 1, 1};
        Aicme4jUtils.normalise(test, min, max);
        System.out.println("Test result with normalization range " + min + " to " + max + ": " + net.process(test, Output.LABEL_INDEX));
    }


	public static void main(String[] args) throws Exception {
		new GameRunner();
	}

}
