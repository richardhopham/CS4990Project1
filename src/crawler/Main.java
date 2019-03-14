package crawler;

public class Main {

	/**
	 * Main method to call the Spider class and begin crawling
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		//Call search Method, where seed URL is the argument
		Spider spider = new Spider();
		spider.search("http://www.cpp.edu");
		
		
		//OTHER TWO SEED URLs
//		spider = new Spider();
//		spider.search("http://www.reddit.com");
//		
//		spider = new Spider();
//		spider.search("http://en.wikipedia.org/wiki/Dog");
	}

}
