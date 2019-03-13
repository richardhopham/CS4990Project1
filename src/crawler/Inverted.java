package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Inverted {

	private static HashMap<String, Integer> freq = new HashMap<String, Integer>();
	
	public void indexFile(File fileToIndex) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileToIndex));
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
			for(String chunk: line.split("\\W+")) {
				String word = chunk.replaceAll("[^a-zA-Z0-9]", " ");
				boolean inside = freq.containsKey(word);
				if(!inside) {
					freq.put(word, 1);
				} else {
					freq.put(word, freq.get(word) + 1);
				}
			}
		}
		reader.close();
	}
	
	public void printHashMap() {
		Set<String> keys = freq.keySet();
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			System.out.println(arrayOfKeys[i]+": "+freq.get(arrayOfKeys[i]));
		}
	}
}
