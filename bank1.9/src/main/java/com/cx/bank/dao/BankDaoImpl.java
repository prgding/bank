package com.cx.bank.dao;


import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.Account;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.DBUtil;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BankDaoImpl
 * 实现BankDao接口
 *
 * @author dingshuai
 * @version 1.9
 */

@NoArgsConstructor
public class BankDaoImpl implements BankDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public void insertUser(String username, String password) {
        try {
            conn = DBUtil.getConnection();
            String sql = "insert into t_user(user_name,user_password,balance,user_flag) values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setBigDecimal(3, new BigDecimal("10"));
            ps.setInt(4, 1);
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
            String sql = "update t_user set balance = ? where user_name = ?";
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
            String sql = "select balance from t_user where user_name = ?";
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
            String sql = "select * from t_user where user_name = ?";
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
            String sql = "select * from t_user where user_name = ? and user_password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                managerInterface.setMoneyBean(new MoneyBean(rs.getBigDecimal("balance")));
                managerInterface.setUserBean(new UserBean(rs.getString("user_name"), rs.getString("user_password")));
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

    @Override
    public List<Account> findAll() {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = initAccount();
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return accounts;
    }

    private Account initAccount() throws SQLException {
        Account account = new Account();
        account.setUserId(rs.getInt("user_id"));
        account.setUsername(rs.getString("user_name"));
        account.setPassword(rs.getString("user_password"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUserFlag(rs.getInt("user_flag"));
        return account;
    }

    @Override
    public Account findOne(String username) {
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user where user_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return initAccount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public void updateFlag(String username, int flag) {
        try {
            conn = DBUtil.getConnection();
            String sql = "update t_user set user_flag = ? where user_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, flag);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }
}