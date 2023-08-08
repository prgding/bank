package com.cx.bank.manager;

import java.math.BigDecimal;

public interface ManagerInterface {
    void deposit(BigDecimal amount);

    void withdrawals(BigDecimal amount);

    BigDecimal inquiry();

    void exitSystem();
}
