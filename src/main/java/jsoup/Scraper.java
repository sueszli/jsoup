package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static util.ColorfulPrint.printYellow;

public class Scraper {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.revengeofficial.com/")
                .header("Accept-Language", "en-US")
                .get();

        printYellow("Now scraping: " + doc.title());

        // anchor element of each product
        Elements anchors = doc.select("a[href^=/collections/main/products]");

        for (Element a : anchors) {

            String itemName = a.text();
            // String relativePath = a.attributes().get("href");
            String absolutePath = a.absUrl("href");
            String price = findPrice(absolutePath);

            System.out.println(itemName + " --> " + price);
        }
    }

    private static String findPrice(String link) throws IOException {
        Document doc = Jsoup.connect(link)
                .header("Accept-Language", "en-US")
                .get();

        Elements span = doc.getElementsByAttribute("data-regular-price");
        return span.text();
    }
}
