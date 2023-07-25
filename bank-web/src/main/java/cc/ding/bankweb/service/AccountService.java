package cc.ding.bankweb.service;

import cc.ding.bankweb.exception.AccountOverDrawnException;
import cc.ding.bankweb.exception.InvalidDepositException;

import java.math.BigDecimal;

public interface AccountService {

    BigDecimal inquiry(String username);

    String withdrawals(String username, BigDecimal amount) throws AccountOverDrawnException;

    String deposit(String username, BigDecimal amount) throws InvalidDepositException;

    String transfer(String fromName, String toName, BigDecimal transMoney) throws AccountOverDrawnException;

}
