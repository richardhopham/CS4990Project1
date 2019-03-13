package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {

	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
	
	private Document htmlDoc;
	private List<String> links = new LinkedList<String>();
	
	
	public boolean crawl(String nextURL) {
		try {
			Connection conn = Jsoup.connect(nextURL).userAgent(USER_AGENT);
			Document htmlDoc = conn.get();
			this.htmlDoc = htmlDoc;
			if(conn.response().statusCode() == 200) {
				System.out.println("\n**Visting** Received web page at " + nextURL);
			}
			if(!conn.response().contentType().contains("text/html")) {
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}
			Elements linksOnPage = htmlDoc.select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for(Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}
	
	public List<String> getLinks() {
		return this.links;
	}
}
