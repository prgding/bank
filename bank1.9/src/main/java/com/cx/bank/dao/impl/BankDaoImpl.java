package com.cx.bank.dao.impl;


import com.cx.bank.dao.BankDaoInterface;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.DBUtil;

import java.math.BigDecimal;
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

    public BankDaoImpl() {
    }

    public BankDaoImpl(UserBean userBean, MoneyBean moneyBean) {
        this.userBean = userBean;
        this.moneyBean = moneyBean;
    }

    @Override
    public void insertUser(String username, String password) {
        try {
            conn = DBUtil.getConnection();
            String sql = "insert into t_user(username,password,balance) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setBigDecimal(3, new BigDecimal("10"));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    @Override
    public void updateMoney(String username, BigDecimal money) {
        try {
            conn = DBUtil.getConnection();
            String sql = "update t_user set balance = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1, money);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    @Override
    public BigDecimal getMoney(String username) {
        BigDecimal balance = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select balance from t_user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return balance;
    }

    @Override
    public boolean findByName(String username) {
        boolean flag = false;
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return flag;
    }

    @Override
    public ManagerInterface findUser(String username, String password) {
        ManagerInterface managerInterface = ManagerImpl.getInstance();
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user where username = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                managerInterface.setMoneyBean(new MoneyBean(rs.getBigDecimal("balance")));
                managerInterface.setUserBean(new UserBean(rs.getString("username"), rs.getString("password")));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return managerInterface;
    }
}