package ie.atu.sw;

import java.util.concurrent.ThreadLocalRandom;

public class Key {
	private String keyToShuffle = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
	private ThreadLocalRandom rand = ThreadLocalRandom.current();

	public String generateKey(){ //Fisher-Yates Shuffle		
		var key = keyToShuffle.toCharArray();
		var index = 0;
		for (int i = key.length - 1; i > 0; i--) {			
			index = rand.nextInt(i + 1);
			if (index != i) {
				key[index] ^= key[i];
				key[i] ^= key[index];
				key[index] ^= key[i];
			}
		}
		return new String(key);
	}
	
	/*
	 * Randomly Makes Changes to The Key Provided
	 * 
	 * 		Swap single letters (90%)
	 * 		Swap random rows (2%)
	 * 		Swap columns (2%)
	 * 		Flip all rows (2%)
	 * 		Flip all columns (2%) 
	 * 		Reverse the whole key (2%)
	 */
	public String shuffleKey(String key){
		var number = rand.nextInt(100) + 1;
		
		return switch (number) {
			case 1, 2 -> swapCols(key, rand.nextInt(4), rand.nextInt(4));
			case 3, 4 -> swapRows(key, rand.nextInt(4), rand.nextInt(4));
			case 5, 6 -> flipCols(key);
			case 7, 8 -> flipRows(key);
			case 9, 10 -> reverseKey(key);
			default -> swapLetters(key, rand.nextInt(24), rand.nextInt(24));
		};
	}

	public String swapLetters(String s, int one, int two) { //Randomly picks two characters and swaps them		
		var chars = s.toCharArray();
        var temp = chars[one];
        chars[one] = chars[two];
        chars[two] = temp;
        return new String(chars);	
	}

	public String reverseKey(String s) { //Reverses the key
		return new StringBuilder(s).reverse().toString();
	}
	
	public String flipRows(String s) { //Flips the rows in the key
		var chars = s.toCharArray();
		for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                chars[i * 5 + j] = s.toCharArray()[(4 - i) * 5 + j];
            }
        }		
		return new String(chars);	
	}
	
	public String flipCols(String s) { //Flips the columns in the key
		var chars = s.toCharArray();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				chars[i * 5 + j] = s.toCharArray()[i * 5 + 4 - j];
			}
		}
		return new String(chars);
	}
	
	public String swapRows(String s, int row1, int row2) { //Randomly swaps two rows in the key
		var chars = s.toCharArray();
		for(int i = 0; i < 5; i++) {
			char temp = chars[row1 * 5 + i];
			chars[row1 * 5 + i] = chars[row2 * 5 + i];
			chars[row2 * 5 + i] = temp;
		}
		
		return new String(chars);
	}

	public String swapCols(String s, int col1, int col2) { //Randomly swaps two columns in the key
		var chars = s.toCharArray();
		for(int i = 0; i < 5; i++){
			char temp = chars[i * 5 + col1];
			chars[i * 5 + col1] = chars[i * 5 + col2];
			chars[i * 5 + col2] = temp;
		}
		return new String(chars);
	}	
}