package com.company.examples.synchronization.utilities;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    public static void main(String[] args) {
        final int ROWS = 100000;
        final int COLS = 1000;
        final int SEARCH = 5;
        final int PARTICIPANTS = 5;
        final int LINES_PARTICIPANT = 2000;

        MatrixMock mock = new MatrixMock(ROWS, COLS, SEARCH);
        Results results = new Results(ROWS);
        Grouper grouper = new Grouper(results);

        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS, grouper);
        Searcher searchers[] = new Searcher[PARTICIPANTS];
        for (int i = 0; i < PARTICIPANTS; i++) {
            int from = i * LINES_PARTICIPANT;
            int to = from + LINES_PARTICIPANT;
            searchers[i] = new Searcher(from, to, mock, results, barrier, SEARCH);
            new Thread(searchers[i]).start();
        }
    }
}


class MatrixMock {
    private int[][] data;
    public MatrixMock(int rows, int cols, int number) {
        Random random = new Random();
        data = new int[rows][cols];
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int generated = (int) (random.nextDouble() * 10);
                data[i][j] = generated;
                if (generated == number) {
                    counter++;
                }
            }
        }

        System.out.printf("Mock: There are %d ocurrences of number in generated data.\n", counter, number);
    }

    public int[] getRow(int row) {
        if (row >= 0 && row < data.length) {
            return data[row];
        }
        return null;
    }
}

class Results {
    private int[] data;

    public Results(int size) {
        data = new int[size];
    }

    public int[] getData() {
        return data;
    }

    public void setData(int pos, int val) {
        data[pos] = val;
    }
}

class Searcher implements Runnable {

    private final int first;
    private final int last;
    private final MatrixMock mock;
    private final Results results;
    private final CyclicBarrier cyclicBarrier;
    private final int numberToSearch;

    public Searcher(int first, int last, MatrixMock matrixMock, Results results,
                    CyclicBarrier cyclicBarrier, int numberToSearch) {
        this.first = first;
        this.last = last;
        this.mock = matrixMock;
        this.results = results;
        this.cyclicBarrier = cyclicBarrier;
        this.numberToSearch = numberToSearch;
    }

    @Override
    public void run() {
        System.out.printf("%s: Processing lines from %d to %d.\n", Thread.currentThread().getName(), first, last);
        int counter;
        for (int i = first; i < last; i++) {
            int row[] = mock.getRow(i);
            counter = 0;
            for (int j = 0; j < row.length; j++) {
                if (row[j] == numberToSearch) {
                    counter++;
                }
            }
            results.setData(i, counter);
        }

        System.out.printf("%s: Lines processed.\n", Thread.currentThread().getName());
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        //after groupper
        System.out.printf("%s: Finished.\n", Thread.currentThread().getName());
    }
}

class Grouper implements Runnable {
    Results results;

    public Grouper(Results results) {
        this.results = results;
    }

    @Override
    public void run() {
        int finalResult = 0;
        System.out.printf("Grouper: Processing results...\n");

        int data[] = results.getData();
        for (int number : data) {
            finalResult += number;
        }

        System.out.printf("Grouper: Total result: %d.\n", finalResult);
    }
}