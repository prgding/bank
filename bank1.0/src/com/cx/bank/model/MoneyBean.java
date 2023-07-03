package com.cx.bank.model;

import java.math.BigDecimal;
import java.util.Objects;

public class MoneyBean {
    private BigDecimal balance;

    public MoneyBean() {
    }

    public MoneyBean(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyBean moneyBean = (MoneyBean) o;
        return Objects.equals(balance, moneyBean.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "MoneyBean{" +
                "balance=" + balance +
                '}';
    }
}
