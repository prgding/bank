package cc.ding.bankweb.service;


import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.MoneyBean;
import cc.ding.bankweb.model.UserBean;
import cc.ding.bankweb.util.AccountOverDrawnException;
import cc.ding.bankweb.util.InvalidDepositException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BankService
 * 业务类接口
 *
 * @author dingshuai
 * @version 1.8
 */
public interface BankService {
    String deposit(BigDecimal amount) throws InvalidDepositException;

    String withdrawals(BigDecimal amount) throws AccountOverDrawnException;

    BigDecimal inquiry(String username);

    void exitSystem() throws IOException;

    boolean checkIfExists(String username);

    Account checkPwd(String username, String password);

    String register(String username, String password);

    BankService login(String username, String password);

    MoneyBean getMoneyBean();

    void setMoneyBean(MoneyBean moneyBean);

    UserBean getUserBean();

    void setUserBean(UserBean userBean);

    String transfer(String toName, BigDecimal transMoney) throws AccountOverDrawnException;
}
