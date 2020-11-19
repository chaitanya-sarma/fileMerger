package com.cerebra.fileMerger.ui.fileSelection;

import com.cerebra.fileMerger.ui.SubmitPanel;
import com.cerebra.fileMerger.util.FileDetails;
import com.cerebra.fileMerger.util.SharedInformation;
import com.cerebra.fileMerger.util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Trial data display panel.
 */
public class FileSelectionPanel {
    private final JDialog dialogBox;
    private final SharedInformation sharedInformation;
    protected SelectionTable selectionTable;
    private final SubmitPanel submitPanel;
    String type;
    private final List<FileDetails> fileDetailsList;

    public FileSelectionPanel(SharedInformation sharedInformation, String type) {
        this.sharedInformation = sharedInformation;
        this.selectionTable = new SelectionTable();
        this.type = type;
        fileDetailsList = new ArrayList<>();
        dialogBox = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
        dialogBox.setTitle("File - Selection ");

        dialogBox.setSize(new Dimension(sharedInformation.getXSize(), sharedInformation.getYSize() * 2 / 3));
        dialogBox.setLocation(Util.getLocation(sharedInformation.getXSize() * 2 / 3, sharedInformation.getYSize() * 2 / 3));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        //mainPanel.add(createDirectoryPanel(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(selectionTable.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        selectionTable.table.setFillsViewportHeight(true);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        submitPanel = new SubmitPanel();
        submitPanel.submit.addActionListener(this::mergeFiles);
        submitPanel.cancel.addActionListener(e -> dialogBox.setVisible(false));
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        dialogBox.add(mainPanel);
        populateFileDetails(sharedInformation.getInputFiles());
        populateTable();
        setVisible(true);
    }

    private void populateFileDetails(List<File> fileList) {
        for (File file : fileList) {
            FileDetails fileDetails = new FileDetails();
            fileDetails.setFileName(file.getName());
            fileDetails.setFilePath(file.getPath());
            fileDetails.setNoOfLines(Util.noOfLinesInFile(file));
            fileDetails.setHeaders(Util.getHeadersFromFile(file, type));
            fileDetailsList.add(fileDetails);
        }
    }

    private void mergeFiles(ActionEvent actionEvent) {
        int mergedFileCount = 0, failedFileCount = 0, lineCount = 0, count = 0;
        String mergedFile = null;
        List<String> headers = new ArrayList<>();
        submitPanel.submit.setEnabled(false);
        List<FileDetails> selectedFiles = new ArrayList<>();
        DefaultTableModel model = selectionTable.defaultTableModel;
        int rowCount = model.getRowCount();
        Vector dataVector = model.getDataVector();
        for (int i = 0; i < rowCount; i++) {
            ArrayList row = new ArrayList((Collection) dataVector.elementAt(i));
            if ((boolean) row.get(0)) {
                FileDetails selectedFileDetails = new FileDetails();
                selectedFileDetails.setFileName((String) row.get(1));
                selectedFileDetails.setFilePath((String) row.get(3));
                selectedFileDetails.setNoOfLines(Integer.parseInt((String) row.get(2)));
                for (FileDetails fileDetails : fileDetailsList) {
                    if (selectedFileDetails.getFilePath().equalsIgnoreCase(fileDetails.getFilePath())) {
                        selectedFileDetails.setHeaders(fileDetails.getHeaders());
                    }
                }
                selectedFiles.add(selectedFileDetails);
            }
        }
        if (selectedFiles.size() == 0) {
            JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
            d.setTitle("Dialog Box");
            d.setSize(500, 100);
            d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));

            JLabel lbl = new JLabel("Select atleast one file.");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            d.add(lbl);
            d.setVisible(true);
        } else {
            while (mergedFile == null) {
                String outputFileName = sharedInformation.getOutputFolder() + "/merged-" + count++ + "." + type;
                File file = new File(outputFileName);
                if (!file.exists()) mergedFile = outputFileName;
            }
            headers = selectedFiles.get(0).getHeaders();
            try {
                Files.createFile(Paths.get(mergedFile));
                Util.writeHeaders(headers, mergedFile, type);
                for (FileDetails sourceFile : selectedFiles)
                    if (headers.equals(sourceFile.getHeaders())) {
                        Util.copyFile(sourceFile.getFilePath(), mergedFile, type);
                        lineCount += sourceFile.getNoOfLines() - 1;
                        mergedFileCount++;
                    } else {
                        failedFileCount++;
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
            createStatusDialog(mergedFileCount, failedFileCount, lineCount, new File(mergedFile).getName(), d);
            d.setVisible(true);
            setVisible(false);
        }
    }

    private void createStatusDialog(int mergedFileCount, int failedFileCount, int lineCount, String name, JDialog d) {
        d.setTitle("Dialog Box");
        if (failedFileCount == 0)
            d.setSize(320, 200);
        else
            d.setSize(340, 250);
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setLayout(null);

        int yPos = 10;
        JLabel statusLbl;
        if (failedFileCount == 0)
            statusLbl = new JLabel("Merging successful");
        else
            statusLbl = new JLabel("Merging partially successful");
        statusLbl.setForeground(new Color(246, 129, 29));
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
        statusLbl.setBounds(20, yPos, 300, 30);
        panel.add(statusLbl);
        yPos += 40;

        Font font = new Font("Tahoma", Font.PLAIN, 16);
        JLabel fileLbl = new JLabel("No of files Merged: ");
        fileLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileLbl.setFont(font);
        fileLbl.setBounds(18, yPos, 200, 20);
        panel.add(fileLbl);

        JLabel fileMerged = new JLabel(String.valueOf(mergedFileCount));
        fileMerged.setHorizontalAlignment(SwingConstants.CENTER);
        fileMerged.setFont(font);
        fileMerged.setBounds(200, yPos, 100, 20);
        panel.add(fileMerged);
        yPos += 30;

        JLabel lineLbl = new JLabel("No of lines Merged: ");
        lineLbl.setHorizontalAlignment(SwingConstants.CENTER);
        lineLbl.setFont(font);
        lineLbl.setBounds(15, yPos, 200, 20);
        panel.add(lineLbl);

        JLabel lineMerged = new JLabel(String.valueOf(lineCount));
        lineMerged.setHorizontalAlignment(SwingConstants.CENTER);
        lineMerged.setFont(font);
        lineMerged.setBounds(200, yPos, 100, 20);
        panel.add(lineMerged);
        yPos += 30;

        if (failedFileCount > 0) {
            JLabel failedFileLbl = new JLabel("No of failed files: ");
            failedFileLbl.setHorizontalAlignment(SwingConstants.CENTER);
            failedFileLbl.setFont(font);
            failedFileLbl.setBounds(25, yPos, 200, 20);
            panel.add(failedFileLbl);

            JLabel failedFileMerged = new JLabel(String.valueOf(failedFileCount));
            failedFileMerged.setHorizontalAlignment(SwingConstants.CENTER);
            failedFileMerged.setFont(font);
            failedFileMerged.setBounds(200, yPos, 100, 20);
            panel.add(failedFileMerged);
            yPos += 30;
        }

        JLabel outputLbl = new JLabel("Output File: ");
        outputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputLbl.setFont(font);
        outputLbl.setBounds(20, yPos, 100, 20);
        panel.add(outputLbl);

        JLabel outputMerged = new JLabel(name);
        outputMerged.setHorizontalAlignment(SwingConstants.CENTER);
        outputMerged.setFont(font);
        outputMerged.setBounds(150, yPos, 150, 20);
        panel.add(outputMerged);
        yPos += 30;

        JButton submit = new JButton("Ok");
        submit.setBounds(150, yPos, 50, 25);
        submit.addActionListener(e -> {
            dialogBox.setVisible(false);
            d.setVisible(false);
        });
        panel.add(submit);
        d.add(panel);
    }

    private void populateTable() {
        DefaultTableModel model = selectionTable.defaultTableModel;
        model.setRowCount(0);

        for (FileDetails fileDetails : fileDetailsList) {
            Object[] row = {true, fileDetails.getFileName(), String.valueOf(fileDetails.getNoOfLines()), fileDetails.getFilePath()};
            model.addRow(row);
        }
        selectionTable.table.setModel(model);
    }


    public void setVisible(boolean visible) {
        dialogBox.setVisible(visible);
        DefaultTableModel model = (DefaultTableModel) selectionTable.table.getModel();
        model.setRowCount(0);
    }
}
