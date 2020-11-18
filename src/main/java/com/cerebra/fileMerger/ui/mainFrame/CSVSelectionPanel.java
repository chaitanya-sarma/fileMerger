package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.ui.fileSelection.FileSelectionPanel;
import com.cerebra.fileMerger.util.NativeFolderChooser;
import com.cerebra.fileMerger.util.SharedInformation;
import com.cerebra.fileMerger.util.Util;
import li.flor.nativejfilechooser.NativeJFileChooser;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.cerebra.fileMerger.util.Constants.CSV;

public class CSVSelectionPanel extends JPanel {

    public JTextField inputPath, outputPath;
    JButton inputBrowse, outputBrowse, mergeBtn;
    SharedInformation sharedInformation;

    JPanel createCSVSelectionPanel(SharedInformation sharedInformation, int xPos, int yPos, int width, int height) {
        this.sharedInformation = sharedInformation;
        setBorder(new LineBorder(Color.WHITE));
        setOpaque(true);
        setBackground(new Color(187, 186, 186, 226));
        setBounds(xPos, yPos, width, height);
        setLayout(null);

        yPos = 10;
        JLabel fileMergerLbl = new JLabel("CSV File Merger");
        fileMergerLbl.setForeground(new Color(246, 129, 29));
        fileMergerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileMergerLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
        fileMergerLbl.setBounds((width - 200) / 2, yPos, 200, 30);
        add(fileMergerLbl);
        yPos += 30;


        JLabel inputIconLbl = new JLabel("");
        inputIconLbl.setToolTipText("Input");
        inputIconLbl.setIcon(new ImageIcon(CSVSelectionPanel.class.getResource("/input.png")));
        inputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputIconLbl.setBounds(70, yPos, 40, 40);
        add(inputIconLbl);
        yPos += 40;
        JLabel inputLbl = new JLabel("Input");
        inputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputLbl.setForeground(new Color(255, 255, 255));
        inputLbl.setBounds(65, yPos, 50, 20);
        inputLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(inputLbl);
        yPos += 20;

        inputPath = new JTextField(20);
        inputPath.setBounds(130, yPos - 25 - 15, 300, 25);
        inputPath.setEditable(false);
        add(inputPath);

        inputBrowse = new JButton("Browse");
        inputBrowse.addActionListener(this::inputBrowseAction);
        inputBrowse.setBounds(450, yPos - 25 - 15, 80, 25);
        add(inputBrowse);

        JLabel outputIconLbl = new JLabel("");
        outputIconLbl.setToolTipText("Output");
        outputIconLbl.setIcon(new ImageIcon(CSVSelectionPanel.class.getResource("/output.png")));
        outputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputIconLbl.setBounds(70, yPos, 40, 40);
        yPos += 40;
        add(outputIconLbl);

        JLabel outputLbl = new JLabel("Output");
        outputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputLbl.setForeground(new Color(255, 255, 255));
        outputLbl.setBounds(64, yPos, 55, 20);
        outputLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(outputLbl);
        yPos += 20;

        outputPath = new JTextField(20);
        outputPath.setBounds(130, yPos - 25 - 15, 300, 25);
        outputPath.setEditable(false);
        add(outputPath);

        outputBrowse = new JButton("Browse");
        outputBrowse.addActionListener(this::outputBrowseAction);
        outputBrowse.setBounds(450, yPos - 25 - 15, 80, 25);
        add(outputBrowse);

        mergeBtn = new JButton("Merge");
        mergeBtn.addActionListener(this::mergeAction);
        mergeBtn.setBounds((width - 80) / 2, yPos + 5, 80, 30);
        add(mergeBtn);
        inputPath.setText(sharedInformation.getInputfolder());
        outputPath.setText(sharedInformation.getOutputFolder());
        return this;
    }

    private void buttonEnabled(boolean b) {
        inputBrowse.setEnabled(b);
        outputBrowse.setEnabled(b);
        mergeBtn.setEnabled(b);
    }

    private void inputBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        NativeFolderChooser fileChooser = Util.getFolderChooser("Select input folder");
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            if (Files.isDirectory(Paths.get(fileChooser.getSelectedFile().toString()))) {
                String directory = fileChooser.getSelectedFile().toString();
                sharedInformation.setInputfolder(directory);
                inputPath.setText(directory);
            } else {
                // folder not exits
                Util.showMessageDialog("Please select a valid folder");
            }
        }
        buttonEnabled(true);
    }


    private void outputBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        NativeFolderChooser fileChooser = Util.getFolderChooser("Select output folder");
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
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
        if (sharedInformation.getInputfolder() == null) {
            Util.showMessageDialog("Please select input folder");
            return;
        }
        if (sharedInformation.getOutputFolder() == null) {
            Util.showMessageDialog("Please select output folder");
            return;
        }
        buttonEnabled(false);
        List<String> files = Util.getFiles(CSV);
        new FileSelectionPanel(sharedInformation, files, CSV);
        buttonEnabled(true);
    }
}
