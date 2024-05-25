package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class NGramParser {	
	private static Map<String, Double> map = new HashMap<>();
	private static double count = 0; 
	
	public void parse(String file) throws Exception{
		Files.lines(Paths.get(file)).forEach(text -> {
			map.put(text.split(" ")[0], Double.parseDouble(text.split(" ")[1]));
		});
		count = map.values().stream().mapToDouble(n -> n.doubleValue()).sum(); //Sum n-gram values
	}  
	
	public double getScore(String text){ //Scores maximum 696 characters from the cipher text against 4grams
		var score = 0;
		var n = text.length() < 700 ? text.length() - 4 : 700 - 4;
		for (int i = 0; i < n; i++) {
			if (map.get(text.substring(i, i + 4)) != null){
				score += Math.log10((map.get(text.substring(i, i + 4))) / count);
			}else{
				score += Math.log10(1.0d / count);
			}
		}
		return score;
	}
}