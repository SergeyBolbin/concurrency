package com.company.examples.distributor;

import java.util.*;
import java.util.function.Consumer;

public final class TaskDistributor {

    private TaskDistributor() {
    }

    public static <T> void execute(Iterator<T> tasks,
                                   Consumer<Iterable<T>> consumer,
                                   long periodInMillis,
                                   int batchSize) {

        long delay = periodInMillis / batchSize + 1;
        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        List<T> batch = new ArrayList<>();
                        if (!tasks.hasNext()) {
                            timer.cancel();
                            return;
                        }

                        while (batch.size() < batchSize) {
                            batch.add(tasks.next());
                        }

                        consumer.accept(batch);
                    }
                }, 0L, delay);


    }

    public static void main(String[] args) {
        List<Item> itemsList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itemsList.add(new Item("item" + i));
        }

        TaskDistributor.execute(itemsList.iterator(), list -> System.out.println("processing items: " + list), 10000, 10);
        TaskDistributor.execute(itemsList.iterator(), list -> System.out.println("processing items: " + list), 10, 100);
        TaskDistributor.execute(new ArrayList<>().iterator(), list -> System.out.println("processing items: " + list), 10, 100);
    }

    static class Item {
        private String name;

        Item(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}