package com.company.examples.demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    public final Lock lock;
    private int balance;
    private AtomicInteger failureCounter;

    public Account(int balance) {
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void incrementFailCount() {
        failureCounter.incrementAndGet();
    }

    public int getFailCount() {
        return failureCounter.get();
    }
}
