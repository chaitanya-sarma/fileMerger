package com.cerebra.fileMerger.util;

import com.cerebra.fileMerger.util.fileHandling.UtilCSV;
import com.cerebra.fileMerger.util.fileHandling.UtilExcel;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cerebra.fileMerger.util.Constants.*;

@Component
public class Util {
    private static SharedInformation sharedInformation;

    @Autowired
    public void setSharedInformation(SharedInformation sharedInformation) {
        Util.sharedInformation = sharedInformation;
    }

    /**
     * File extension.
     * Ex: a.html gives .html
     *
     * @param fileName Name of file
     * @return File extension
     */
    public static String getFileExtension(String fileName) {
        int dotInd = fileName.lastIndexOf('.');
        // Dot in first position implies we are dealing with a hidden file rather than an extension
        return (dotInd > 0) ? fileName.substring(dotInd + 1) : fileName;
    }

    public static String writeHeaders(List<String> headers, String file, String type) {
        String returnStatus = Constants.SUCCESS;
        try {
            if (type.equalsIgnoreCase(CSV))
                UtilCSV.writeHeaders(headers, file, type);
            if (type.equalsIgnoreCase(XLSX))
                UtilExcel.writeHeaders(headers, file);
        } catch (Exception e) {
            sharedInformation.getLogger().info(e.getStackTrace().toString());
            returnStatus = "Exception: Please refer to the log file. for details" + e.toString() + e.getMessage();
        }
        return returnStatus;
    }

    /**
     * Copies a file from location source to location destination.
     *
     * @param source      File path
     * @param destination File Path
     * @param type        type of file
     * @return Status of copy.
     */
    public static String copyFile(String source, String destination, String type) {
        String returnStatus = "";
        sharedInformation.getLogger().info("Copying file from " + source + " to " + destination);
        try {
            if (type.equalsIgnoreCase(CSV))
                UtilCSV.copyFile(source, destination, type);
            if (type.equalsIgnoreCase(XLSX))
                UtilExcel.copyFile(source, destination);
        } catch (Exception e) {
            sharedInformation.getLogger().info(Arrays.toString(e.getStackTrace()));
            returnStatus = "Exception: Please refer to the log file. for details" + e.toString() + e.getMessage();
        }
        return returnStatus;
    }

    /**
     * Reads no of lines in the file.
     *
     * @param file Path to the file.
     * @param type type of file (csv, tsv, excel)
     * @return No of lines.
     */
    public static int noOfLinesInFile(File file, String type) {
        Logger logger = sharedInformation.getLogger();
        int linenumber = -1;
        try {
            if (type.equalsIgnoreCase(CSV))
                return UtilCSV.noOfLinesInFile(file);
            if (type.equalsIgnoreCase(XLSX))
                return UtilExcel.noOfLinesInFile(file);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }
        return linenumber;
    }

    public static List<String> getHeadersFromFile(File file, String type) {
        Logger logger = sharedInformation.getLogger();
        List<String> headers = new ArrayList<>();
        try {
            if (type.equalsIgnoreCase(CSV))
                return UtilCSV.getHeadersFromFile(file, type);
            if (type.equalsIgnoreCase(XLSX))
                return UtilExcel.getHeadersFromFile(file);
        } catch (IOException |
                CsvValidationException ex) {
            logger.warn(ex.getMessage());
        }
        return headers;
    }

    /**
     * Computes how long it took for a function to run.
     *
     * @param startTime start time.
     * @return String representing the difference.
     */
    public static String resultComputedOn(long startTime) {
        String computedOn;
        long stopTime = System.currentTimeMillis();
        long difference = stopTime - startTime;
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = TimeUnit.MILLISECONDS.toDays(difference);
        long hours1 = TimeUnit.MILLISECONDS.toHours(difference) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(difference));
        long minutes1 = TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference));
        long seconds1 = TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference));
        if (days == 0) {
            computedOn = String.format(" (Total Time: %02d Hr : %02d Min : %02d Sec)", hours1, minutes1, seconds1);
        } else {
            computedOn = String.format(" (Total Time: %dd Days : %02d Hr : %02d Min : %02d Sec)", days, hours, minutes, seconds);
        }
        return computedOn;
    }

    /**
     * Returns a file chooser
     *
     * @return File chooser
     */
    public static NativeFolderChooser getFolderChooser(String title) {
        NativeFolderChooser folderChooser;
        if (PathConstants.lastChosenFilePath != null)
            folderChooser = new NativeFolderChooser(PathConstants.lastChosenFilePath);
        else folderChooser = new NativeFolderChooser();
        sharedInformation.getLogger().info("Got file chooser");
        folderChooser.setDialogTitle(title);
        folderChooser.setMultiSelectionEnabled(true);
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return folderChooser;
    }

    public static NativeFolderChooser getFileChooser(String title) {
        NativeFolderChooser folderChooser;
        if (PathConstants.lastChosenFilePath != null)
            folderChooser = new NativeFolderChooser(PathConstants.lastChosenFilePath);
        else folderChooser = new NativeFolderChooser();
        sharedInformation.getLogger().info("Got file chooser");
        folderChooser.setDialogTitle(title);
        folderChooser.setMultiSelectionEnabled(true);
        folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return folderChooser;
    }

    public static JFileChooser getBasicFileChooser(String title) {
        JFileChooser folderChooser;
        if (PathConstants.lastChosenFilePath != null)
            folderChooser = new JFileChooser(new File(PathConstants.lastChosenFilePath));
        else folderChooser = new JFileChooser();
        sharedInformation.getLogger().info("Got file chooser");
        folderChooser.setDialogTitle(title);
        folderChooser.setMultiSelectionEnabled(true);
        folderChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return folderChooser;
    }

    public static void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(sharedInformation.getMainFrame(), message);
    }


    /**
     * Position of the dialog box
     *
     * @param x width of dialog box
     * @param y height of dialog box
     * @return Point
     */
    public static Point getLocation(int x, int y) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xCoord = (int) ((screenSize.getWidth() - x) / 2);
        int yCoord = (int) (screenSize.getHeight() - y) / 2;
        return new Point(xCoord, yCoord);
    }

    public static void populateFiles(ArrayList<File> selectedFiles, JTextField inputPath, String type) {
        String path = null;
        List<File> inputFiles = new ArrayList<>();
        while (selectedFiles.size() > 0) {
            File file = selectedFiles.remove(0);
            if (file.exists()) {
                if (path == null) path = file.getParent();
                if (file.isFile()) {
                    if (Util.getFileExtension(file.getName()).equalsIgnoreCase(type))
                        inputFiles.add(file);
                }
                if (file.isDirectory()) {
                    path = file.getParent();
                    File[] files = file.listFiles();
                    if (Objects.requireNonNull(files).length > 0)
                        selectedFiles.addAll(Arrays.asList(files));
                }
            } else {
                // folder not exits
                Util.showMessageDialog("Invalid file/folder " + file);
            }
        }
        if (inputFiles.size() == 0) {
            Util.showMessageDialog("Selected path doesn't have files. Please reselect.");
            return;
        }
        PathConstants.lastChosenFilePath = path;
        sharedInformation.setInputFiles(inputFiles);
        StringJoiner joiner = new StringJoiner(",");
        for (File f : sharedInformation.getInputFiles()) {
            joiner.add(f.getName());
        }
        inputPath.setText(joiner.toString());
    }

}