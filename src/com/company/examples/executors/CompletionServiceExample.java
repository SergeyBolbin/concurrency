package com.company.examples.executors;

import java.util.concurrent.*;

public class CompletionServiceExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<String> service = new ExecutorCompletionService<>(executor);

        ReportRequest faceRequest = new ReportRequest("Face", service);
        ReportRequest onlineRequest = new ReportRequest("Online", service);
        ReportProcessor processor = new ReportProcessor(service);
        Thread processorThread = new Thread(processor);
        Thread faceThread = new Thread(faceRequest);
        Thread onlineThread = new Thread(onlineRequest);

        faceThread.start();
        onlineThread.start();
        processorThread.start();

        try {
            faceThread.join();
            onlineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        processor.complete();
    }
}


class ReportGenerator implements Callable<String> {

    private String sender;
    private String title;

    public ReportGenerator(String sender, String title) {
        this.sender = sender;
        this.title = title;
    }

    @Override
    public String call() throws Exception {
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s_%s Generating a report during %d seconds \n", sender, title, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException iex) {
            iex.printStackTrace();
        }

        return String.format("[SENDER] %s. [TITLE] %s", sender, title);
    }
}

class ReportRequest implements Runnable {

    private final CompletionService<String> service;
    private final String name;

    public ReportRequest(String name, CompletionService<String> service) {
        this.name = name;
        this.service = service;
    }

    @Override
    public void run() {
        ReportGenerator generator = new ReportGenerator(name, "Request");
        service.submit(generator);
    }
}

class ReportProcessor implements Runnable {

    private CompletionService<String> completionService;
    private boolean done = false;

    public ReportProcessor(CompletionService<String> completionService) {
        this.completionService = completionService;
    }

    public void complete() {
        this.done = true;
    }

    @Override
    public void run() {
        while (!done) {
            try {
                Future<String> result = completionService.poll(20, TimeUnit.SECONDS);
                if (result != null) {
                    String resultReport = result.get();
                    System.out.printf("[PROCESSOR] Report received: %s \n", resultReport);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}