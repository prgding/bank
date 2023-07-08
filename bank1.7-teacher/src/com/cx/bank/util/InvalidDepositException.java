package com.cx.bank.util;

/**
 * 作用：存款为负数时抛出异常
 *
 * @author admin
 */
@SuppressWarnings("serial")
public class InvalidDepositException extends Exception {

    public InvalidDepositException() {
        super();
    }

    public InvalidDepositException(String msg) {
        super(msg);
    }
}

