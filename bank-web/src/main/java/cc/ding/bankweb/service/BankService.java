package cc.ding.bankweb.service;


import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.util.AccountOverDrawnException;
import cc.ding.bankweb.util.InvalidDepositException;

import java.math.BigDecimal;

/**
 * BankService
 * 业务类接口
 *
 * @author dingshuai
 * @version 1.8
 */
public interface BankService {
    boolean checkIfExists(String username);

    String register(String username, String password);

    Account checkPwd(String username, String password);

    BigDecimal inquiry(String username);

    String withdrawals(String username, BigDecimal amount) throws AccountOverDrawnException;

    String deposit(String username, BigDecimal amount) throws InvalidDepositException;

    String transfer(String fromName, String toName, BigDecimal transMoney) throws AccountOverDrawnException;
}
