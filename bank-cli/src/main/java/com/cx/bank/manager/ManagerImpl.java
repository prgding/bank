package com.cx.bank.manager;


import com.cx.bank.dao.BankDaoImpl;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.test.TestBank;
import com.cx.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManagerImpl {
    BankDaoImpl bankDao;
    MoneyBean moneyBean;

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ManagerImpl() {
    }

    public ManagerImpl(UserBean userBean) {
        this.moneyBean = new MoneyBean(userBean);
        this.bankDao = new BankDaoImpl(userBean, moneyBean);
    }

    public void inquiry() {
        System.out.print("您的余额为：" + moneyBean.getBalance() + " 元");
    }

    public void withdraws() {
        System.out.print("输入取款金额：");
        Scanner s2 = new Scanner(System.in);

        double balance = moneyBean.getBalance();
        try {
            double withdrawMoney = s2.nextDouble();
            if (withdrawMoney > 0) {
                double leftBalance = balance - withdrawMoney;
                if (leftBalance < 0) {
                    System.out.println("取款失败（余额不足）");
                } else {
                    System.out.print("取款成功，");
                    moneyBean.setBalance(leftBalance);
                }
            } else {
                System.out.println("取款失败（取款金额不能为负）");
            }
        } catch (Exception e) {
            System.out.println("取款失败（请检查取款金额格式）");
        }

    }

    public void deposit() {
        System.out.print("输入存款金额：");
        Scanner s3 = new Scanner(System.in);
        double inMoney = s3.nextDouble();
        double balance = moneyBean.getBalance();
        if (inMoney < 0) {
            System.out.println("存款失败（存款不能为负）");
        } else {
            System.out.print("存款成功，");
            moneyBean.setBalance(balance + inMoney);
        }
    }

    public void transfer() {
        System.out.print("输入转账金额：");
        Scanner s = new Scanner(System.in);
        double inMoney = s.nextDouble();
        System.out.print("输入转入账户名：");
        String inAccount = s.next();

        if ((inMoney > moneyBean.getBalance())) {
            System.out.println("金额不足，转账失败");
        } else if (inMoney < 0) {
            System.out.println("金额不能为负，转账失败");
        } else {
            try {
                bankDao.updateMoney(inAccount, inMoney);
            } catch (Exception e) {
                System.out.println("转账失败(请检查转入账户名)");
            }
        }
    }

    public void exitSystem() {
        bankDao.saveMoney();
        System.out.print("已退出");
        System.exit(0);
    }

    public void register() {

        System.out.print("输入用户名：");
        Scanner s2 = new Scanner(System.in);
        String userName = s2.next();

        System.out.print("输入密码：");
        Scanner s3 = new Scanner(System.in);
        String passWord = s3.next();

        // 连接数据库

        try {
            conn = DBUtil.getConnection();
            // 插入用户数据，注册账户
            String sql = "INSERT INTO t_user(username,password) VALUES ('" + userName + "','" + passWord + "')";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            System.out.println("注册成功");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

    }

    public void login() {
        String userName;
        String passWord;
        UserBean userBean = new UserBean();
        System.out.print("输入用户名：");
        Scanner s1 = new Scanner(System.in);
        userName = s1.next();
        userBean.setUserName(userName);

        System.out.print("输入密码：");
        Scanner s2 = new Scanner(System.in);
        passWord = s2.next();
        userBean.setPassword(passWord);

        // 连接数据库
        try {
            conn = DBUtil.getConnection();
            // 与数据库比对
            String sql = "select * from t_user where username = '" + userName + "' and password = '" + passWord + "'";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("密码正确，进入功能界面");
                TestBank testBank = new TestBank();
                testBank.functions(userBean);
            } else {
                System.out.println("用户不存在或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }
}
