package com.cx.bank.dao;


import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * BankDao
 * 数据库操作接口
 *
 * @author dingshuai
 * @version 1.9
 */
public interface BankDao {
    void insertUser(String username, String password);

    void updateMoney(String username, BigDecimal money);

    BigDecimal getMoney(String username);

    boolean findByName(String username);

    ManagerInterface findUser(String username, String password);

    List<Account> findAll();

    Account findOne(String username);

    void updateFlag(String username, int flag);
}