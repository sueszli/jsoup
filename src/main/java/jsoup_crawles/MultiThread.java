package jsoup_crawles;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.ColorfulPrint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NOT IMPLEMENTED IN A USEFUL WAY
 */
public class MultiThread {
    final static public String WEB_ADDRESS = "https://en.wikipedia.org/";
    final static public int MAX_CRAWL_DEPTH = 5;
    final static public AtomicInteger processIDs = new AtomicInteger();

    public static void main(String[] args) {
        ArrayList<Crawler> bots = new ArrayList<>();

        // not useful because we dont have enough synchronization for multiple bots to access the same site
        bots.add(new Crawler(WEB_ADDRESS));
        bots.add(new Crawler(WEB_ADDRESS));
        bots.add(new Crawler(WEB_ADDRESS));

        for (Crawler c : bots) {
            try {
                c.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





    static class Crawler implements Runnable {
        final private int ID = processIDs.incrementAndGet();
        final private ArrayList<String> visitedList = new ArrayList<>();
        private final String INIT_ADDRESS;

        private final Thread thread = new Thread(this);

        public Crawler(String url) {
            this.INIT_ADDRESS = url;
            System.out.println("Created Crawler <" + ID + ">");

            thread.start();
        }

        @Override
        public void run() {
            crawl(1, INIT_ADDRESS);
        }

        private void crawl(int level, String url) {
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

                System.out.println("<" + this.ID + ">[" + level + "]: " + doc.title());
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
                    crawl(++level, link);
                }
            }
        }

        public Thread getThread() {
            return thread;
        }
    }
}
