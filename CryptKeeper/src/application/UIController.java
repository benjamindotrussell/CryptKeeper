/*/
 * Ben Russell
 * 09/07/2018 
 */
package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;


public class UIController {

	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&?";
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private TextField cryptKey;
    
    @FXML 
    private Label keyLbl;
    
    @FXML
    private TextField myFile;
    
    File file;
    
    @FXML
    void Initilaize() {
    }
    /**
     * Launch a file browser. Set the file field to the selected file.
     * @param event
     */
    @FXML
    void browseClick(MouseEvent event) {
    	FileChooser fc = new FileChooser();
    	file = fc.showOpenDialog(null);
    	myFile.setText(file.toString());
    	cryptKey.setVisible(true);
    	keyLbl.setVisible(true);
    }

    /**
     * When decrypt 
     * @param event
     */
    @FXML
    void decryptClick(MouseEvent event) {
    	if (file != null) decrypt(file);
    }
    
    @FXML
    void encryptClick(MouseEvent event) {
    	if (file != null) encrypt(file);
    }

    private void decrypt(File file2) {
    	if (cryptKey.getText().length() != 0) {
	    	String cypherKey = uniquify(cryptKey.getText().toLowerCase());
	    	String cryptabet = createAlphabet(cypherKey);
	    	decryptFile(file2, cryptabet);
    	}
    	
	}

    private void decryptFile(File file2, String cryptabet) {
    	try {
			FileReader fr = new FileReader(file2);
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				for (Character c : line.toCharArray()) {
					if (cryptabet.contains(c.toString())) {
						int a = cryptabet.indexOf(c);
						sb.append(ALPHABET.charAt(a));
					} else {
						sb.append(c);
					}
				}
				sb.append("\n");
			}
			
			String[] s1 = sb.toString().split("\n");
			System.out.print(s1.toString());
			fr.close();
			br.close();
			FileWriter fw = new FileWriter(file2);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < s1.length; i++) {
				bw.write(s1[i]);
				bw.newLine();
				
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
     * Encrypt the selected file
     * @param file2 file to encrypt
     */
    private void encrypt(File file2) {
    	if (cryptKey.getText().length() != 0) {
	    	String cypherKey = uniquify(cryptKey.getText().toLowerCase());
	    	String cryptabet = createAlphabet(cypherKey);
	    	encryptFile(file2, cryptabet);
    	}    	
	}

    /**
     * encrypt the specified file.
     * @param file2 file to encrypt
     * @param cryptabet the alphabet to use to encrypt the file.
     */
    private void encryptFile(File file2, String cryptabet) {
		try {
			FileReader fr = new FileReader(file2);
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				for (Character c : line.toCharArray()) {
					if (ALPHABET.contains(c.toString())) {
						int a = ALPHABET.indexOf(c);
						sb.append(cryptabet.charAt(a));
					} else {
						sb.append(c);
					}
				}
				sb.append("\n");
			}
			
			String[] s1 = sb.toString().split("\n");
			System.out.print(s1.toString());
			fr.close();
			br.close();
			FileWriter fw = new FileWriter(file2);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < s1.length; i++) {
				bw.write(s1[i]);
				bw.newLine();
				
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
     * Create an cryptic alphabet that can be used to encode the file.
     * @param cypherKey
     * @return a cryptic alphabet.
     */
    private String createAlphabet(String cypherKey) {
    	StringBuilder cryptabet = new StringBuilder();
    	cryptabet.append(cypherKey);
    	for (Character c : ALPHABET.toCharArray()) {
    		if (!cryptabet.toString().contains(c.toString())) cryptabet.append(c.toString());
    	}
    	int cryptArrayHeight = cryptabet.length()/cypherKey.length();
    	String[] cryptArray = new String[cryptArrayHeight + 1];
    	cryptArray = createCryptArray(cryptArray, cryptabet.toString(), cypherKey.length());
    	cryptabet = arrayToString(cryptArray);
    	
		return cryptabet.toString();
	}

    /**
     * Convert the 2DArray to a string.
     * @param cryptArray 2DArray
     * @return cryptabet a cryptic alphabet
     */
    private StringBuilder arrayToString(String[] cryptArray) {
		StringBuilder cryptabet = new StringBuilder();
		HashMap<Character, Integer> m = new HashMap<Character, Integer>();
		int i = 0;
		for (Character c : cryptArray[0].toCharArray()) {
			m.put(c, i);			
			i++;		
		}
		Stream<Map.Entry<Character, Integer>> sorted =
			    m.entrySet().stream()
			       .sorted(Map.Entry.comparingByKey());
		Iterator<Entry<Character, Integer>> it = sorted.iterator();
		while (it.hasNext()) {
			Entry<Character, Integer> a = it.next();
			int rowNumber = a.getValue();
			for (int i2 = 0; i2 < cryptArray.length; i2++) {				
				try {
					cryptabet.append(cryptArray[i2].charAt(rowNumber));
				} catch (Exception e) {					
				}
			}			
		}
		
		return cryptabet;
	}
    
	/**
     * create an array of strings that represent rows and columns.
     * @param cryptArray an empty array to be filled with strings
     * @param cryptabet the alphabet to be used to fill the array
     * @param rows number of rows
     * @return an array of strings generated from the cryptabet.
     */
	private String[] createCryptArray(String[] cryptArray, String cryptabet, int rows) {
		
		int i = 0;
		int j = 0;
		while (i < cryptArray.length - 1) {
			cryptArray[i] = cryptabet.substring(j, j + rows);
			j += rows;
			i++;
		}

		cryptArray[i] = cryptabet.substring(j);
		for (int i1 = 0; i1 < cryptArray.length; i1++) {
			System.out.println(cryptArray[i1] + " ");

		}
		return cryptArray;
	}
	
	/**
	 * remove any duplicate characters. 
	 * @param cypherKey
	 * @return a string with unique characters
	 */
	private String uniquify(String cypherKey) {
    	StringBuilder unique = new StringBuilder();
    	for (Character c : cypherKey.toCharArray()) {
    		if (!unique.toString().contains(c.toString())) unique.append(c.toString());
    	}
    	return unique.toString();
    }
	
	
}


