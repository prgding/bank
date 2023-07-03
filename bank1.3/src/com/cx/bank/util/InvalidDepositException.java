package com.cx.bank.util;

/**
 * InvalidDepositException
 * 存款异常类
 *
 * @author dingshuai
 * @version 1.3
 */
public class InvalidDepositException extends Exception {
    public InvalidDepositException() {
    }

    public InvalidDepositException(String message) {
        super(message);
    }
}
