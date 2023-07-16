package com.cx.bank.dao;


import com.cx.bank.manager.ManagerInterface;

import java.math.BigDecimal;

public interface BankDaoInterface {
    void insertUser(String username, String password);

    void updateMoney(String username, BigDecimal money);

    BigDecimal getMoney(String username);

    boolean findByName(String username);

    ManagerInterface findUser(String username, String password);
}