package com.cx.bank.manager;

import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.math.BigDecimal;

/**
 * ManagerInterface
 * 业务类接口
 *
 * @author dingshuai
 * @version 1.3
 */
public interface ManagerInterface {
    void deposit(BigDecimal amount) throws InvalidDepositException;

    void withdrawals(BigDecimal amount) throws AccountOverDrawnException;

    BigDecimal inquiry();

    void exitSystem();
}
