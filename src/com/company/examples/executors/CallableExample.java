package com.company.examples.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CallableExample {

    public static void main(String args[]) {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        Random rand = new Random();

        List<Future<Long>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int number = rand.nextInt(30);
            FactorialCalculator job = new FactorialCalculator(number);
            Future<Long> future = service.submit(job);
            results.add(future);
        }


        while (service.getCompletedTaskCount() < service.getTaskCount()) {
            System.out.println("Await..." + (service.getTaskCount() - service.getCompletedTaskCount()));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();

        int i = 0;
        for (Future<Long> future : results) {
            try {
                Long futureResult = future.get();
                System.out.println("Task " + i + " completed. Result is " + futureResult);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            i++;
        }

    }

}


class FactorialCalculator implements Callable<Long> {

    private int n;

    public FactorialCalculator(int n) {
        this.n = n;
    }

    @Override
    public Long call() throws Exception {
        if(n < 0) {
            throw new IllegalArgumentException("Illegal n. N should be greater then 0");
        }

        if (n == 0 || n == 1) {
            return 1L;
        }

        long result = 1L;
        for (int i = 1; i < n; i++) {
            result *= i;
            TimeUnit.MILLISECONDS.sleep(10);
        }
        System.out.printf("Finished %s \n", Thread.currentThread().getName());
        return result;
    }
}