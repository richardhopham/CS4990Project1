package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * A class Spider to initialize and call crawl method inside SpiderLeg
 * Also checks if pages have been visited and manages the frontier (queue)
 * @author Richard Pham
 *
 */
public class Spider {
	
	//Limit the pages that the Crawler will search
	private final int MAX_PAGES_TO_SEARCH = 10;
	
	//Stores the pages visited in a set. Used a hashset because it does not store duplicates
	private Set<String> pagesVisited = new HashSet<String>();
	
	//Stores unvisited pages in a queue
	private Queue<String> pagesToVisit = new LinkedList<String>();
	
	//Gets the next url to visit by removing and returning the next url in queue
	private String getNextURL() {
		String nextURL;
		do {
			nextURL = this.pagesToVisit.poll();
		} while(this.pagesVisited.contains(nextURL));
		this.pagesVisited.add(nextURL);		//Then add the url to pagesVisited
		return nextURL;
	}
	
	//Search function that has a url as a parameter to find new pages to search
	//Throws an InterruptedException because of Thread.sleep()
	public void search(String url) throws InterruptedException {
		while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {		//While loop, crawl continues until we reach max pages
			String currentURL;
			SpiderLeg leg = new SpiderLeg();
			if(this.pagesToVisit.isEmpty()) {
				currentURL = url;
				this.pagesVisited.add(url);							//Add more links to pagesToVisit after a crawl
			} else {
				currentURL = this.getNextURL();						//Gets nextUrl if there is more pages to visit.
			}
			System.out.println(String.format("Connected to %s", currentURL));
			leg.crawl(currentURL);									//call crawl Method
			this.pagesToVisit.addAll(leg.getLinks());				//Get links from the leg.
			Thread.sleep(3000);
		}
		System.out.println("\nVisited " + this.pagesVisited.size() + " web page(s)");
	}
	
}
