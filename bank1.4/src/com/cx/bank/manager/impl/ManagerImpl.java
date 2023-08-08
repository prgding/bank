package com.cx.bank.manager.impl;

import com.cx.bank.dao.BankDaoInterface;
import com.cx.bank.dao.impl.BankDaoImpl;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Scanner;

/**
 * ManagerImpl
 * 实现ManagerInterface接口
 *
 * @author dingshuai
 * @version 1.4
 */

public class ManagerImpl implements ManagerInterface {

    private static ManagerImpl instance;
    private MoneyBean moneyBean;
    private UserBean userBean;
    private final BankDaoInterface bankDaoInterface = new BankDaoImpl();

    public ManagerImpl() {

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
    public void deposit(BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负\n");
        } else {
            BigDecimal balance = moneyBean.getBalance().add(amount);
            moneyBean.setBalance(balance);
            System.out.println("存款成功，余额：" + balance + "\n");
        }
    }

    @Override
    public void withdrawals(BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = moneyBean.getBalance();
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款\n");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            System.out.println("取款成功，余额：" + balance + "\n");
        }
    }

    @Override
    public BigDecimal inquiry() {
        return moneyBean.getBalance();
    }

    @Override
    public void exitSystem() throws IOException {
        bankDaoInterface.saveMoney(moneyBean, userBean);
    }

    @Override
    public void register() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.next();
        new File("./userInfo").mkdirs();
        boolean status = bankDaoInterface.findByName(username);
        if (status) {
            System.out.println("该用户名已存在\n");
        } else {
            System.out.println("请输入密码:");
            String password = scanner.next();
            bankDaoInterface.insertUser(username, password);
        }
    }

    @Override
    public ManagerInterface login() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.next();
        System.out.println("请输入密码:");
        String password = scanner.next();
        boolean status = bankDaoInterface.findUser(username, password);
        if (status) {
            System.out.println("登录成功\n");
            Properties props = new Properties();
            props.load(new FileInputStream("./userInfo/" + username + ".properties"));
            BigDecimal fileMoney = new BigDecimal(props.getProperty("money"));
            instance.setMoneyBean(new MoneyBean(fileMoney));
            instance.setUserBean(new UserBean(username, password));
            return instance;
        } else {
            System.out.println("登录失败，检查你的用户名和密码\n");
            return null;
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
}
