package com.company.examples.demo;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Thread t1 = new Thread(new Task());
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            int counter = 0;
            try {
                while (true) {
                    System.out.println(counter);
                    TimeUnit.SECONDS.sleep(1);
                    counter++;

                    if(Thread.interrupted()) {
                        throw new InterruptedException("e");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
}
