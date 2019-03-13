package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {

	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
	
	private static int documentNumber = 1;
	
	private List<String> links = new LinkedList<String>();
	
	private static HashMap<String, Integer> outlinks = new HashMap<String, Integer>();
	
	private BufferedWriter writer = null;
	
	private Inverted index = new Inverted();
	
	
	public boolean crawl(String nextURL) {
		try {
			Connection conn = Jsoup.connect(nextURL).userAgent(USER_AGENT);
			Document htmlDoc = conn.get();
			if(conn.response().statusCode() == 200) {
				System.out.println("\nCurrently @ " + nextURL);
			}
			if(!conn.response().contentType().contains("text/html")) {
				System.out.println("Did not retrieve HTML!");
				return false;
			}
			Elements linksOnPage = htmlDoc.select("a[href]");
			outlinks.put(nextURL, linksOnPage.size());
			System.out.println("Found (" + linksOnPage.size() + ") links");
			String fileName = "document"+documentNumber+".txt";
			try {

				writer = new BufferedWriter(new FileWriter ( new File ("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", fileName)));
				writer.write(htmlDoc.html());

			} catch (Exception e) {
				System.out.println(e);
			}
			writer.flush();
			writer.close();
			
			String parsedFileName = "parsedDocument"+documentNumber+".txt";
			File file = new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository/"+fileName);
			writer = new BufferedWriter(new FileWriter( new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", parsedFileName)));
			Scanner readFile = new Scanner(file);
			while(readFile.hasNext()) {
				writer.write(Jsoup.parse(readFile.nextLine()).wholeText());
				
			}
			writer.flush();
			writer.close();
			readFile.close();
			
			for(Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			index.indexFile(new File("/Users/Richard Pham/workspace/CS4990Project1/src/crawler/repository", parsedFileName));
			index.printHashMap();
			documentNumber++;
			return true;
		} catch (IOException ioe) {
			return false;
		}

	}
	
	public List<String> getLinks() {
		return this.links;
	}
}
