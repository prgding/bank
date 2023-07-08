package com.cx.bank.test.thread;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Deposit
 *
 * @author dingshuai
 * @version 1.6
 */
public class Deposit implements Runnable {
    @Override
    public void run() {
        ManagerInterface instance = ManagerImpl.getInstance();
        try {
            ManagerInterface loggedIn = instance.login();
            System.out.println("存款前：" + loggedIn.getMoneyBean().getBalance());
            loggedIn.deposit(new BigDecimal(1));
        } catch (IOException | InvalidDepositException e) {
            throw new RuntimeException(e);
        }
    }
}
