package com.cx.bank.dao;

import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;

import java.io.IOException;

/**
 * BankDaoInterface
 * BankDaoInterface接口
 *
 * @author dingshuai
 * @version 1.4
 */
public interface BankDaoInterface {
    void saveMoney(MoneyBean moneyBean, UserBean userBean) throws IOException;

    void insertUser(String username,String password) throws IOException;

    void updateMoney() throws IOException;

    boolean findByName(String username) throws IOException;

    boolean findUser(String username,String password) throws IOException;
}
