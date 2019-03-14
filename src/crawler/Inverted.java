package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * A class to store each term and their frequencies into an inverted index and write them to a .csv file
 * @author Richard Pham
 *
 */
public class Inverted {

	//HashMap used to store term and term frequency of words parsed from document
	private static HashMap<String, Integer> freq = new HashMap<String, Integer>();
	
	//Stores the termFile as a .csv file
	private static File termFile = new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/termFreq.csv");
	
	//indexFile used to parse the the document
	public void indexFile(File fileToIndex) throws IOException {
		
		//BufferedReader and FileReader used to parse the argument (File fileToIndex)
		BufferedReader reader = new BufferedReader(new FileReader(fileToIndex));
		
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {		//For loop that reads line until there's no more lines
			for(String chunk: line.split("\\W+")) {											// For each loop that splits the line into chunks based on whitespace
				
				//Parse the string chunk and remove punctuations, numbers, and anything that does not follow a-z A-z regex
				String word = chunk.replaceAll("[^a-zA-Z]", "");
				word = word.replaceAll("\\p{Punct}]", "");
				
				//check to see if the hashmap contains the word
				boolean inside = Inverted.freq.containsKey(word);
				
				//If not, put the word into the hashmap
				if(!inside) {
					Inverted.freq.put(word, 1);		//put the word into the inverted index and set value to 1
				} else {							//Else, update the term frequency
					Inverted.freq.put(word, Inverted.freq.get(word) + 1);	//increment value of word by 1
				}
			}
		}
		//Remove "" from the hashmap. We did this because the parsedFile keeps showing "'"
		Inverted.freq.remove("");	
		
		reader.close();			//Close the reader to save memory
		writeTermFreqFile();
	}
	
	//Method to write the term frequencies to a file
	public void writeTermFreqFile() throws IOException {
		//Create a StringBuilder that is used to append Strings to the term file
		StringBuilder build = null;
		
		//Move everything to set that stores the keys from the freq hashmap
		Set<String> keys = Inverted.freq.keySet();
		
		//Create a FileWriter that writes to the termFile described in the private static variable
		FileWriter writer = new FileWriter(Inverted.termFile);
		
		//Create an array of Keys based on the size of the keys Set
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		
		//Append the term and the term frequency to the termFile on each line
		for(int i = 0; i < arrayOfKeys.length; i++) {
			build = new StringBuilder();
			build.append(arrayOfKeys[i]+","+Inverted.freq.get(arrayOfKeys[i]));
			build.append("\n");
			writer.write(build.toString());
		}
		//Flush and close writer
		writer.flush();
		writer.close();
	}
	

	//Method to test and print all entries in the hash map
	public void printHashMap() {
		Set<String> keys = Inverted.freq.keySet();
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			System.out.println(arrayOfKeys[i]+": "+ Inverted.freq.get(arrayOfKeys[i]));
		}
	}
}
