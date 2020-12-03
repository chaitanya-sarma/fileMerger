package com.cerebra.fileMerger.util.fileHandling;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cerebra.fileMerger.util.Constants.*;

public class UtilCSV {

    /**
     * Reads no of lines in the file.
     *
     * @param file Path to the file.
     * @return No of lines.
     */
    public static int noOfLinesInFile(File file) throws IOException {
        int linenumber = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.readLine() != null)
            linenumber++;
        return linenumber;
    }

    public static List<String> getHeadersFromFile(File file, String type) throws IOException, CsvValidationException {
        List<String> headers = new ArrayList<>();
        CSVIterator csvIterator = getCSVReader(file, type);
        while (csvIterator.hasNext()) {
            String[] nextLine = csvIterator.next();
            if (nextLine.length == 1 && nextLine[0].equals("")) break;
            headers = Arrays.asList(nextLine);
            break;
        }
        return headers;
    }

    public static void writeHeaders(List<String> headers, String file, String type) throws IOException {
        Files.createFile(Paths.get(file));
        CSVWriter writer = getCSVWriter(new File(file), type);
        writer.writeNext(headers.toArray(new String[0]));
        writer.close();
    }

    /**
     * Copies a file from location source to location destination.
     *
     * @param source      File path
     * @param destination File Path
     * @param type        type of file
     */
    public static void copyFile(String source, String destination, String type) throws IOException, CsvValidationException {
        int lineNo = 0;
        File sourceFile = new File(source);
        File destFile = new File(destination);
        CSVIterator csvIterator = getCSVReader(sourceFile, type);
        CSVWriter writer = getCSVWriter(destFile, type);
        while (csvIterator.hasNext()) {
            String[] nextLine = csvIterator.next();
            if (lineNo++ != 0)
                writer.writeNext(nextLine);
        }
        writer.close();
    }

    private static CSVIterator getCSVReader(File sourceFile, String type) throws IOException, CsvValidationException {
        CSVParser parser;
        if (type.equalsIgnoreCase(TSV)) {
            parser = new CSVParserBuilder().withSeparator('\t').build();
        } else {
            parser = new CSVParserBuilder().build();
        }
        return new CSVIterator(new CSVReaderBuilder(new FileReader(sourceFile)).withCSVParser(parser).build());
    }

    private static CSVWriter getCSVWriter(File destFile, String type) throws IOException {
        if (type.equalsIgnoreCase(TSV)) {
            return new CSVWriter(new FileWriter(destFile, true), '\t', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        } else {
            return new CSVWriter(new FileWriter(destFile, true), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        }
    }


}
