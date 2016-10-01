package com.company.examples.threadmanagement;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Interruption {

    public static void main(String[] args) {
        Thread t = new Thread(new FileSearch("C:\\", "deploy.jar"));
        t.start();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();
    }
}

class FileSearch implements Runnable {

    private String fileName;
    private String directoryName;

    public FileSearch(String directoryName, String fileName) {
        this.fileName = fileName;
        this.directoryName = directoryName;
    }

    @Override
    public void run() {
        File initialFile = new File(directoryName);
        if(initialFile.isDirectory()) {
            try {
                processDir(initialFile);
            } catch (InterruptedException iex) {
                System.out.printf("%s: Thread has been interrupted", Thread.currentThread().getId());
            }
        } else {
            System.err.println("Not a directory!");
        }
    }

    private void processDir(File directory) throws InterruptedException {
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    processDir(file);
                } else {
                    processFile(file);
                }
            }
        }

        if (Thread.interrupted()) {
            throw new InterruptedException("Thread is interrupted");
        }

    }

    private void processFile(File file) throws InterruptedException {
        if (file.getName().contains(fileName)) {
            System.out.printf("%s : %s\n",Thread.currentThread().getName() ,file.getAbsolutePath());
        }
        if (Thread.interrupted()) {
            throw new InterruptedException("Thread is interrupted");
        }
    }
}
