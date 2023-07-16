package com.cx.bank.model;

//用于封装用户数据

public class UserBean {
    private String userName;
    private String password;

    public UserBean() {
    }

    public UserBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
