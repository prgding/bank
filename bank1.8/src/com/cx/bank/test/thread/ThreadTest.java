package com.cx.bank.test.thread;

/**
 * ThreadTest
 *
 * @author dingshuai
 * @version 1.6
 */
public class ThreadTest {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Deposit());
        Thread thread2 = new Thread(new Withdraw());
        thread1.start();
        thread2.start();
    }
}
