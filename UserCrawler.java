package crawler;

import java.util.ArrayList;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class UserCrawler {
	public void userCrawlStart(ArrayList<String> userInfo) throws Exception {
		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 7;
		System.out.println("들어옴 ");
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(0);
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		if (userInfo != null) {
			for (int i = 0; i < userInfo.size(); i++) {
				controller
						.addSeed("http://movie.naver.com/movie/point/af/list.nhn?st=nickname&target=after&sword="
								+ userInfo.get(i));
				System.out.println(userInfo.get(i));
			}
		}

		controller.start(UserCra.class, numberOfCrawlers);
	}
}
