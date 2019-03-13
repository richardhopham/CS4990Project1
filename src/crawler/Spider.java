package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Spider {
	
	private static final int MAX_PAGES_TO_SEARCH = 1;
	
	private Set<String> pagesVisited = new HashSet<String>();
	
	private Queue<String> pagesToVisit = new LinkedList<String>();
	
	private String getNextURL() {
		String nextURL;
		do {
			nextURL = this.pagesToVisit.poll();
		} while(this.pagesVisited.contains(nextURL));
		this.pagesVisited.add(nextURL);
		return nextURL;
	}
	
	public void search(String url) {
		while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentURL;
			SpiderLeg leg = new SpiderLeg();
			if(this.pagesToVisit.isEmpty()) {
				currentURL = url;
				this.pagesVisited.add(url);
			} else {
				currentURL = this.getNextURL();
			}
			leg.crawl(currentURL);
			System.out.println(String.format("Connected to %s", currentURL));
			this.pagesToVisit.addAll(leg.getLinks());
		}
		System.out.println("\nVisited " + this.pagesVisited.size() + " web page(s)");
	}
	
}
