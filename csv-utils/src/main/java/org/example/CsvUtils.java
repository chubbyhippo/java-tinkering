package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvUtils {
    private CsvUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    public static List<CSVRecord> readCsv(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path);
             var csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            return csvParser.getRecords();
        }
    }

    public static void writeCsv(List<String[]> data, Path path) throws IOException {
        try (var writer = Files.newBufferedWriter(path);
             var csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            for (String[] row : data) {
                csvPrinter.printRecord((Object[]) row);
            }
            writer.flush();
        }
    }


}
