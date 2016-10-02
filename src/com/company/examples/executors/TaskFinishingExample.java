package com.company.examples.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskFinishingExample {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        List<ResultTask> resultTasks = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            ResultTask task = new ResultTask(new ExecutableTask("task " + i));
            resultTasks.add(task);
            service.submit(task);
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(ResultTask task: resultTasks) {
            task.cancel(true);
        }

        service.shutdown();
    }
}


class ExecutableTask implements Callable<String> {

    private String name;

    public ExecutableTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String call() throws Exception {
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ignored) {
        }
        return "Hello, world. I'm " + name;
    }
}

class ResultTask extends FutureTask<String> {

    private String name;

    public ResultTask(ExecutableTask callable) {
        super(callable);
        name = callable.getName();
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            System.out.printf("%s: Has been canceled\n", name);
        } else {
            System.out.printf("%s: Has finished\n", name);
        }
    }
}
