package com.cx.bank.manager.impl;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        if (moneyBean != null && userBean != null) {
            Properties props = new Properties();
            props.setProperty("username", userBean.getUsername());
            props.setProperty("password", userBean.getPassword());
            props.setProperty("money", moneyBean.getBalance().toString());
            String msg = userBean.getUsername() + " 的用户信息";
            props.store(new FileOutputStream("./userInfo/" + userBean.getUsername() + ".properties"), msg);
        }
        System.out.println("系统已退出");
        System.exit(0);
    }

    @Override
    public void register() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.next();
        new File("./userInfo").mkdirs();
        File userInfo = new File("./userInfo/" + username + ".properties");
        if (!userInfo.exists()) {
            System.out.println("请输入密码:");
            String password = scanner.next();

            Properties props = new Properties();
            props.setProperty("username", username);
            props.setProperty("password", password);
            props.setProperty("money", BigDecimal.valueOf(10).toString());

            String msg = username + " 的用户信息";
            props.store(new FileOutputStream("./userInfo/" + username + ".properties"), msg);
            System.out.println("注册成功\n");
        } else {
            System.out.println("该用户名已存在\n");
        }
    }

    @Override
    public ManagerInterface login() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.next();
        System.out.println("请输入密码:");
        String password = scanner.next();

        Properties props = new Properties();
        props.load(new FileInputStream("./userInfo/" + username + ".properties"));
        String fileName = props.getProperty("username");
        String filePwd = props.getProperty("password");
        BigDecimal fileMoney = new BigDecimal(props.getProperty("money"));

        if (fileName.equals(username) && filePwd.equals(password)) {
            System.out.println("登录成功\n");
            ManagerInterface instance = ManagerImpl.getInstance();
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
