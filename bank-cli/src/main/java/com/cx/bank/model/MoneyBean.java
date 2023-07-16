package com.cx.bank.model;


import com.cx.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyBean {
    private double balance;

    public MoneyBean() {
    }

    public MoneyBean(UserBean userBean) {
        // 读取余额
        // 连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            // 插入用户数据，注册账户
            String sql = "select balance from t_user where user_name='" + userBean.getUserName() + "'";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String balance = rs.getString("balance");
                this.setBalance(Double.parseDouble(balance));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

