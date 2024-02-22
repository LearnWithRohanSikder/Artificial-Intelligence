package ie.atu.sw;

import static java.lang.System.out;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.LayerSize;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class ImageClassifier {
	private String[] names = { "Demon", "Egg", "Flying Pig", "Hawk", "Ice Cream", "Man with a Hat", "Moon", "Pigeon", "Rabbit", "Radar", "Razor", "Robot", "Saw", "Scary Skull", "Spiral", "Swiss Roll", "Tree", "Woman" };
	private double[][] data;
	private double[][] expected;
	private ThreadLocalRandom rand = ThreadLocalRandom.current();

	public void go(String directory) throws Exception {
		parse(directory);
		
		//Create the neural network here...
		NeuralNetwork net = trainNetwork();
		test(net);
	}
	

	private NeuralNetwork trainNetwork() throws Exception {
		var builder = NetworkBuilderFactory.getInstance().newNetworkBuilder();
		NeuralNetwork net = builder
				.inputLayer("Input", 256)
				.hiddenLayer("Hidden", Activation.SIGMOID, LayerSize.GEOMETRIC_PYRAMID)
				.outputLayer("Output", Activation.SIGMOID, names.length)
				.train(data, expected, 0.01, 0.95, 10000,  0.00001, Loss.SSE)
				.save("./MPGRunner.data")
				.build();
		out.println(net);
		return net;
	}

	/*
	 * It's important to enasure that the test data has not been seen by
	 * the model before. Otherwise we can end up with a model that is highly
	 * sensitive and accurate to its training data only and will not work
	 * as an accurate predictor.
	 */
	private void test(NeuralNetwork net) throws Exception {
		var percentage = 5; //Progressively increase this value to see how good the model is  
		for (int i = 0; i < 100; i++) { 
			out.print("Test " + i + ": ");

			var index = rand.nextInt(0, data.length);
			var vector = noiseify(data[index], percentage, -2, 2);
			var actual = names[(int) net.process(vector, Output.LABEL_INDEX)];
			var desired = names[index / 4];

			out.println("\t" + (desired.equals(actual) ? "[CORRECT]" : "[ERROR..]") + " " + desired + " => " + actual);
		}
	}

	//Use a messed up version of the training data to test the model
	private double[] noiseify(double[] vector, double percentage, double min, double max) {
		var tmp = new double[vector.length];
		System.arraycopy(vector, 0, tmp, 0, vector.length); //Copy the pixels

		var counter = (int) ((vector.length / 100.00d) * percentage);
		out.print("Making " + counter + " random pixel changes. ");
		while (counter > 0) {
			tmp[rand.nextInt(0, tmp.length)] = rand.nextDouble(min, max);
			counter--;
		}
		return tmp;
	}

	//Process each PNG file in the sprites directory
	private void parse(String directory) throws Exception {
		var images = new File(directory).listFiles(n -> n.getName().endsWith("png"));
		data = new double[images.length][];
		expected = new double[images.length][];

		out.println("Processing " + images.length + " images.");
		for (int i = 0; i < images.length; i++) {
			var expectVector = new double[names.length];
			expected[i] = expectVector;
			expectVector[i / 4] = 1; //Each sprite consists of 4 images 
			data[i] = getPixels(ImageIO.read(images[i]));
		}
	}
	
	//Convert each image into a flat map of pixels
	public double[] getPixels(BufferedImage image) {
		var pixels = new double[image.getWidth() * image.getHeight()];
		var index = 0;
		for (int x = 0; x < image.getWidth(); x++) { //Loop over the image pixels
			for (int y = 0; y < image.getHeight(); y++) {
				var rgb = image.getRGB(y, x); //The the pixel at the x/y coord
				var r = (rgb >> 16) & 0xFF; //Get the red channel
				var g = (rgb >> 8) & 0xFF;  //Get the green channel
				var b = (rgb & 0xFF);		//Get the blue channel
				var colour = (r + g + b) / 3; //Computes to 255 for white or 0 for black.
				pixels[index] = colour > 0 ? 0 : 1; //Binary encode with 0 or white and 1 for black
				index++;
			}
		}
		return pixels;
	}

	public static void main(String[] args) throws Exception {
		new ImageClassifier().go("./sprites");
	}
}