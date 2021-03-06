package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.ui.fileSelection.FileSelectionPanel;
import com.cerebra.fileMerger.util.NativeFolderChooser;
import com.cerebra.fileMerger.util.SharedInformation;
import com.cerebra.fileMerger.util.Util;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static com.cerebra.fileMerger.util.Constants.*;

public class TSVSelectionPanel extends JPanel {

    public JTextField inputPath, outputPath;
    JButton inputBrowse, outputBrowse, mergeBtn;
    SharedInformation sharedInformation;

    JPanel createTSVSelectionPanel(SharedInformation sharedInformation, int xPos, int yPos, int width, int height) {
        this.sharedInformation = sharedInformation;
        setBorder(new LineBorder(Color.WHITE));
        setOpaque(true);
        setBackground(new Color(187, 186, 186, 226));
        setBounds(xPos, yPos, width, height);
        setLayout(null);

        yPos = 10;
        JLabel fileMergerLbl = new JLabel("TSV File Merger");
        fileMergerLbl.setForeground(new Color(246, 129, 29));
        fileMergerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileMergerLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
        fileMergerLbl.setBounds((width - 200) / 2, yPos, 200, 30);
        add(fileMergerLbl);
        yPos += 30;


        JLabel inputIconLbl = new JLabel("");
        inputIconLbl.setToolTipText("Input");
        inputIconLbl.setIcon(new ImageIcon(TSVSelectionPanel.class.getResource("/input.png")));
        inputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputIconLbl.setBounds(20, yPos, 40, 40);
        add(inputIconLbl);
        yPos += 40;
        JLabel inputLbl = new JLabel("Input");
        inputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputLbl.setForeground(new Color(255, 255, 255));
        inputLbl.setBounds(15, yPos, 50, 20);
        inputLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(inputLbl);
        yPos += 20;

        inputPath = new JTextField(20);
        inputPath.setBounds(80, yPos - 25 - 15, 300, 25);
        inputPath.setEditable(false);
        add(inputPath);

        inputBrowse = new JButton("File");
        inputBrowse.addActionListener(this::inputFileBrowseAction);
        inputBrowse.setBounds(400, yPos - 25 - 15, 80, 25);
        add(inputBrowse);

        inputBrowse = new JButton("Folder");
        inputBrowse.addActionListener(this::inputFolderBrowseAction);
        inputBrowse.setBounds(490, yPos - 25 - 15, 80, 25);
        add(inputBrowse);

        JLabel outputIconLbl = new JLabel("");
        outputIconLbl.setToolTipText("Output");
        outputIconLbl.setIcon(new ImageIcon(TSVSelectionPanel.class.getResource("/output.png")));
        outputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputIconLbl.setBounds(20, yPos, 40, 40);
        yPos += 40;
        add(outputIconLbl);

        JLabel outputLbl = new JLabel("Output");
        outputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputLbl.setForeground(new Color(255, 255, 255));
        outputLbl.setBounds(14, yPos, 55, 20);
        outputLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(outputLbl);
        yPos += 20;

        outputPath = new JTextField(20);
        outputPath.setBounds(80, yPos - 25 - 15, 300, 25);
        outputPath.setEditable(false);
        add(outputPath);

        outputBrowse = new JButton("Browse");
        outputBrowse.addActionListener(this::outputBrowseAction);
        outputBrowse.setBounds(400, yPos - 25 - 15, 80, 25);
        add(outputBrowse);

        mergeBtn = new JButton("Merge");
        mergeBtn.addActionListener(this::mergeAction);
        mergeBtn.setBounds((width - 80) / 2, yPos + 5, 80, 30);
        add(mergeBtn);

      /*  StringJoiner joiner = new StringJoiner(",");
        for (File f : sharedInformation.getInputFiles()) {
            joiner.add(f.getName());
        }
        inputPath.setText(joiner.toString());
        outputPath.setText(sharedInformation.getOutputFolder());
        */return this;
    }

    private void buttonEnabled(boolean b) {
        inputBrowse.setEnabled(b);
        outputBrowse.setEnabled(b);
        mergeBtn.setEnabled(b);
    }

    private void inputFileBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        NativeFolderChooser fileChooser = Util.getFileChooser("Select input files");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT (Tab delimited) (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            ArrayList<File> selectedFiles = new ArrayList<>(Arrays.asList(fileChooser.getSelectedFiles()));
            Util.populateFiles(selectedFiles, inputPath, TXT);
        }
        buttonEnabled(true);
    }

    private void inputFolderBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        NativeFolderChooser fileChooser = Util.getFolderChooser("Select input folder");
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            ArrayList<File> selectedFiles = new ArrayList<>(Arrays.asList(fileChooser.getSelectedFiles()));
            Util.populateFiles(selectedFiles, inputPath, TXT);
        }
        buttonEnabled(true);
    }


    private void outputBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        NativeFolderChooser fileChooser = Util.getFolderChooser("Select output folder");
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.DIRECTORIES_ONLY) {
            if (Files.isDirectory(Paths.get(fileChooser.getSelectedFile().toString()))) {
                String directory = fileChooser.getSelectedFile().toString();
                sharedInformation.setOutputFolder(directory);
                outputPath.setText(directory);
            } else {
                // folder not exits
                Util.showMessageDialog("Please select a valid folder");
            }
        }
        buttonEnabled(true);
    }

    private void mergeAction(ActionEvent actionEvent) {
        if (sharedInformation.getInputFiles() == null) {
            Util.showMessageDialog("Please select input folder");
            return;
        }
        if (sharedInformation.getOutputFolder() == null) {
            Util.showMessageDialog("Please select output folder");
            return;
        }
        buttonEnabled(false);
        new FileSelectionPanel(sharedInformation, TSV);
        buttonEnabled(true);
    }
}
