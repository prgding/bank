package com.cx.bank.exception;

/**
 * AccountOverDrawnException
 * 取款异常类
 *
 * @author dingshuai
 * @version 1.9
 */
public class AccountOverDrawnException extends Exception {
    public AccountOverDrawnException() {
    }

    public AccountOverDrawnException(String message) {
        super(message);
    }
}
