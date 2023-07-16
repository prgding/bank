package com.cx.bank.dao;

import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * FileDao
 * BankDaoInterface接口
 *
 * @author dingshuai
 * @version 1.9
 */
public interface FileDao {
    void saveMoney(MoneyBean moneyBean, UserBean userBean) throws IOException;
    void insertUser(String username,String password) throws IOException;
    void updateMoney(String username, BigDecimal money) throws IOException;
    BigDecimal getMoney(String username) throws IOException;
    boolean findByName(String username) throws IOException;
    boolean findUser(String username,String password) throws IOException;
}
