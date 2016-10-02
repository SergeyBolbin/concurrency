package com.company.examples.executors;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledPoolExample {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new ScheduledTask(), 1L, 1L, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}


class ScheduledTask implements Runnable {
    @Override
    public void run() {
        System.out.printf("Starting at : %s \n", new Date());
    }
}