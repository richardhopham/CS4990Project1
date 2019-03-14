package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A class SpiderLeg to perform the web crawling and writes the results of the links to a .csv file
 * Also calls the Inverted class to perform term frequency operations and store each term in a inverted index HashMap
 * @author Richard Pham
 *
 */
public class SpiderLeg {

	//Used to make sure the version of the page is up-to-date with each browser
	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
	
	//Keeps track of what document it is on
	private static int documentNumber = 1;
	
	//Linked List to hold all links
	private List<String> links = new LinkedList<String>();
	
	//HashMap to hold each specific link and the number of out-links on the page
	private static HashMap<String, Integer> outlinks = new HashMap<String, Integer>();
	
	//File to store each specific link and the number of out-links
	private static File urlFile = new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/report.csv");
	
	//BufferedWriter to write to a file
	private BufferedWriter writer = null;
	
	//Inverted index object
	private Inverted index = new Inverted();
	
	//Main operation of the entire program
	//Connects to the web, performs a GET HTML request, stores the HTML file, parses it, stores the number of outlinks, and calls indexfile to index each term and their frequency
	public boolean crawl(String nextURL) {
		try {
			Connection conn = Jsoup.connect(nextURL).userAgent(USER_AGENT);		//attempt to connect to the URL
			Document htmlDoc = conn.get();										//performs GET request
			if(conn.response().statusCode() == 200) {							//check if successful connection
				System.out.println("\nCurrently @ " + nextURL);
			}
			if(!conn.response().contentType().contains("text/html")) {			//if not, exit
				System.out.println("Did not retrieve HTML!");
				return false;
			}
			Elements linksOnPage = htmlDoc.select("a[href]");					//gets all elements that are in the form "<a href ...", since they hold the out-links
			outlinks.put(nextURL, linksOnPage.size());							//URL is the key, number of outlinks is the value in the HashMap
			System.out.println("Found (" + linksOnPage.size() + ") links");
			String fileName = "document"+documentNumber+".txt";					//file name of the document
			try {
				//Write raw html file to the repository
				writer = new BufferedWriter(new FileWriter ( new File ("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", fileName)));
				writer.write(htmlDoc.html());

			} catch (Exception e) {
				System.out.println(e);
			}
			writer.flush();
			writer.close();
			
			//file name of parsed document
			String parsedFileName = "parsedDocument"+documentNumber+".txt";
			
			//Create access to the file
			File file = new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository/"+fileName);
			
			//Allow write to parsed html file
			writer = new BufferedWriter(new FileWriter( new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", parsedFileName)));
			
			//Scanner object to read the raw html file
			Scanner readFile = new Scanner(file);
			
			//While loop to parse through html file and remove html elements
			while(readFile.hasNext()) {
				writer.write(Jsoup.parse(readFile.nextLine()).wholeText());
				
			}
			
			//Flush and close
			writer.flush();
			writer.close();
			
			//Close Scanner from reading from file
			readFile.close();
			
			//For loop to add links
			for(Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			
			//index each term in the parsed file
			index.indexFile(new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", parsedFileName));
			
			//write the url and outlinks to the .csv file
			writeURLCountFile();
			
			//Print each term and their frequency so far
			index.printHashMap();
			documentNumber++;		//increment doc number to prep for next doc/link
			return true;
		} catch (IOException ioe) {
			return false;
		}

	}
	
	//Method to write the number of outlinks and URL to a file
	public void writeURLCountFile() throws IOException {
		StringBuilder build = null;
		Set<String> keys = outlinks.keySet();
		FileWriter writer = new FileWriter(SpiderLeg.urlFile);
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			build = new StringBuilder();
			build.append(arrayOfKeys[i]+","+outlinks.get(arrayOfKeys[i]));
			build.append("\n");
			writer.write(build.toString());
		}
		writer.flush();
		writer.close();
	}
	
	//Method to check and print each URL and the number of outlinks
	public void printURLAndLinks() {
		Set<String> keys = outlinks.keySet();
		String[] arrayOfKeys = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < arrayOfKeys.length; i++) {
			System.out.println(arrayOfKeys[i]+" : "+outlinks.get(arrayOfKeys[i]));
		}
	}
	
	//Returns the list of links
	public List<String> getLinks() {
		return this.links;
	}
}
