package crawler;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		Spider spider = new Spider();
		spider.search("http://www.cpp.edu");
		
		spider = new Spider();
		spider.search("http://www.reddit.com");
		
		spider = new Spider();
		spider.search("http://en.wikipedia.org/wiki/Dog");
	}

}
