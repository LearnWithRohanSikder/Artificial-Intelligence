package ie.atu.sw;

public class CipherRunner {
	public static void main(String[] args) throws Exception {
		var gp = new NGramParser();
		gp.parse("./4grams.txt");
		
		var sa = new SimulatedAnnealing("./cyphertext.txt");
		sa.start();
	}
}