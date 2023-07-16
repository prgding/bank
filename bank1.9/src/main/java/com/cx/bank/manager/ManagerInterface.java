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
 * @version 1.8
 */
public interface ManagerInterface {
    void deposit(BigDecimal amount) throws InvalidDepositException, IOException;

    void withdrawals(BigDecimal amount) throws AccountOverDrawnException, IOException;

    BigDecimal inquiry(String username);

    void exitSystem() ;

    String register(String username, String password) ;

    ManagerInterface login(String username, String password) ;

    MoneyBean getMoneyBean();

    void setMoneyBean(MoneyBean moneyBean);

    UserBean getUserBean();

    void setUserBean(UserBean userBean);

    void transfer(String toName, BigDecimal transMoney) throws IOException, AccountOverDrawnException;
}
