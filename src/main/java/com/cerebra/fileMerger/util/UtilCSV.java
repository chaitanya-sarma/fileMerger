package com.cerebra.fileMerger.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class UtilCSV {
    private static SharedInformation sharedInformation;

    @Autowired
    public void setSharedConstants(SharedInformation sharedInformation) {
        UtilCSV.sharedInformation = sharedInformation;
    }

    /**
     * Gets the header information from File.
     *
     * @param fileName file name
     * @return list of headers.
     */
    public static List<String> getHeaders(String fileName) {
        String[] headers = null;
        List<String> headerList = new ArrayList<>();
        try {
            //initialized a CsvReader object with file path. Assumes default separator as Comma.
            CSVReader reader = new CSVReader(new FileReader(fileName));
            // you have to always call readHeaders first before you do any other operation
            headers = reader.readNext();
            reader.close();
        } catch (Exception e) {
            //Log Message
            sharedInformation.getLogger().error("Error when reading file:" + fileName + "\n Pls check log file for details.");
        }
        if (headers != null) {
            headerList.addAll(Arrays.asList(headers));
        }
        return headerList;
    }

    /**
     * Copies the selected fields into a new file. First field is selected by default.
     *
     * @param fileName       File Name
     * @param requiredFields fields to be copied
     */
    public static String createCSVwithRequiredFields(String fileName, String outputFileName, List<String> requiredFields) {
        String status = Constants.SUCCESS;
        List<String> originalFileHeaders = getHeaders(fileName);
        List<Integer> indices = requiredIndices(originalFileHeaders, requiredFields);
        indices.add(0, 0);
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFileName));

            String[] nextLine;
            List<List<String>> outputLines = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
                List<String> outputLine = new ArrayList<>();
                for (Integer index : indices) {
                    outputLine.add(nextLine[index]);
                }
                outputLines.add(outputLine);
                csvWriter.writeNext(outputLine.toArray(new String[outputLine.size()]));
            }
            // This is the data from BMS unprocessed
          /*  if (fileName.contains("BMS") && !fileName.contains("selected"))
                processBMSDate(outputLines, csvWriter);
           */
            reader.close();
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException | CsvValidationException e) {
            sharedInformation.getLogger().error(e.getMessage() + e.getStackTrace());
            status = e.getMessage();
        }
        return status;
    }


    /**
     * Gets the required header indices in sorted order.
     *
     * @param originalFileHeaders Header list
     * @param requiredFields      required columns.
     * @return Sorted list of columns indices.
     */
    private static List<Integer> requiredIndices(List<String> originalFileHeaders, List<String> requiredFields) {
        List<Integer> requiredIndices = new ArrayList<>();
        for (String field : requiredFields) {
            requiredIndices.add(originalFileHeaders.indexOf(field));
        }
        Collections.sort(requiredIndices);
        return requiredIndices;
    }
}
