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
    JRadioButton excludeDirectoriesButton, includeDirectories;
    ButtonGroup directory;
    protected SelectionTable selectionTable;
    private final SubmitPanel submitPanel;
    String type;
    private final List<FileDetails> fileDetailsList;

    public FileSelectionPanel(SharedInformation sharedInformation, List<String> files, String type) {
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

        mainPanel.add(createDirectoryPanel(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(selectionTable.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        selectionTable.table.setFillsViewportHeight(true);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        submitPanel = new SubmitPanel();
        submitPanel.submit.addActionListener(this::mergeFiles);
        submitPanel.cancel.addActionListener(e -> dialogBox.setVisible(false));
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        dialogBox.add(mainPanel);
        populateFileDetails(files);
        populateTable();
        setVisible(true);
    }

    private void populateFileDetails(List<String> fileList) {
        for (String file : fileList) {
            FileDetails fileDetails = new FileDetails();
            fileDetails.setFileName(new File(file).getName());
            fileDetails.setFilePath(file);
            fileDetails.setNoOfLines(Util.noOfLinesInFile(file));
            fileDetails.setHeaders(Util.getHeadersFromFile(file, type));
            fileDetailsList.add(fileDetails);
        }
    }

    private void mergeFiles(ActionEvent actionEvent) {
        int fileCount = 0, lineCount = 0;
        List<String> headers = new ArrayList<>();
        submitPanel.submit.setEnabled(false);
        List<String> selectedFiles = new ArrayList<>();
        DefaultTableModel model = selectionTable.defaultTableModel;
        int rowCount = model.getRowCount();
        Vector dataVector = model.getDataVector();
        for (int i = 0; i < rowCount; i++) {
            List<Object> row = new ArrayList((Collection) dataVector.elementAt(i));
            if ((boolean) row.get(0))
                selectedFiles.add((String) row.get(3));
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
            for (FileDetails fileDetails : fileDetailsList) {
                if (selectedFiles.get(0).equalsIgnoreCase(fileDetails.getFilePath())) {
                    headers = fileDetails.getHeaders();
                }
            }
            for (String selectedFile : selectedFiles)
                for (FileDetails fileDetails : fileDetailsList) {
                    if (selectedFile.equalsIgnoreCase(fileDetails.getFilePath())) {
                        fileCount++;
                        lineCount += fileDetails.getNoOfLines() - 1;
                    }
                }
            String mergedFile = null;
            int count = 0;
            while (mergedFile == null) {
                String outputFileName = sharedInformation.getOutputFolder() + "/merged-" + count++ + "." + type;
                File file = new File(outputFileName);
                if (!file.exists()) mergedFile = outputFileName;
            }
            try {
                Files.createFile(Paths.get(mergedFile));
                Util.writeHeaders(headers, mergedFile, type);
                for (String sourceFile : selectedFiles)
                    Util.copyFile(sourceFile, mergedFile, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
            createStatusDialog(fileCount, lineCount, new File(mergedFile).getName(), d);
            d.setVisible(true);
            setVisible(false);
        }
    }

    private void createStatusDialog(int fileCount, int lineCount, String name, JDialog d) {
        d.setTitle("Dialog Box");
        d.setSize(320, 200);
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));
        JPanel panel = new JPanel();
        panel.setBorder(null);
        // panel.setBackground(new Color(128, 128, 128));
        panel.setLayout(null);

        int yPos = 10;
        JLabel statusLbl = new JLabel("Merging successful");
        statusLbl.setForeground(new Color(246, 129, 29));
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusLbl.setFont(new Font("Tahoma", Font.BOLD, 25));
        statusLbl.setBounds(10, yPos, 300, 30);
        panel.add(statusLbl);
        yPos += 40;

        JLabel fileLbl = new JLabel("No of files Merged: ");
        fileLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fileLbl.setBounds(10, yPos, 200, 25);
        panel.add(fileLbl);

        JLabel fileMerged = new JLabel(String.valueOf(fileCount));
        fileMerged.setHorizontalAlignment(SwingConstants.CENTER);
        fileMerged.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fileMerged.setBounds(210, yPos, 100, 25);
        panel.add(fileMerged);
        yPos += 35;

        JLabel lineLbl = new JLabel("No of lines Merged: ");
        lineLbl.setHorizontalAlignment(SwingConstants.CENTER);
        lineLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lineLbl.setBounds(5, yPos, 200, 25);
        panel.add(lineLbl);

        JLabel lineMerged = new JLabel(String.valueOf(lineCount));
        lineMerged.setHorizontalAlignment(SwingConstants.CENTER);
        lineMerged.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lineMerged.setBounds(210, yPos, 100, 25);
        panel.add(lineMerged);
        yPos += 35;

        JLabel outputLbl = new JLabel("Output File: ");
        outputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        outputLbl.setBounds(10, yPos, 150, 25);
        panel.add(outputLbl);

        JLabel outputMerged = new JLabel(name);
        outputMerged.setHorizontalAlignment(SwingConstants.CENTER);
        outputMerged.setFont(new Font("Tahoma", Font.PLAIN, 20));
        outputMerged.setBounds(160, yPos, 150, 25);
        panel.add(outputMerged);
        d.add(panel);
    }

    private JPanel createDirectoryPanel() {
        JPanel directoryPanel = new JPanel();
        directoryPanel.setBounds(20, 20, 300, 50);
        directoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel directoryLabel = new JLabel("Sub-directory");
        directoryLabel.setBounds(10, 10, 100, 30);
        directoryPanel.add(directoryLabel);

        excludeDirectoriesButton = new JRadioButton("Exclude");
        excludeDirectoriesButton.setBounds(120, 10, 100, 30);
        directoryPanel.add(excludeDirectoriesButton);
        includeDirectories = new JRadioButton("Include");
        includeDirectories.setBounds(230, 10, 100, 30);
        directoryPanel.add(includeDirectories);
        directory = new ButtonGroup();
        directory.add(excludeDirectoriesButton);
        directory.add(includeDirectories);
        return directoryPanel;
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
