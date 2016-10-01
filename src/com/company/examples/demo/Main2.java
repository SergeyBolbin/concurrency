package com.company.examples.demo;


public class Main2 {
    public static void main(String[] args) {
//     for(int i=0; i<100; i++) {
//         Thread t = new Thread(() -> {
//             for (int j=1; j <=100; j ++){
//                 Thread current = Thread.currentThread();
//                 System.out.printf("%s: %s \n", current.getName(), current.getState().name());
//                 try {
//                     Thread.sleep(100);
//                 } catch (InterruptedException e) {
//                     e.printStackTrace();
//                 }
//             }
//         });
//
//         t.start();
//     }

        PrimeGenerator task = new PrimeGenerator();
        task.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.interrupt();

    }

    static class PrimeGenerator extends Thread {

        @Override
        public void run() {
            long number = 1L;
            while (true) {
                if (isPrime(number)) {
                    System.out.println("Is prime number " + number);
                }

                if (isInterrupted()) {
                    System.out.printf("The Prime Generator has been Interrupted");
                    return;
                }

                number++;
            }
        }

        private boolean isPrime(long number) {
            if (number <= 2) {
                return true;
            }
            for (long i = 2; i < number; i++) {
                if ((number % i) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

}
