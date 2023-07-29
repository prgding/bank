package com.cx.bank.dao;

import com.cx.bank.model.Log;

import java.math.BigDecimal;
import java.util.List;

public interface LogDao {
    void insert(String logType, BigDecimal logAmount, int userId);

    Log findOne(int userId);

    List<Log> findAll();

    List<Log> findLogsByName(String username);
}
