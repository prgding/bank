package com.cx.bank.test.thread;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.util.AccountOverDrawnException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Withdraw
 *
 * @author dingshuai
 * @version 1.6
 */
public class Withdraw implements Runnable {
    @Override
    public void run() {
        ManagerInterface instance = ManagerImpl.getInstance();
        try {
            ManagerInterface loggedIn = instance.login();
            System.out.println("取款前：" + loggedIn.getMoneyBean().getBalance());
            loggedIn.withdrawals(new BigDecimal(1));
        } catch (IOException | AccountOverDrawnException e) {
            throw new RuntimeException(e);
        }
    }
}
