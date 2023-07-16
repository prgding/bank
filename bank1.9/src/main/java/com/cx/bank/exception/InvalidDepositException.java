package com.cx.bank.exception;

/**
 * InvalidDepositException
 * 存款异常类
 *
 * @author dingshuai
 * @version 1.9
 */
public class InvalidDepositException extends Exception {
    public InvalidDepositException() {
    }

    public InvalidDepositException(String message) {
        super(message);
    }
}
