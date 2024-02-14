package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvUtils {
    private CsvUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    public static List<CSVRecord> readCsv(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            return csvParser.getRecords();
        }
    }

    public static void writeCsv(List<String[]> data, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            for (String[] row : data) {
                csvPrinter.printRecord((Object[]) row);
            }
            writer.flush();
        }
    }


}
