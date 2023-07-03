package com.cx.bank.manager;

import com.cx.bank.model.MoneyBean;

import java.math.BigDecimal;

public class ManagerImpl {
    private MoneyBean moneyBean;

    public ManagerImpl(MoneyBean moneyBean) {
        this.moneyBean = moneyBean;
    }

    public void deposit(BigDecimal amount) {
        if (amount.signum() < 0) {
            System.out.println("存款不能为负");
        } else {
            BigDecimal balance = moneyBean.getBalance().add(amount);
            moneyBean.setBalance(balance);
            System.out.println("存款成功，余额：" + balance);
        }
    }

    public void withdrawals(BigDecimal amount) {
        BigDecimal balance = moneyBean.getBalance();

        if (balance.compareTo(amount) < 0) {
            System.out.println("余额不足，无法取款");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            System.out.println("取款成功，余额：" + balance);
        }
    }

    public BigDecimal inquiry() {
        return moneyBean.getBalance();
    }

    public void exitSystem() {
        System.out.println("系统已退出");
        System.exit(0);
    }
}
