package com.cx.bank.util;

/**
 * 作用：取款超出余额时抛出异常
 *
 * @author admin
 */
@SuppressWarnings("serial")
public class AccountOverDrawnException extends Exception {

    public AccountOverDrawnException() {
        super();
    }

    public AccountOverDrawnException(String msg) {
        super(msg);
    }
}
