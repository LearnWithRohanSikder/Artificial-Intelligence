package ie.atu.sw;

import java.util.concurrent.ThreadLocalRandom;
import static java.lang.System.*;
import static java.lang.Math.*;

public class SimulatedAnnealing {	
	//These two constants control the amount of time SA spends searching
	private static final int TRANSITIONS = 100000;
	private static final int TEMPERATURE = 30;
	
	//Initialise the main components of the programme
	private ThreadLocalRandom rand = ThreadLocalRandom.current();
	private PlayfairCipher cipher = new PlayfairCipher();
	private NGramParser parser = new NGramParser();
	private Key key = new Key(); 	//The initial key to start with
	
	private String fullText;		//The full encrypted text
	private String cipherText;		//The clipped encrypted text (700 chars)
	
	//Initialise the main SA parameters
	private String parent;			
	private String child;

	private String bestDecrypted;
	private String decrypted;
	private String bestKey;
	
	private double parentScore;
	private double bestScore;
	
	private double threshold; //Bail out of the SA search when this threshold is reached.
	
	
	public SimulatedAnnealing(String file) throws Exception {
		//Initialise some variables.
		cipherText = FileIO.read(file);
		fullText = cipherText;
		cipher.createTable(parent); //Create a table with the key
		
		/* 
		 * Unicity Heuristic: 
		 * The unicity distance of a cipher is the length of an ciphertext needed to break the 
		 * cipher. The Unicity distance of Playfair is 27 characters, but we'll use 700 characters 
		 * of the text to help the search find a solution faster. Once a decent key is computed, 
		 * we'll use that to decrypt the fullText.
		 */
		if (cipherText.length() > 700){
			cipherText = cipherText.substring(0, Math.min(cipherText.length(), 700));
		}
	}

	
	public void start() throws Exception{
		//Start by decrypting the cipherText with the generated key
		decrypted = cipher.decode(cipherText);
		bestDecrypted = decrypted;
		
		//Configure the starting state for the parent node
		parent = key.generateKey(); //Generate a random 25 letter key called Parent
		parentScore = parser.getScore(decrypted); //Get heuristic score for Parent 
		
		
		/* 
		 * Threshold - If the current score is 165% better than the starting
		 * score then the we stop and use that key.
		 */
		threshold = parentScore / 1.65;
		bestScore = parentScore;
		bestKey = parent;
		
		
		/*
		 * Start the simulated annealing process
		 */
		anneal(cipherText);
		
		
		//Decode the full text with the generated key
		cipher.createTable(bestKey);
		bestDecrypted = cipher.decode(fullText);
		
		//Output the result to file
		write();
	}
	
	
	private void anneal(String cipherText) {
		out.println("[INFO] Best heuristic score at start: " + bestScore);

		for (int temp = TEMPERATURE; temp > 0; temp--){		
			for (int trans = TRANSITIONS; trans > 0; trans--){
				child = key.shuffleKey(parent); //Make a small change to the key
				cipher.createTable(child);
				decrypted = cipher.decode(cipherText);
				
				double childScore = parser.getScore(decrypted);		
				double delta = childScore - parentScore;
				
				if(delta > 0){ //New key better
					parent = child;
					parentScore = childScore;
				}else{ //New key worse
					if(exp(delta / temp) > rand.nextDouble()){ //Toss a coin...
						parent = child;
						parentScore = childScore;
					}
				}
				
				if(parentScore > bestScore){
					bestScore = parentScore;
					bestDecrypted = decrypted;
					bestKey = parent;
					
					if(bestScore > threshold){
						temp = 0;
						trans = 0;
					}
				}
			}	
			out.println("[INFO] Temp: " + temp + "\tBest Score: " + bestScore 
					  + "\tBest Key: " + bestKey);
		}
		out.println("[INFO] Done! Best Score: " + bestScore + "\nBest Decrypted Text: " 
		             + bestDecrypted + "\nBest Key: " + bestKey);
	}

	
	//Output results
	private void write() throws Exception {
		out.println("[INFO] Writing decrypted text to file.");
		FileIO.write(bestDecrypted, "./out.txt");
	}
}