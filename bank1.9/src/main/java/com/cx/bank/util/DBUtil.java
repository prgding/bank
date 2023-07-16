package com.cx.bank.util;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * DBUtil
 * 数据库工具类
 *
 * @author dingshuai
 * @version 1.9
 */
public class DBUtil {

    // 静态变量
    private static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

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
        return DriverManager.getConnection(url, user, password);
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