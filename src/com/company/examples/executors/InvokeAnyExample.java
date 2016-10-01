package com.company.examples.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvokeAnyExample {
    public static void main(String[] args) {
        int number = new Random().nextInt(100);
        System.out.println(number);
        ExecutorService pool = Executors.newFixedThreadPool(15);
        List<FactorialCalculator> tasks = new ArrayList<>();
        for(int i=0; i<10; i++) {
            tasks.add(new FactorialCalculator(number));
        }
        try {
            Long aLong = pool.invokeAny(tasks);
            System.out.printf("%d! = %d\n", number, aLong);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}

