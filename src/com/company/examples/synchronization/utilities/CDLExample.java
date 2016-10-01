package com.company.examples.synchronization.utilities;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CDLExample {

    public static void main(String[] args) {
        VideoConference conference = new VideoConference(10);
        new Thread(conference).start();

        for(int i = 0; i < 10; i++) {
            Participant participant = new Participant(conference, "John Doe " + translateToRomanic(i + 1));
            Thread t = new Thread(participant);
            t.start();
        }
    }

    static String translateToRomanic(int i) {
        switch (i) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
            default: return "The Great";
        }
    }
}


class VideoConference implements  Runnable {
    private final CountDownLatch controller;

    public VideoConference(int number) {
        controller = new CountDownLatch(number); // the number of times {@link #countDown} must be invoked
                                                 // before threads can pass through {@link #await}
    }

    @Override
    public void run() {
        System.out.printf("Waiting for %d participants. \n", controller.getCount());
        try {
            controller.await();
            System.out.printf("Video Conference: all participants have come. \n");
            System.out.printf("Let's rock! \n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void arrive(String participantName) {
        System.out.printf("%s arrived. ", participantName);
        controller.countDown();
        long count = controller.getCount();
        if(count > 0) {
            System.out.printf("Waiting for %d participants. \n", count);
        }
        System.out.print('\n');
    }
}

class Participant implements Runnable {
    private VideoConference conference;
    private String name;

    public Participant(VideoConference conference, String name) {
        this.conference = conference;
        this.name = name;
    }

    @Override
    public void run() {
        long duration = (long) (Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        conference.arrive(name);
    }
}