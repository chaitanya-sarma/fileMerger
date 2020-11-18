package com.cerebra.fileMerger.util;

import java.util.ArrayList;
import java.util.List;

public class FileDetails {
    String fileName;
    String filePath;
    int noOfLines;
    List<String> headers;

    public FileDetails() {
        this.headers = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getNoOfLines() {
        return noOfLines;
    }

    public void setNoOfLines(int noOfLines) {
        this.noOfLines = noOfLines;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers.clear();
        this.headers.addAll(headers);
    }
}
