package com.company.examples.threadmanagement;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class Daemon {

    public static void main(String[] args) {
        Deque<Event> queue = new ArrayDeque<>();
        for(int i=0; i < 100; i++) {
            new EventWriterTask(queue).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new CleanerTask(queue).start();

    }
}


class EventWriterTask extends Thread {

    private final Deque<Event> deque;

    public EventWriterTask(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        for(int i=0; i < 100; i++) {
            Event event = new Event(String.format("The thread %s generated an event", Thread.currentThread().getId()));
            deque.addFirst(event);
            System.out.printf("[SENDER] Queue size: %d, Message is sent: %s\n", deque.size(), event);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

class CleanerTask extends Thread {
    private final Deque<Event> deque;

    public CleanerTask(Deque<Event> deque) {
        this.deque = deque;
        setDaemon(true);
    }

    @Override
    public void run() {
        while(true) {
            Date date = new Date();
            clean(date);
        }
    }

    private void clean(Date date) {
        if(deque.isEmpty()) {
            return;
        }

        long difference;
        do {
            Event event = deque.getLast();
            difference = date.getTime() - event.date.getTime();
            if(difference > 10000) {
                System.out.printf("[CLEANER] Queue size: %d, clean event %s\n: ", deque.size(), event);
                deque.removeLast();
            }
        } while (difference > 10000);

    }
}


class Event {
    final String description;
    final Date date;

    public Event(String description) {
        this.description = description;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return "Event{" +
                "description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}