package com.cx.bank.dao;


import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDaoImpl implements BankDaoInterface {
    MoneyBean moneyBean;
    UserBean userBean;

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public BankDaoImpl(UserBean userBean, MoneyBean moneyBean) {
        this.userBean = userBean;
        this.moneyBean = moneyBean;
    }

    @Override //存储方法实现
    public void saveMoney() {
        try {
            conn = DBUtil.getConnection();
            // 更新数据库余额
            String sql = "update t_user set balance=" + moneyBean.getBalance() + " where username='" + userBean.getUserName() + "'";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    @Override //添加用户方法实现
    public void insertUser() {
    }

    @Override //更新方法说明实现
    public void updateMoney(String username, double inMoney) {
        try {
            conn = DBUtil.getConnection();
            // 查询username对应的余额
            String sql = "select balance from t_user where username='" + username + "'";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println(balance);
                double updated = balance + inMoney;
                System.out.println(updated);
                // 更新存入账户数据库余额
                String sql2 = "update t_user set balance=" + updated + "where username='" + username + "'";
                ps = conn.prepareStatement(sql2);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        // 更改当前账户余额
        moneyBean.setBalance(moneyBean.getBalance() - inMoney);
    }

    @Override //按名字查找用户实现
    public void findByName() {
    }

    @Override //按用户名和密码查找用户实现
    public void findUser() {
    }
}