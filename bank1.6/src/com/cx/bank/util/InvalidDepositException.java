package com.cx.bank.util;

/**
 * InvalidDepositException
 * 存款异常类
 *
 * @author dingshuai
 * @version 1.6
 */
public class InvalidDepositException extends Exception {
    public InvalidDepositException() {
    }

    public InvalidDepositException(String message) {
        super(message);
    }
}
