package com.cerebra.fileMerger.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component("sharedInformation")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SharedInformation {
    private JFrame mainFrame;
    private String OS;
    List<File> inputFiles = new ArrayList<>();
    String outputFolder;
    //="C:\\Users\\csarma\\Downloads";
    static int XSize = 800, YSize = 450;
    private String workingDirectory = System.getProperty("user.dir");
    private Logger logger;

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public int getXSize() {
        return XSize;
    }

    public void setXSize(int XSize) {
        this.XSize = XSize;
    }

    public int getYSize() {
        return YSize;
    }

    public void setYSize(int ySize) {
        this.YSize = ySize;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<File> inputFiles) {
        this.inputFiles = new ArrayList<>();
        this.inputFiles = inputFiles;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }
}
