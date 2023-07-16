package com.cx.bank.utils;

import java.sql.*;
import java.util.ResourceBundle;

// JDBC 的工具类
public class DBUtil {

    // 静态变量
    private static final ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
    private static final String driver = bundle.getString("driver");
    private static final String url = bundle.getString("url");
    private static final String user = bundle.getString("user");
    private static final String password = bundle.getString("password");

    static {
        // 注册驱动
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 获取数据库连接对象
    public static Connection getConnection() throws SQLException {
        // 获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    // 释放资源
    public static void close(Connection conn, Statement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
