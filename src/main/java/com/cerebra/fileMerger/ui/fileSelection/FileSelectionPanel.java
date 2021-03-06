package com.cerebra.fileMerger.ui.fileSelection;

import com.cerebra.fileMerger.ui.SubmitPanel;
import com.cerebra.fileMerger.util.FileDetails;
import com.cerebra.fileMerger.util.SharedInformation;
import com.cerebra.fileMerger.util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private boolean continueMerge;
    private List<String> mergedFiles, failedFiles;
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

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(selectionTable.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        selectionTable.table.setFillsViewportHeight(true);
        filePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel movePanel = new JPanel();
        movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
        movePanel.setBorder(BorderFactory.createEmptyBorder(75, 5, 0, 5));
        JButton up = new JButton("  Up  ");
        up.addActionListener(this::moveUp);
        JButton down = new JButton("Down");
        down.addActionListener(this::moveDown);
        movePanel.add(up);
        movePanel.add(down);
        filePanel.add(movePanel, BorderLayout.EAST);
        mainPanel.add(filePanel, BorderLayout.CENTER);

        submitPanel = new SubmitPanel(true);
        submitPanel.submit.addActionListener(this::mergeFiles);
        submitPanel.cancel.addActionListener(e -> dialogBox.setVisible(false));
        mainPanel.add(submitPanel, BorderLayout.SOUTH);
        dialogBox.add(mainPanel);
        populateFileDetails(sharedInformation.getInputFiles());
        populateTable();
        setVisible(true);
    }

    private void moveDown(ActionEvent actionEvent) {
        int selectedRow = selectionTable.table.getSelectedRow();
        if (selectedRow == -1) return;
        DefaultTableModel model = selectionTable.defaultTableModel;
        int rowCount = model.getRowCount();
        if (selectedRow < rowCount - 1) {
            model.moveRow(selectedRow, selectedRow, selectedRow + 1);
            selectionTable.table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
        }
    }

    private void moveUp(ActionEvent actionEvent) {
        int selectedRow = selectionTable.table.getSelectedRow();
        if (selectedRow == -1) return;
        DefaultTableModel model = selectionTable.defaultTableModel;
        if (selectedRow > 0) {
            model.moveRow(selectedRow, selectedRow, selectedRow - 1);
            selectionTable.table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
        }
    }

    private void populateFileDetails(List<File> fileList) {
        for (File file : fileList) {
            FileDetails fileDetails = new FileDetails();
            fileDetails.setFileName(file.getName());
            fileDetails.setFilePath(file.getPath());
            fileDetails.setNoOfLines(Util.noOfLinesInFile(file, type));
            fileDetails.setHeaders(Util.getHeadersFromFile(file, type));
            fileDetailsList.add(fileDetails);
        }
    }

    private void mergeFiles(ActionEvent actionEvent) {
        int lineCount = 0, count = 0;
        String mergedFile = null;
        String mergedLogFileName = null;
        String failedLogFileName = null;
        List<String> headers = new ArrayList<>();
        submitPanel.submit.setEnabled(false);
        continueMerge = true;
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
                String outputFileName = sharedInformation.getOutputFolder() + "/merge-" + count + "." + type;
                String csvOutputFileName = sharedInformation.getOutputFolder() + "/merge-" + count + "." + type;
                String excelOutputFileName = sharedInformation.getOutputFolder() + "/merge-" + count + "." + type;
                File csvfile = new File(csvOutputFileName);
                File excelfile = new File(excelOutputFileName);
                if (!(csvfile.exists() || excelfile.exists())) {
                    mergedFile = outputFileName;
                    mergedLogFileName = sharedInformation.getOutputFolder() + "/merge-" + count + "-success.txt";
                    failedLogFileName = sharedInformation.getOutputFolder() + "/merge-" + count + "-failure.txt";
                }
                count++;
            }
            headers = selectedFiles.get(0).getHeaders();
            if (differentFormatFiles(headers, selectedFiles)) {
                continueMerge = false;
                JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
                createConfirmationDialog(d);
                d.setVisible(true);
            }
            if (continueMerge) {
                mergedFiles = new ArrayList<>();
                failedFiles = new ArrayList<>();
                try (BufferedWriter mergedBr = new BufferedWriter(new FileWriter(new File(mergedLogFileName)));
                     BufferedWriter failedBr = new BufferedWriter(new FileWriter(new File(failedLogFileName)))) {
                    Util.writeHeaders(headers, mergedFile, type);
                    for (FileDetails sourceFile : selectedFiles)
                        if (headers.equals(sourceFile.getHeaders())) {
                            Util.copyFile(sourceFile.getFilePath(), mergedFile, type);
                            lineCount += sourceFile.getNoOfLines() - 1;
                            mergedFiles.add(sourceFile.getFilePath());
                            mergedBr.write(sourceFile.getFilePath());
                            mergedBr.newLine();
                        } else {
                            failedFiles.add(sourceFile.getFilePath());
                            failedBr.write(sourceFile.getFilePath());
                            failedBr.newLine();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
                createStatusDialog(lineCount, new File(mergedFile).getName(), d);
                d.setVisible(true);
            }
            setVisible(false);
        }
    }

    private boolean differentFormatFiles(List<String> headers, List<FileDetails> selectedFiles) {
        for (FileDetails sourceFile : selectedFiles) {
            if (!headers.equals(sourceFile.getHeaders()))
                return true;
        }
        return false;
    }

    private void createStatusDialog(int lineCount, String name, JDialog d) {
        d.setTitle("Status");
        if (failedFiles.size() == 0)
            d.setSize(320, 200);
        else
            d.setSize(340, 250);
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setLayout(null);

        int yPos = 10;
        JLabel statusLbl;
        if (failedFiles.size() == 0)
            statusLbl = new JLabel("Merge successful");
        else
            statusLbl = new JLabel("Merge partially successful");
        statusLbl.setForeground(new Color(246, 129, 29));
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
        statusLbl.setBounds(20, yPos, 300, 30);
        panel.add(statusLbl);
        yPos += 40;

        Font font = new Font("Tahoma", Font.PLAIN, 16);
        JLabel fileLbl = new JLabel("Files Merged: ");
        fileLbl.setHorizontalAlignment(SwingConstants.LEFT);
        fileLbl.setFont(font);
        fileLbl.setBounds(20, yPos, 150, 20);
        panel.add(fileLbl);

        JLabel fileMerged = new JLabel(String.valueOf(mergedFiles.size()));
        fileMerged.setHorizontalAlignment(SwingConstants.LEFT);
        fileMerged.setFont(font);
        fileMerged.setBounds(175, yPos, 70, 20);
        panel.add(fileMerged);

        JButton viewMerged = new JButton(" View ");
        viewMerged.setHorizontalAlignment(SwingConstants.LEFT);
        viewMerged.setFont(font);
        viewMerged.setBounds(250, yPos, 60, 20);
        viewMerged.addActionListener(this::displayMergedFiles);
        panel.add(viewMerged);

        yPos += 30;

        JLabel lineLbl = new JLabel("Lines Merged: ");
        lineLbl.setHorizontalAlignment(SwingConstants.LEFT);
        lineLbl.setFont(font);
        lineLbl.setBounds(20, yPos, 150, 20);
        panel.add(lineLbl);

        JLabel lineMerged = new JLabel(String.valueOf(lineCount));
        lineMerged.setHorizontalAlignment(SwingConstants.LEFT);
        lineMerged.setFont(font);
        lineMerged.setBounds(175, yPos, 70, 20);
        panel.add(lineMerged);
        yPos += 30;

        if (failedFiles.size() > 0) {
            JLabel failedFileLbl = new JLabel("Files Failed: ");
            failedFileLbl.setHorizontalAlignment(SwingConstants.LEFT);
            failedFileLbl.setFont(font);
            failedFileLbl.setBounds(20, yPos, 150, 20);
            panel.add(failedFileLbl);

            JLabel failedFileMerged = new JLabel(String.valueOf(failedFiles.size()));
            failedFileMerged.setHorizontalAlignment(SwingConstants.LEFT);
            failedFileMerged.setFont(font);
            failedFileMerged.setBounds(175, yPos, 70, 20);
            panel.add(failedFileMerged);

            JButton viewFailed = new JButton(" View ");
            viewFailed.setHorizontalAlignment(SwingConstants.LEFT);
            viewFailed.setFont(font);
            viewFailed.setBounds(250, yPos, 60, 20);
            viewFailed.addActionListener(this::displayFailedFiles);
            panel.add(viewFailed);

            yPos += 30;
        }

        JLabel outputLbl = new JLabel("Output File: ");
        outputLbl.setHorizontalAlignment(SwingConstants.LEFT);
        outputLbl.setFont(font);
        outputLbl.setBounds(20, yPos, 150, 20);
        panel.add(outputLbl);

        JLabel outputMerged = new JLabel(name);
        outputMerged.setHorizontalAlignment(SwingConstants.LEFT);
        outputMerged.setFont(font);
        outputMerged.setBounds(175, yPos, 200, 20);
        panel.add(outputMerged);
        yPos += 30;

        JButton submit = new JButton("Ok");
        submit.setBounds(135, yPos, 50, 25);
        submit.addActionListener(e -> {
            dialogBox.setVisible(false);
            d.setVisible(false);
        });
        panel.add(submit);

        d.add(panel);
    }

    private void displayFailedFiles(ActionEvent actionEvent) {
        JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
        d.setTitle("Failed Files");
        d.setSize(Math.min(800, failedFiles.get(0).length() * 8), Math.min(500, failedFiles.size() * 10));
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));

        JLabel statusLbl;
        StringBuilder sb = new StringBuilder();
        sb.append("<html><div style='text-align: center;'>");
        for (String failedFile : failedFiles)
            sb.append("<br>").append(failedFile);
        sb.append("</div></html>");
        statusLbl = new JLabel(sb.toString());
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        d.add(new JScrollPane(statusLbl, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        d.setVisible(true);
    }

    private void displayMergedFiles(ActionEvent actionEvent) {
        JDialog d = new JDialog(sharedInformation.getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL);
        d.setTitle("Merged Files");
        d.setSize(Math.min(800, mergedFiles.get(0).length() * 8), Math.min(500, mergedFiles.size() * 10));
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));

        JLabel statusLbl;
        StringBuilder sb = new StringBuilder();
        sb.append("<html><div style='text-align: center;'>");
        for (String mergedFile : mergedFiles)
            sb.append("<br>").append(mergedFile);
        sb.append("</div></html>");
        statusLbl = new JLabel(sb.toString());

        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        d.add(new JScrollPane(statusLbl, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        d.setVisible(true);
    }

    private void createConfirmationDialog(Dialog d) {
        d.setTitle("Confirmation Dialog");
        d.setSize(320, 150);
        d.setLocation(Util.getLocation(100, sharedInformation.getYSize() * 2 / 3));
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setLayout(new BorderLayout());
        JLabel statusLbl;
        statusLbl = new JLabel("<html><div style='text-align: center;'>Selected files does not have similar format.<br>Merge only similar files.</div></html>");
        statusLbl.setForeground(new Color(246, 129, 29));
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusLbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel.add(statusLbl, BorderLayout.CENTER);

        SubmitPanel submitPanel = new SubmitPanel(false);
        submitPanel.submit.addActionListener(e -> {
            this.continueMerge = true;
            d.setVisible(false);
        });
        submitPanel.cancel.addActionListener(e -> d.setVisible(false));
        panel.add(submitPanel, BorderLayout.SOUTH);
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
