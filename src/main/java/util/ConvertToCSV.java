package util;

import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertToCSV {

    public static final String FILE_PATH = "src/main/resources/";

    public static void createCSV(String fileName, List<String[]> data) {
        // see: http://opencsv.sourceforge.net/#writing

        // create new file
        File file = new File(FILE_PATH + fileName + ".csv");

        // write into file
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeAll(data);
        } catch (IOException ignore) {
        }
    }

    public static void createCSVWithHeader(String fileName,  String[] header, List<String[]> data) {
        // see: http://opencsv.sourceforge.net/#writing

        // create new file
        File file = new File(FILE_PATH + fileName + ".csv");

        // write into file
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeNext(header);
            writer.writeAll(data);
        } catch (IOException ignore) {
        }
    }

    public static void appendCSV(String fileName, String[] data) {
        // write into file
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH + fileName + ".csv", true))) {
            writer.writeNext(data);
        } catch (IOException ignore) {
        }
    }
}
