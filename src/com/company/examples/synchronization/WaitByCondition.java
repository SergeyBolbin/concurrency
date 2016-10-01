package com.company.examples.synchronization;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitByCondition {
    public static void main(String[] args) {
        FileMock mock = new FileMock(100, 10);
        Buffer buffer = new Buffer(50);

        Thread producerTask = new Thread(new BufferProducer(mock, buffer));
        producerTask.start();

        for(int i = 0; i < 3; i++) {
            Thread consumerTask = new Thread(new BufferConsumer(buffer));
            consumerTask.start();
        }
    }
}

class BufferProducer implements Runnable {

    private FileMock mock;
    private Buffer buffer;

    public BufferProducer(FileMock mock, Buffer buffer) {
        this.buffer = buffer;
        this.mock = mock;
    }

    @Override
    public void run() {
        while (mock.hasMoreLines()) {
            String line = mock.getLine();
            System.out.printf("[PRODUCER] Produce new line=%s. Buffer size = %d \n", line, buffer.getBufferSize());
            buffer.insert(line);

        }

        System.out.println("[PRODUCER] is finished");
        buffer.setPendingLines(false);
    }
}

class BufferConsumer implements Runnable {

    private Buffer buffer;

    public BufferConsumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.hasPendingLines()) {
            System.out.printf("[CONSUMER] Consumer with id=%s process line=%s. Buffer size=%d \n",
                    Thread.currentThread().getId(), buffer.get(), buffer.getBufferSize());
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class FileMock {
    private String[] content;
    private int index;

    public FileMock(int size, int length) {
        content = new String[size];
        for (int i = 0; i < size; i++) {
            StringBuilder buffer = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                int index = (int) (Math.random() * 10);
                buffer.append(index);
            }
            content[i] = buffer.toString();
        }
    }

    public boolean hasMoreLines() {
        return index < content.length;
    }

    public String getLine() {
        String result = null;
        if (hasMoreLines()) {
            result = content[index];
            index++;
        }
        return result;
    }
}

class Buffer {

    private Queue<String> bufferQueue = new LinkedList<>();
    private int maxSize;
    private ReentrantLock lock = new ReentrantLock();
    private Condition isEmptyCondition = lock.newCondition();
    private Condition isFullCondition = lock.newCondition();
    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        this.pendingLines = true;
    }

    public int getBufferSize() {
        return  bufferQueue.size();
    }

    public boolean hasPendingLines() {
        return pendingLines || bufferQueue.size() > 0;
    }

    public void setPendingLines(boolean val) {
        pendingLines = val;
    }

    public void insert(String value) {
        lock.lock();
        try {
            while (bufferQueue.size() >= maxSize) {
                isFullCondition.await();
            }
            bufferQueue.offer(value);
            isEmptyCondition.signalAll();

        } catch (InterruptedException iex) {
            iex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        String result = null;
        lock.lock();
        try {
            while (bufferQueue.isEmpty() && pendingLines) {
                isEmptyCondition.await();
            }

            if(hasPendingLines()) {
                result = bufferQueue.poll();
                isFullCondition.signalAll();
            }
        } catch (InterruptedException iex) {
            iex.printStackTrace();
        } finally {
            lock.unlock();
        }

        return result;
    }

}