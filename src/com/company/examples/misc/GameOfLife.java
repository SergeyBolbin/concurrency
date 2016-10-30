package com.company.examples.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameOfLife {
    private int size;
    private int[][] board;

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife(10);
        game.printBoard();
        game.play();
    }

    public GameOfLife(int size) {
        this.size = size;
        Random random = new Random(size);
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = random.nextBoolean() ? 1 : 0;
            }    
        }
    }

    public void play() {
        boolean doPlay;
        int i = 0;
        do {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doPlay = playNext();
            System.out.println("Step" + i + "------------------");
            printBoard();
            i++;
        } while (doPlay);
    }

    public boolean playNext() {
        int[][] previousBoard = cloneArray(board);
        boolean doPlayNext = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean alive = previousBoard[i][j] == 1;
                int neighboursCount = getNeighboursCount(i, j, previousBoard);

                if (!alive && neighboursCount == 3) {
                    board[i][j] = 1;
                    doPlayNext = true;
                }

                if (alive && (neighboursCount > 3 || neighboursCount < 2)) {
                    board[i][j] = 0;
                    doPlayNext = true;
                }
            }
        }

        return doPlayNext;
    }


    private static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private int getNeighboursCount(int row, int col, int[][] board) {
        List<Integer> result = new ArrayList<>();
        int upperEdge = size - 1;

        if (row > 0)
            result.add(board[row - 1][col]);

        if (row < upperEdge)
            result.add(board[row + 1][col]);

        if (col > 0)
            result.add(board[row][col - 1]);

        if (col < upperEdge)
            result.add(board[row][col + 1]);

        if (row > 0 && col > 0)
            result.add(board[row - 1][col - 1]);

        if (row < upperEdge && col > 0)
            result.add(board[row + 1][col - 1]);

        if (row < upperEdge && col < upperEdge)
            result.add(board[row + 1][col + 1]);

        if (row > 0 && col < upperEdge)
            result.add(board[row - 1][col + 1]);

        return result.stream().mapToInt(Integer::intValue).sum();
    }

    public void printBoard() {
        for (int[] arr : board) {
            for (int elem : arr) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }
}
