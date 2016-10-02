package com.company.examples.executors;

import java.util.concurrent.*;

public class RejectedHandlerExample {
    public static void main(String[] args) {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        service.setRejectedExecutionHandler(new CustomRejectedHandler()); //set rejection handler
        // if handler is not specified, RejectedExecutionException occurred
        for (int i = 0; i < 5; i++) {
            service.submit(new ExTask("Task " + i));
        }

        service.shutdown();

        for (int i = 5; i < 8; i++) {
            service.submit(new ExTask("RejectedTask " + i));
        }

    }
}


class CustomRejectedHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.printf("RejectedTaskController: The task %s has been rejected\n", r.toString());
        System.out.printf("RejectedTaskController: %s\n", executor.toString());
        System.out.printf("RejectedTaskController: Terminating: %s\n", executor.isTerminating());
        System.out.printf("RejectedTaksController: Terminated: %s\n", executor.isTerminated());
    }
}

class ExTask implements Runnable {

    private String name;

    public ExTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Task " + name + ": Starting");
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n", name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}