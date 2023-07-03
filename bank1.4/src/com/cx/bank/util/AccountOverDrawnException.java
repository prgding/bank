package com.cx.bank.util;

/**
 * AccountOverDrawnException
 * 取款异常类
 *
 * @author dingshuai
 * @version 1.4
 */
public class AccountOverDrawnException extends Exception {
    public AccountOverDrawnException() {
    }

    public AccountOverDrawnException(String message) {
        super(message);
    }
}
