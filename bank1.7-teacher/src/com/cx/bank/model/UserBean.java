package com.cx.bank.model;

/**
 * 用户实体类
 *
 * @author Administrator
 */
public class UserBean {

    private String userName;// 用户名
    private String password;// 密码

    public UserBean() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


}
