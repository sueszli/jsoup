package jsoup_crawles;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.ColorfulPrint;

import java.io.IOException;
import java.util.ArrayList;

public class SingleThread {

    final private static String WEB_ADDRESS = "https://en.wikipedia.org/";
    final private static int MAX_CRAWL_DEPTH = 5;

    public static void main(String[] args) {
        // start at level 0
        crawl(0, WEB_ADDRESS, new ArrayList<String>());
    }

    private static void crawl(int level, String url, ArrayList<String> visitedList) {
        if (level > MAX_CRAWL_DEPTH) {
            return;
        }

        // fetch
        Document doc = null;
        try {
            Connection con = Jsoup.connect(url);
            doc = con.header("Accept-Language", "en-US").get();

            if (con.response().statusCode() != 200) {
                ColorfulPrint.printRed("Status-code to " + url + " wasn't 200.");
                return;
            }

            System.out.println("[" + level + "]: " + doc.title());
            visitedList.add(url);

        } catch (IOException ignored) {
            ColorfulPrint.printRed("IOException occured for " + url + ".");
            return;
        }

        // repeat for children
        Elements as = doc.select("a[href]");
        if (as.size() == 0) {
            ColorfulPrint.printRed("No children of " + url + ".");
            return;
        }
        for (Element a : as) {
            String link = a.absUrl("href");
            if (!visitedList.contains(link)) {
                crawl(++level, link, visitedList);
            }
        }
    }
}
