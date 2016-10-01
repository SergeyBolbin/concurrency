package com.company.examples.executors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeAllExample {
    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(15);
        List<FactorialCalculator> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int number = Math.abs(new Random().nextInt(10));
            System.out.println(number);
            tasks.add(new FactorialCalculator(number));
        }

        List<Future<Long>> futures = Collections.EMPTY_LIST;
        try {
            futures = pool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<Long> future : futures) {
            try {
                System.out.println("The result is: " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
    }
}