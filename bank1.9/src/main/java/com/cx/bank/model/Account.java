package com.cx.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Account
 * 账户类
 *
 * @author dingshuai
 * @version 1.9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    int userId;
    String username;
    String password;
    BigDecimal balance;
    int userFlag;
}
