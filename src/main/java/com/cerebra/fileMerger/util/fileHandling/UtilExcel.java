package com.cerebra.fileMerger.util.fileHandling;

import com.cerebra.fileMerger.util.SharedInformation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UtilExcel {
    private static SharedInformation sharedInformation;

    @Autowired
    public void setSharedConstants(SharedInformation sharedInformation) {
        UtilExcel.sharedInformation = sharedInformation;
    }

    /**
     * Reads no of lines in the file.
     *
     * @param file Path to the file.
     * @return No of lines.
     */
    public static int noOfLinesInFile(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        workbook.close();
        return lastRowNum;
    }

    public static List<String> getHeadersFromFile(File file) throws IOException {
        List<String> headers = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        for (Cell cell : row)
            headers.add(cell.getStringCellValue());
        workbook.close();
        return headers;
    }

    public static void writeHeaders(List<String> headers, String file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Merged");
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++)
            header.createCell(i).setCellValue(headers.get(i));
        workbook.write(fileOutputStream);
        workbook.close();
    }

    /**
     * Copies a file from location source to location destination.
     *
     * @param source      File path
     * @param destination File Path
     */
    public static void copyFile(String source, String destination) throws IOException {
        String returnStatus = "";
        sharedInformation.getLogger().info("Copying file from " + source + " to " + destination);
        File sourceFile = new File(source);
        File destFile = new File(destination);

        Workbook inputWorkbook = new XSSFWorkbook(new FileInputStream(sourceFile));
        Sheet inputSheet = inputWorkbook.getSheetAt(0);
        int inputRow = 2;
        int maxInputRows = inputSheet.getLastRowNum();

        FileInputStream fileInputStream = new FileInputStream(destFile);
        Workbook outputWorkbook = new XSSFWorkbook(fileInputStream);
        Sheet outputSheet = outputWorkbook.getSheetAt(0);
        int outputRow = outputSheet.getLastRowNum() + 1;

        while (inputRow <= maxInputRows) {
            Row inputSheetRow = inputSheet.getRow(inputRow++);
            Row outputSheetRow = outputSheet.createRow(outputRow++);
            for (int i = 0; i < inputSheetRow.getLastCellNum(); i++) {
                String s = String.valueOf(inputSheetRow.getCell(i));
                outputSheetRow.createCell(i).setCellValue(!s.equals("null") ? s : "");
            }
        }
        fileInputStream.close();
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);
        outputWorkbook.write(fileOutputStream);
        inputWorkbook.close();
        outputWorkbook.close();
    }
}
