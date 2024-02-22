package ie.atu.sw.ai;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Aicme4jUtils;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class BiFunctionRunner {
	private BiFunction<Double, Double, Double> func = (m, n) -> Math.pow(m, 2.0) + (2 * n) + 7;
	private double[][] data = new double[100][2];
	private double[][] expected = new double[100][1];
	private double min = -4;
	private double max = 4;

	
	public BiFunctionRunner() throws Exception {
		NeuralNetwork net = trainNetwork();
		Aicme4jUtils.standardise(data);
		testNetwork(net);
	}
	
	public void init(BiFunction<Double, Double, Double> func) {
		var rand = ThreadLocalRandom.current();
		IntStream.range(0, data.length).forEach(i -> {
			data[i][0] = rand.nextDouble(min, max);
			data[i][1] = rand.nextDouble(min, max);
			expected[i][0] = func.apply(data[i][0], data[i][1]);
		});
	}
	
	private NeuralNetwork trainNetwork() throws Exception {
		var builder = NetworkBuilderFactory.getInstance().newNetworkBuilder();
		NeuralNetwork net = builder
				.inputLayer("Input", 2)
				.hiddenLayer("Hidden1", Activation.SIGMOID, 8)
				.outputLayer("Output", Activation.LINEAR, 1)
				.train(data, expected, 0.001, 0.95, 10000, 0.0000001, Loss.SSE)
				.save("./BiFunction.data")
				.build();
		return net;
	}
	
	
	private void testNetwork(NeuralNetwork net) throws Exception {
		for (int i = 0; i < data.length; i++) {
			 var predicted = net.process(data[i], Output.NUMERIC);
			 var actual = expected[i][0];
			 System.out.println(data[i][0] + "," + predicted + "," + actual + "\t");
			}
	}
	
	public static void main(String[] args) throws Exception {
		new BiFunctionRunner();

	}

}
