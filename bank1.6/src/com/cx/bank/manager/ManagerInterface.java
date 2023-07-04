package com.cx.bank.manager;

import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * ManagerInterface
 * 业务类接口
 *
 * @author dingshuai
 * @version 1.6
 */
public interface ManagerInterface {
    void deposit(BigDecimal amount) throws InvalidDepositException;

    void withdrawals(BigDecimal amount) throws AccountOverDrawnException;

    BigDecimal inquiry();

    void exitSystem() throws IOException;

    void register() throws IOException;

    ManagerInterface login() throws IOException;

    MoneyBean getMoneyBean();

    void setMoneyBean(MoneyBean moneyBean);

    UserBean getUserBean();

    void setUserBean(UserBean userBean);

    void transfer(String toName, BigDecimal transMoney) throws IOException, AccountOverDrawnException;
}
