package ie.atu.sw;

import static java.lang.Math.round;
import static java.lang.System.out;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class PatternRunner {
	private double[][] data;
	private double[][] expected;
	
	private PatternRunner() throws Exception{
		init();
		NeuralNetwork net = trainNetwork();
		testNetwork(net);

	}
	
	private double[][] init(){
		data = translate(numerals); 

		//Initialise the expected results
		expected = new double[numerals.length][numerals.length];
		for (int i = 0; i < expected.length; i++){
			expected[i][i] = 1.0d;
		}
		return data;		
	}
	
	private NeuralNetwork trainNetwork() throws Exception {
		var builder = NetworkBuilderFactory.getInstance().newNetworkBuilder();
		NeuralNetwork net = builder
				.inputLayer("Input", 64)
				.hiddenLayer("Hidden", Activation.TANH, 25)
				.outputLayer("Output", Activation.TANSIG, 10)
				.train(data, expected, 0.01, 0.95, 100000, 0.000001, Loss.SSE)
				.save("./MPGRunner.data")
				.build();
		out.println(net);
		return net;
	}
	
	private void testNetwork(NeuralNetwork net) throws Exception {
		var sb = new StringBuffer();
		var test = translate(year);
		for (int i = 0; i < test.length; i++){
		sb.append((int) net.process(test[i], Output.LABEL_INDEX)); }
		out.println("Result: " + sb.toString());
	}
	
	private double[][] translate(String[][] s){
		var temp = new double[s.length][];
		for (int character = 0; character <  s.length; character++){
			var next = new double[s[character].length * s[character].length];
			temp[character] = next;
			
			var index = 0;
			for (int row = 0; row < s[character].length; row++){
				for (int i = 0; i < s[character][row].length(); i++){
					next[index] = s[character][row].charAt(i) == '@' ? 1.0d : 0.0d;
					index++;
				}
			}
		}
		return temp;
	}
	
	private String[][] numerals = {
			{
			" @@@@@  ",
			"@@   @@ ",
			"@@  @@@ ",
			"@@ @@@@ ",
			"@@@@ @@ ",
			"@@@  @@ ",
			" @@@@@  ",
			"        "
			},
			{
			"  @@    ",
			" @@@    ",
			"  @@    ",
			"  @@    ",
			"  @@    ",
			"  @@    ",
			"@@@@@@  ",
			"        "
			},
			{
			" @@@@   ",
			"@@  @@  ",
			"    @@  ",
			"  @@@   ",
			" @@     ",
			"@@  @@  ",
			"@@@@@@  ",
			"        "
			},
			{
			" @@@@   ",
			"@@  @@  ",
			"    @@  ",
			"  @@@   ",
			"    @@  ",
			"@@  @@  ",
			" @@@@   ",
			"        "
			},
			{
			"   @@@  ",
			"  @@@@  ",
			" @@ @@  ",
			"@@  @@  ",
			"@@@@@@@ ",
			"    @@  ",
			"   @@@@ ",
			"        "
			},
			{
			"@@@@@@  ",
			"@@      ",
			"@@@@@   ",
			"    @@  ",
			"    @@  ",
			"@@  @@  ",
			" @@@@   ",
			"        "
			},
			{
			"  @@@   ",
			" @@     ",
			"@@      ",
			"@@@@@   ",
			"@@  @@  ",
			"@@  @@  ",
			" @@@@   ",
			"        "
			},
			{
			"@@@@@@  ",
			"@@  @@  ",
			"    @@  ",
			"   @@   ",
			"  @@    ",
			"  @@    ",
			"  @@    ",
			"        "
			},
			{
			" @@@@   ",
			"@@  @@  ",
			"@@  @@  ",
			" @@@@   ",
			"@@  @@  ",
			"@@  @@  ",
			" @@@@   ",
			"        "
			},
			{
			" @@@@   ",
			"@@  @@  ",
			"@@  @@  ",
			" @@@@@  ",
			"    @@  ",
			"   @@   ",
			" @@@    ",
			"        "
			}
		};
		
		private String[][] year = { //1789... Note that the numerals have errors
				{
				"  @@    ",
				" @@     ",
				"  @@    ",
				"   @    ",
				"  @@    ",
				"  @@    ",
				"@ @@ @  ",
				"        "
				},
				{
				"@@  @@  ",
				"    @@  ",
				"    @@  ",
				"    @   ",
				"  @@@   ",
				"   @    ",
				"  @     ",
				"        "
				},
				{
				" @ @@   ",
				"@    @  ",
				"@   @@  ",
				"   @@   ",
				"@@      ",
				"@@  @@  ",
				" @ @@   ",
				"        "
				},
				{
				" @@@@   ",
				"@   @   ",
				"@@   @  ",
				" @   @  ",
				"     @  ",
				"   @@   ",
				" @@@    ",
				"        "
				}
			};
		
	public static void main(String[] args) throws Exception {
		new PatternRunner();
	}
}