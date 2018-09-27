package com.company.examples.distributor;

import java.util.TimerTask;
import java.util.concurrent.*;

public class TaskWithTimeout {

    public static class TimeoutExecutor {


        private TimeoutExecutor() {
        }

        public void execute(Runnable runnable, long timeout, TimeUnit timeUnit)
                throws InterruptedException, ExecutionException, TimeoutException {

            Future<?> submit = CompletableFuture.runAsync(runnable);
            submit.get(timeout, timeUnit);
        }
    }

    public static class LongTimerTask extends TimerTask {

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        TimeoutExecutor taskExecutor = new TimeoutExecutor();
        taskExecutor.execute(() -> {
            while (true) {
                System.out.println("aaaa");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }
}
