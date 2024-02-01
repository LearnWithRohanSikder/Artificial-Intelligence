package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.LayerSize;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.Output;

public class XORRunner {
	
	public void go() throws Exception{
		
		double[][] data = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
		double[][] expected = {{0}, {1}, {1}, {0}};
		
		var net = NetworkBuilderFactory.getInstance().newNetworkBuilder()
				.inputLayer("Input", 2)
				.hiddenLayer("Hidden1", Activation.TANH, LayerSize.SUM)
				.outputLayer("Output", Activation.TANH, 1)
				.train(data, expected, 0.01, 0.95, 100000, 0.00001, Loss.SSE)
				.save("./xor.data")
				.build();
		
		
		System.out.println(net);
		
		double[] test1 = {0.0, 0.0};
		double[] test2 = {1.0, 0.0};
		double[] test3 = {0.0, 1.0};
		double[] test4 = {1.0, 1.0};
		
		System.out.println("00=>" + net.process(test1, Output.NUMERIC_ROUNDED));
		System.out.println("10=>" + net.process(test2, Output.NUMERIC_ROUNDED));
		System.out.println("01=>" + net.process(test3, Output.NUMERIC_ROUNDED));
		System.out.println("11=>" + net.process(test4, Output.NUMERIC_ROUNDED));

	}
	
	public static void main(String[] args) {
		XORRunner runner = new XORRunner();
        try {
			runner.go();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
