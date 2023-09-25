package cc.ding.bankweb.exception;

/**
 * InvalidDepositException
 * 存款异常类
 *
 * @author dingshuai
 * @version 1.8
 */
public class InvalidWithdrawException extends Exception {
    public InvalidWithdrawException() {
    }

    public InvalidWithdrawException(String message) {
        super(message);
    }
}
