package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.ui.fileSelection.FileSelectionPanel;
import com.cerebra.fileMerger.util.NativeFolderChooser;
import com.cerebra.fileMerger.util.SharedInformation;
import com.cerebra.fileMerger.util.Util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static com.cerebra.fileMerger.util.Constants.*;

public class ExcelSelectionPanel extends TemplateSelectionPanel {

    ExcelSelectionPanel(SharedInformation sharedInformation, int xPos, int yPos) {
        super(sharedInformation, xPos, yPos, "Excel File Merger");
    }

    @Override
    void inputBrowseAction(ActionEvent actionEvent) {
        buttonEnabled(false);
        JFileChooser fileChooser = Util.getBasicFileChooser("Select input files and folders");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files(*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(sharedInformation.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            ArrayList<File> selectedFiles = new ArrayList<>(Arrays.asList(fileChooser.getSelectedFiles()));
            Util.populateFiles(selectedFiles, inputPath, XLSX);
        }
        buttonEnabled(true);
    }

    @Override
    void outputBrowseAction(ActionEvent actionEvent) {
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

    @Override
    void mergeAction(ActionEvent actionEvent) {
        if (sharedInformation.getInputFiles() == null) {
            Util.showMessageDialog("Please select input folder");
            return;
        }
        if (sharedInformation.getOutputFolder() == null) {
            Util.showMessageDialog("Please select output folder");
            return;
        }
        buttonEnabled(false);
        new FileSelectionPanel(sharedInformation, XLSX);
        buttonEnabled(true);
    }
}
