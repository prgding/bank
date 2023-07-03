package com.cx.bank.manager;

import java.math.BigDecimal;

public interface ManagerInterface {
    public void deposit(BigDecimal amount);

    public void withdrawals(BigDecimal amount);

    public BigDecimal inquiry();

    public void exitSystem();
}
