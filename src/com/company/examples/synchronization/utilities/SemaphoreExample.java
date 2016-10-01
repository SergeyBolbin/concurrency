package com.company.examples.synchronization.utilities;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {

    public static void main(String[] args) {
        int semaphoreSize = 2;
        int threadCount = 50;
        PrintQueue pq = new PrintQueue(semaphoreSize);
        for(int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() ->  pq.doPrint() );
            thread.start();
        }
    }
}


class PrintQueue {
    private Semaphore semaphore;

    public PrintQueue(int size) {
        this.semaphore = new Semaphore(size);
    }

    public void doPrint() {
        try {
            semaphore.acquire();
            System.out.printf("Performed by %s  at %s \n", Thread.currentThread().getId(), new Date());
            TimeUnit.MILLISECONDS.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
