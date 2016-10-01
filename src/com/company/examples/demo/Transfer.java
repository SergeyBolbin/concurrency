package com.company.examples.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Transfer implements Callable<Boolean> {
    private static final int WAIT_SEC = 1;

    private Account a1;
    private Account a2;
    private int amount;

    public Transfer(Account a1, Account a2, int amount) {
        this.a1 = a1;
        this.a2 = a2;
        this.amount = amount;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println("Execute operation (PID): " + Thread.currentThread().getId());
        boolean result = false;
        if(a1.getBalance() < amount && a1.lock.tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
            try {
                if(a2.lock.tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                    try {
                        a1.withdraw(amount);
                        a2.deposit(amount);
                        Thread.sleep(1000);
                        result = true;
                    } finally {
                        a2.lock.unlock();
                    }
                } else {
                    a1.incrementFailCount();
                }
            } finally {
                a1.lock.unlock();
            }
        }

        return result;
    }
}
