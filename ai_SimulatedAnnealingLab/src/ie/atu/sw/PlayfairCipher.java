package ie.atu.sw;

//import java.awt.Point;

public class PlayfairCipher {//Implements the Playfair cipher
    private char[][] charTable;
    private Point[] positions;
    
    private static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    //Prepares Text For Decryption converting to Caps and replacing J's with I's
    public String primeText(String s, boolean changeJtoI) { 
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? s.replace("J", "I") : s.replace("Q", "");
    }
 
    //Create a table with a Key
    public void createTable(String key) {
        charTable = new char[5][5];
        positions = new Point[26];
 
        var s = primeText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", true);
        var len = s.length();
        for (int i = 0, k = 0; i < len; i++) {
            char c = s.charAt(i);
            if (positions[c - 'A'] == null) {
                charTable[k / 5][k % 5] = c;
                positions[c - 'A'] = new Point(k % 5, k / 5);
                k++;
            }
        }
    }
    
    public String encode(String s) {
        var builder = new StringBuilder(s);
        for (int i = 0; i < builder.length(); i += 2) {
            if (i == builder.length() - 1) {
                builder.append(builder.length() % 2 == 1 ? 'X' : "");
            }else  if (builder.charAt(i) == builder.charAt(i + 1)) {
                builder.insert(i + 1, 'X');
            }
        }
        return codec(builder, 1);
    }
      
    public String decode(String s) { 	
        return codec(new StringBuilder(s), 4);
    }
    
    public String codec(StringBuilder text, int direction) {
        var len = text.length();
        for (int i = 0; i < len; i += 2) {
        	var a = text.charAt(i);
        	var b = text.charAt(i + 1);
        	
        	//Get indices
        	var row1 = positions[a - 'A'].y;
        	var row2 = positions[b - 'A'].y;
        	var col1 = positions[a - 'A'].x;
        	var col2 = positions[b - 'A'].x;
 
            if (row1 == row2) {
                col1 = (col1 + direction) % 5;
                col2 = (col2 + direction) % 5;
            } else if (col1 == col2) {
                row1 = (row1 + direction) % 5;
                row2 = (row2 + direction) % 5;
            } else {
                int tmp = col1;
                col1 = col2;
                col2 = tmp;
            }
 
            text.setCharAt(i, charTable[row1][col1]);
            text.setCharAt(i + 1, charTable[row2][col2]);
        }
        return text.toString();
    }
}