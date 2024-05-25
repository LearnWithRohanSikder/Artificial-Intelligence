package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIO {
	public static String read(String name) throws Exception{
		var builder = new StringBuilder();
		try (var stream = Files.lines(Paths.get(name), StandardCharsets.UTF_8)){
			stream
			.map(String::toUpperCase)
			.map(text -> text.replaceAll("[^A-Za-z0-9 ]", ""))
			.forEach(s -> builder.append(s));
		}
		return builder.toString();
	}
	
	public static void write(String text, String path) throws Exception{
		try (var out = new BufferedWriter(new FileWriter(path))){
			out.write(text);  
		}
	}
}