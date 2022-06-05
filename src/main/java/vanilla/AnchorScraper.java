package vanilla;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AnchorScraper {

    private static final String webAddress = "https://en.wikipedia.org/";

    public static void main(String[] args) throws IOException {
        // get all anchors

        URL url = new URL(webAddress);
        Scanner in = new Scanner(url.openStream());

        while (in.hasNext()) {
            String word = in.next();
            if (word.contains("href=\"http")) {
                int start = word.indexOf("\"") + 1;
                int end = word.lastIndexOf("\"");
                System.out.println(word.substring(start, end));
            }
        }
    }

    private static void backupHTML() throws IOException {
        // fetch
        URL url = new URL(webAddress);
        Scanner in = new Scanner(url.openStream());

        // get current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = dtf.format(now);

        // write
        File file = new File("src/main/resources/backup" + formattedTime + ".txt");
        FileWriter fw = new FileWriter(file);
        while (in.hasNextLine()) {
            fw.write(in.nextLine()+"\n");
        }
        fw.close();
    }
}
