package com.company.examples.threadmanagement;

import java.util.concurrent.TimeUnit;

public class Joining {
    static Thread dataSourceLoadTask;
    static Thread networkLoadTask;

    public static void main(String[] args) {
        dataSourceLoadTask = new Thread(new DataSourceLoader());
        networkLoadTask = new Thread(new NetworkLoader());

        networkLoadTask.start();
        dataSourceLoadTask.start();


        try {
            dataSourceLoadTask.join();
            networkLoadTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All done");
    }


    static class DataSourceLoader implements Runnable {

        @Override
        public void run() {
            System.out.println("Data source loader started");
            try {
                //networkLoadTask.join(); //hello, deadlock!
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Data source loader finished");
        }
    }

    static class NetworkLoader implements Runnable {

        @Override
        public void run() {
            try {
                dataSourceLoadTask.join(10000);
                System.out.println("Network loader started");
                TimeUnit.SECONDS.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Network  loader finished");
        }
    }
}


