package com.cx.bank.dao;

import com.cx.bank.model.Log;
import com.cx.bank.util.DBUtil;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class LogDaoImpl implements LogDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public void insert(String logType, BigDecimal logAmount, int userId) {
        try {
            conn = DBUtil.getConnection();
            String sql = "insert into t_log(log_type,log_amount,userid) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, logType);
            ps.setBigDecimal(2, logAmount);
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    @Override
    public Log findOne(int userId) {
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_log where userid = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Log log = new Log();
                log.setLogId(rs.getInt("log_id"));
                log.setLogType(rs.getString("log_type"));
                log.setLogAmount(rs.getBigDecimal("log_amount"));
                log.setUserId(rs.getInt("userid"));
                return log;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Log> findAll() {
        ArrayList<Log> logs = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_log";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Log log = new Log();
                log.setLogId(rs.getInt("log_id"));
                log.setLogType(rs.getString("log_type"));
                log.setLogAmount(rs.getBigDecimal("log_amount"));
                log.setUserId(rs.getInt("userid"));
                logs.add(log);
            }
            return logs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }
}
