package com.company.examples.synchronization.utilities;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserExample {

    public static void main(String[] args) {
        Phaser phaser = new MyPhaser();
        Student[] students = new Student[5];
        Thread[] threads = new Thread[5];

        for(int i=0; i< students.length; i++) {
            students[i] = new Student(phaser);
            threads[i] = new Thread(students[i]);
            phaser.register();
        }

        for(Thread th: threads) {
            th.start();
//            try {
//                th.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } --- DEADLOCK
        }
    }
}

class MyPhaser extends Phaser {

    private static final int PHASE_STUDENT_ARRIVED = 0;
    private static final int PHASE_FINISH_FIRST_EXCERCISE = 1;
    private static final int PHASE_FINISH_SECOND_EXCERCISE = 2;
    private static final int PHASE_FINISH_EXAM = 3;

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case PHASE_STUDENT_ARRIVED:
                return studentsArrived();
            case PHASE_FINISH_FIRST_EXCERCISE:
                return finishFirstExercise();
            case PHASE_FINISH_SECOND_EXCERCISE:
                return finishSecondExercise();
            case PHASE_FINISH_EXAM:
                return finishExam();
            default:
                return true;
        }
    }

    private boolean studentsArrived() {
        System.out.printf("Phaser: The exam are going to start. The students are ready.\n");
        System.out.printf("Phaser: We have %d students.\n", getRegisteredParties());
        return false;
    }

    private boolean finishFirstExercise() {
        System.out.printf("Phaser: All the students have finished the first exercise.\n");
        System.out.printf("Phaser: It's time for the second one.\n");
        return false;
    }

    private boolean finishSecondExercise() {
        System.out.printf("Phaser: All the students have finished the second exercise.\n");
        System.out.printf("Phaser: It's time for the third one.\n");
        return false;
    }

    private boolean finishExam() {
        System.out.printf("Phaser: All the students have finished the exam.\n");
        System.out.printf("Phaser: Thank you for your time.\n");
        return true;
    }
}

class Student implements Runnable {

    private Phaser phaser;

    public Student(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.printf("%s: Has arrived to do the exam. %s\n",Thread.currentThread().getName(),new Date());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: Going to do first exercise. %s\n", Thread.currentThread().getName(), new Date());
        doNextExercise();
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: Going to do 2nd exercise. %s\n", Thread.currentThread().getName(), new Date());
        doNextExercise();
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: Going to do 3rd exercise. %s\n", Thread.currentThread().getName(), new Date());
        doNextExercise();
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: finished exam. %s\n", Thread.currentThread().getName(), new Date());
    }

    private void doNextExercise() {
        try {
            long duration=(long)(Math.random()*10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}