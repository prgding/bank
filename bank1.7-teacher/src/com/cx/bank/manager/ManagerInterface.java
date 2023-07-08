package com.cx.bank.manager;

import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;

/**
 * 业务类接口
 *
 * @author admin
 */
public interface ManagerInterface {

    /**
     * 注册
     *
     * @param 用户名
     * @param 密码
     * @return boolean
     */
    public boolean register(String name, String psd);

    /**
     * 登录方法的实现
     *
     * @param 用户名
     * @param 密码
     * @return boolean
     */
    public boolean login(String name, String psd);

    /**
     * 实现存款功能
     *
     * @param 存款金额
     * @return void
     * @throws InvalidDepositException,NumberFormatException
     */
    void deposit(String money) throws InvalidDepositException, NumberFormatException;

    /**
     * 实现取款功能
     *
     * @param 取款金额
     * @return void
     * @throws AccountOverDrawnException，NumberFormatException
     */
    void withdrawals(String money) throws AccountOverDrawnException, NumberFormatException;

    /**
     * 实现查看余额功能
     *
     * @return 余额
     */
    double inquiry();

    /**
     * 实现转账功能
     *
     * @param inAccount 转入账号
     * @param money     转账金额
     * @return boolean
     * @throws NumberFormatException，IOException
     */

    public boolean tranferMoney(String outAccount, String money) throws NumberFormatException, IOException;

    /**
     * 退出系统
     *
     * @param logname 退出账号（用户名）
     * @return void
     */
    public void exitSystem(String logname);
}
