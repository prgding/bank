package com.cx.bank.dao.impl;

import com.cx.bank.dao.BankDaoInterface;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * BankDaoImpl
 * 实现BankDaoInterface接口
 *
 * @author dingshuai
 * @version 1.4
 */
public class BankDaoImpl implements BankDaoInterface {
    public void saveMoney(MoneyBean moneyBean, UserBean userBean) throws IOException {
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
    public void insertUser(String username, String password) throws IOException {
        Properties props = new Properties();
        props.setProperty("username", username);
        props.setProperty("password", password);
        props.setProperty("money", BigDecimal.valueOf(10).toString());

        String msg = username + " 的用户信息";
        props.store(new FileOutputStream("./userInfo/" + username + ".properties"), msg);
        System.out.println("注册成功\n");
    }

    @Override
    public boolean findByName(String username) {
        File userInfo = new File("./userInfo/" + username + ".properties");
        return userInfo.exists();
    }

    @Override
    public boolean findUser(String username, String password) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("./userInfo/" + username + ".properties"));
        String fileName = props.getProperty("username");
        String filePwd = props.getProperty("password");
        return fileName.equals(username) && filePwd.equals(password);
    }
}
