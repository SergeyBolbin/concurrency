package com.company.examples.synchronization;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class WaitNotify {

    public static void main(String[] args) {
        EventStorage storage = new EventStorage();
        Thread thread1 = new Thread(new Producer(storage));
        Thread thread2 = new Thread(new Consumer(storage));

        thread1.start();
        thread2.start();
    }
}


class EventStorage {
    private int maxSize;
    private Queue<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void set() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        storage.offer(new Date());
        System.out.printf("Set executed: %d\n", storage.size());
        notifyAll();
    }

    public synchronized void get() {
        while (storage.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Get executed: %d, %s \n", storage.size(), storage.poll());
        notifyAll();
    }
}

class Producer implements Runnable {
    private EventStorage storage;

    public Producer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            storage.set();
        }
    }
}

class Consumer implements Runnable {
    private EventStorage storage;

    public Consumer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            storage.get();
        }
    }
}