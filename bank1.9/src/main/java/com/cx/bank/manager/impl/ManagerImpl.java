package com.cx.bank.manager.impl;

import com.cx.bank.dao.BankDaoInterface;
import com.cx.bank.factory.UserDaoFactory;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * ManagerImpl
 * 实现ManagerInterface接口
 *
 * @author dingshuai
 * @version 1.8
 */

public class ManagerImpl implements ManagerInterface {

    private static ManagerInterface instance;
    private MoneyBean moneyBean;
    private UserBean userBean;
    private BankDaoInterface dao;


    public ManagerImpl() {
        this.dao = UserDaoFactory.getInstance().getDao();
    }

    public ManagerImpl(MoneyBean moneyBean, UserBean userBean) {
        this.moneyBean = moneyBean;
        this.userBean = userBean;
    }

    public static synchronized ManagerInterface getInstance() {
        if (instance == null) {
            instance = new ManagerImpl();
        }
        return instance;
    }

    @Override
    public synchronized void deposit(BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负\n");
        } else {
            BigDecimal balance = inquiry(userBean.getUsername()).add(amount);
            moneyBean.setBalance(balance);
            System.out.println("存款成功，余额：" + balance + "\n");
        }
        dao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
    }

    @Override
    public synchronized void withdrawals(BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = inquiry(userBean.getUsername());
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款\n");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            System.out.println("取款成功，余额：" + balance + "\n");
        }
        dao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
    }

    @Override
    public BigDecimal inquiry(String username) {
        return dao.getMoney(username);
    }

    @Override
    public void exitSystem() {
        if (moneyBean != null && userBean != null) {
            dao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
        }
        System.out.println("系统已退出");
        System.exit(0);
    }

    @Override
    public String register(String username, String password) {
        boolean byName = dao.findByName(username);
        if (byName) {
            return "该用户名已存在\n";
        } else {
            dao.insertUser(username, password);
        }
        return "注册成功";
    }

    @Override
    public ManagerInterface login(String username, String password) {
        instance = dao.findUser(username, password);
        if (instance != null) {
            System.out.println("登录成功\n");
            BigDecimal fileMoney = instance.inquiry(username);
            moneyBean = new MoneyBean();
            moneyBean.setBalance(fileMoney);
            userBean = new UserBean();
            userBean.setUsername(username);
            userBean.setPassword(password);
            return instance;
        } else {
            System.out.println("登录失败，检查你的用户名和密码\n");
            return null;
        }
    }

    public void transfer(String toName, BigDecimal transMoney) throws IOException, AccountOverDrawnException {
        boolean exist = dao.findByName(toName);
        if (!exist) {
            throw new IOException("对方账户不存在");
        } else {
            BigDecimal fromMoney = inquiry(userBean.getUsername()).subtract(transMoney);
            if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountOverDrawnException("余额不足，无法转账\n");
            } else {
                moneyBean.setBalance(fromMoney);
                dao.updateMoney(userBean.getUsername(), fromMoney);
                BigDecimal toMoney = inquiry(toName).add(transMoney);
                dao.updateMoney(toName, toMoney);
                System.out.println("转账成功，自己账户余额 = " + fromMoney + "\n");
            }
        }
    }

    public MoneyBean getMoneyBean() {
        return moneyBean;
    }

    public void setMoneyBean(MoneyBean moneyBean) {
        this.moneyBean = moneyBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Override
    public String toString() {
        return "ManagerImpl{" +
                "moneyBean=" + moneyBean +
                ", userBean=" + userBean +
                ", dao=" + dao +
                '}';
    }
}
