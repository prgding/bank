package com.cx.bank.manager.impl;

import com.cx.bank.dao.FileDaoInterface;
import com.cx.bank.factory.UserDaoFactory;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;
import com.cx.bank.util.MD5Utils;

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
 * @version 1.8
 */

public class ManagerImpl implements ManagerInterface {

    private static ManagerImpl instance;
    private MoneyBean moneyBean;
    private UserBean userBean;
    private FileDaoInterface dao;

    public ManagerImpl() {
    }

    public ManagerImpl(MoneyBean moneyBean, UserBean userBean) {
        this.dao = UserDaoFactory.getInstance().getDao();
        this.moneyBean = moneyBean;
        this.userBean = userBean;
    }

    public static synchronized ManagerInterface getInstance() {
        if (instance == null) {
            instance = new ManagerImpl(new MoneyBean(), new UserBean());
        }
        return instance;
    }

    @Override
    public synchronized void deposit(BigDecimal amount) throws InvalidDepositException, IOException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负\n");
        } else {
            BigDecimal balance = inquiry().add(amount);
            moneyBean.setBalance(balance);
            System.out.println("存款成功，余额：" + balance + "\n");
        }
        dao.saveMoney(moneyBean, userBean);
    }

    @Override
    public synchronized void withdrawals(BigDecimal amount) throws AccountOverDrawnException, IOException {
        BigDecimal balance = inquiry();
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款\n");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            System.out.println("取款成功，余额：" + balance + "\n");
        }
        dao.saveMoney(moneyBean, userBean);
    }

    @Override
    public BigDecimal inquiry() throws IOException {
        return dao.getMoney(userBean.getUsername());
    }

    @Override
    public void exitSystem() throws IOException {
        dao.saveMoney(moneyBean, userBean);
        System.out.println("系统已退出");
        System.exit(0);
    }

    @Override
    public void register() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.next();
        new File("./userInfo").mkdirs();
        boolean status = dao.findByName(username);
        if (status) {
            System.out.println("该用户名已存在\n");
        } else {
            System.out.println("请输入密码:");
            String password = MD5Utils.hash(scanner.next());
            dao.insertUser(username, password);
        }
    }

    @Override
    public ManagerInterface login() throws IOException {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("请输入用户名:");
        //String username = scanner.next();
        String username = "1";
        //System.out.println("请输入密码:");
        //String password = MD5Utils.hash(scanner.next());
        String password = "c4ca4238a0b923820dcc509a6f75849b";
        boolean status = dao.findUser(username, password);
        if (status) {
            System.out.println("登录成功\n");
            Properties props = new Properties();
            props.load(new FileInputStream("./userInfo/" + username + ".properties"));
            BigDecimal fileMoney = new BigDecimal(props.getProperty("money"));
            ManagerInterface instance = ManagerImpl.getInstance();
            instance.getMoneyBean().setBalance(fileMoney);
            instance.getUserBean().setUsername(username);
            instance.getUserBean().setPassword(password);
            return instance;
        } else {
            System.out.println("登录失败，检查你的用户名和密码\n");
            return null;
        }
    }

    public void transfer(String toName, BigDecimal transMoney) throws IOException, AccountOverDrawnException {
        Properties props = new Properties();
        props.load(new FileInputStream("./userInfo/" + userBean.getUsername() + ".properties"));
        BigDecimal fromMoney = new BigDecimal(props.getProperty("money")).subtract(transMoney);
        if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountOverDrawnException("余额不足，无法转账\n");
        } else {
            moneyBean.setBalance(fromMoney);
            dao.updateMoney(userBean.getUsername(), fromMoney);
            props.load(new FileInputStream("./userInfo/" + toName + ".properties"));
            BigDecimal toMoney = new BigDecimal(props.getProperty("money")).add(transMoney);
            dao.updateMoney(toName, toMoney);
            System.out.println("转账成功，自己账户余额 = " + fromMoney + "\n");
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
