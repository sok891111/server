package crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler  {
	
	ArrayList<String> user = new ArrayList<>();
	
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&&  href.startsWith("http://movie.naver.com/movie");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int tempIndex = 0;
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			while(true){
				tempIndex = html.indexOf("javascript:showPointListByNid")+30;
				if(tempIndex == 29) break;
				String userA = html.substring(tempIndex,tempIndex+7);
				html = html.replace(html.substring(0,tempIndex), "");
				user.add(userA);
			}
			UserCrawler u = new UserCrawler();
			try {
				u.userCrawlStart(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<WebURL> links = htmlParseData.getOutgoingUrls();
            Document doc = Jsoup.parseBodyFragment(html);
			//System.out.println("Text length: " + text.length());
			//System.out.println("제목 : ");
			//System.out.println("Number of outgoing links: " + links.size());
		}
	}
	public ArrayList<String> getUser(){
		return user;
	}
}
