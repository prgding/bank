package com.cx.bank.model;

/**
 * 单例模式-账户余额实体bean
 *
 * @author admin
 * @date 2021.6
 */
public class MoneyBean {

    private double money;//账户余额


    public MoneyBean() {

    }

    /**
     * 获取MoneyBean实例的静态方法
     * @return public static synchronized MoneyBean getInstance() {
    if(moneyBean==null){
    moneyBean=new MoneyBean();
    }
    return  moneyBean;
    }
     */
    /**
     * 得到余额
     */
    public double getMoney() {
        return money;
    }

    /**
     * 存储钱
     */
    public void setMoney(double money) {
        this.money = money;
    }

}
