package com.cx.bank.dao;


public interface BankDaoInterface {
    void saveMoney(); //存储方法说明,可用于退出系统功能

    void insertUser(); //添加用户方法说明,可用于注册功能

    void updateMoney(String username, double money); //更新方法说明,可用于转账功能

    void findByName(); //按名字查找用户说明,可用于注册功能

    void findUser(); //按用户名和密码查找用户说明,可用于登录功能

}