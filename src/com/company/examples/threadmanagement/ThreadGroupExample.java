package com.company.examples.threadmanagement;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ThreadGroupExample {

    public static void main(String[] args) {
        ThreadGroup threadGroup = new CustomThreadGroup("Searchers");
        Result result = new Result();
        SearchTask task = new SearchTask(result);

        for(int i=0; i < 3; i++) {
            Thread thread = new Thread(threadGroup, task);
            thread.start();
            try {
                TimeUnit.MILLISECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        waitFinish(2, threadGroup);
        threadGroup.interrupt();
        waitFinish(0, threadGroup);
        System.out.printf("The winner is: %s", result.getName());
    }

    private static void waitFinish(int count, ThreadGroup threadGroup) {
        while (threadGroup.activeCount() > count) {
            //threadGroup.list();
            System.out.printf("[CLEANER] Active count is %d\n", threadGroup.activeCount());
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class CustomThreadGroup extends ThreadGroup {

    public CustomThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("The thread %s has thrown an Exception\n",t.getId());
        e.printStackTrace(System.out);
        System.out.printf("Terminating the rest of the Threads\n");
        interrupt();
    }
}

class SearchTask implements Runnable {
    private Result result;

    public SearchTask(Result result) {
        this.result = result;
    }

    @Override
    public void run() {
        String name=Thread.currentThread().getName();
        System.out.printf("Thread %s: Start\n",name);
        try {
            doTask();
            result.setName(name);
        } catch (InterruptedException e) {
            System.out.printf("Thread %s: Interrupted\n",name);
            return;
        }
        System.out.printf("Thread %s: End\n",name);
    }

    private void doTask() throws InterruptedException {
        Random random=new Random((new Date()).getTime());
        int value= random.nextInt(10) + 1;

        System.out.printf("Thread %s: %d\n",Thread.currentThread().getName(),value);
        TimeUnit.SECONDS.sleep(value);
    }

}


class Result {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}