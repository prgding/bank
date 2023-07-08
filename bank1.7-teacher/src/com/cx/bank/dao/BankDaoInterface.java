package com.cx.bank.dao;

import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;

import java.io.IOException;

/**
 * 持久层接口
 *
 * @author Administrator
 */
public interface BankDaoInterface {

    /**
     * 存储方法
     *
     * @param logname   登录帐号
     * @param MoneyBean
     * @return void
     * @throws Exception
     */
    void saveMoney(String logname, MoneyBean moneyBean) throws Exception;

    /**
     * 按名字查找用户
     *
     * @param name 用户名
     * @return boolean
     */
    boolean findByName(String name);


    /**
     * 按用户名和密码查找用户
     *
     * @param UserBean（用户名，密码）
     * @param MoneyBean（money）
     * @return boolean
     */
    boolean findUser(UserBean user, MoneyBean moneyBean);


    /**
     * 添加用户方法实现
     *
     * @param UserBean（用户名，密码）
     * @return void
     */
    void insertUser(UserBean user);


    /**
     * 更新余额
     *
     * @param MoneyBean(money)
     * @param inAccount        转入账号
     * @param double           dmoney 转账金额
     * @return void
     * @throws IOException
     */
    void updateMoney(MoneyBean moneyBean, String inAccount, double dmoney) throws IOException;
}
