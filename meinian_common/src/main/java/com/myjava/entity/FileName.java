package com.myjava.entity;

public class FileName {

    private String fileName;
    private String fileNames;

    public FileName() {
    }

    public FileName(String fileName, String fileNames) {
        this.fileName = fileName;
        this.fileNames = fileNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }
}
