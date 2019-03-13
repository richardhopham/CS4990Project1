package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Inverted {

	private static HashMap<String, Integer> freq = new HashMap<String, Integer>();
	
	private static File termFile = new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/termFreq.csv");
	
	public void indexFile(File fileToIndex) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileToIndex));
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
			for(String chunk: line.split("\\W+")) {
				String word = chunk.replaceAll("[^a-zA-Z]", "");
				word = word.replaceAll("\\p{Punct}]", "");
				boolean inside = Inverted.freq.containsKey(word);
				if(!inside) {
					Inverted.freq.put(word, 1);
				} else {
					Inverted.freq.put(word, Inverted.freq.get(word) + 1);
				}
			}
		}
		Inverted.freq.remove("");
		reader.close();
		writeTermFreqFile();
	}
	
	public void writeTermFreqFile() throws IOException {
		StringBuilder build = null;
		Set<String> keys = Inverted.freq.keySet();
		FileWriter writer = new FileWriter(Inverted.termFile);
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			build = new StringBuilder();
			build.append(arrayOfKeys[i]+","+Inverted.freq.get(arrayOfKeys[i]));
			build.append("\n");
			writer.write(build.toString());
		}
		writer.flush();
		writer.close();
	}
	
	public void printHashMap() {
		Set<String> keys = Inverted.freq.keySet();
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			System.out.println(arrayOfKeys[i]+": "+ Inverted.freq.get(arrayOfKeys[i]));
		}
	}
}
