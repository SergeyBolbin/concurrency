package com.company.examples.synchronization.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerExample {
    public static void main(String[] args) {
        Exchanger<List<String>> exchanger = new Exchanger<>();
        Producer p = new Producer(new ArrayList<>(), exchanger);
        Consumer c = new Consumer(new ArrayList<>(), exchanger);

        new Thread(p).start();
        new Thread(c).start();
    }
}


class Producer implements Runnable {
    private List<String> buffer;
    private Exchanger<List<String>> exchanger;

    public Producer(List<String> buffer,
                    Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
        for (int i = 0; i < 10; i++) {
            System.out.printf("Producer: Cycle %d\n", cycle);
            for (int j = 0; j < 10; j++) {
                String message = "Event " + ((i * 10) + j);
                System.out.printf("Producer: %s\n", message);
                buffer.add(message);
            }

            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cycle ++;
        }

        System.out.println("Producer buffer size: " + buffer.size());
    }
}

class Consumer implements Runnable {

    private List<String> buffer;
    private Exchanger<List<String>> exchanger;

    public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
        for (int i = 0; i < 10; i++) {
            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Consumer: Cycle %d, buffer size: %d\n", cycle, buffer.size());
            for (int j = 0; j < 10; j++) {
                String message = buffer.get(0);
                System.out.printf("Consumed: %s\n", message);
                buffer.remove(0);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cycle ++;
        }

        System.out.println("Exchanger buffer size: " + buffer.size());
    }
}
