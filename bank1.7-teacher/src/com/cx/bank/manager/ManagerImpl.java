package com.cx.bank.manager;

import com.cx.bank.dao.BankDaoInterface;
import com.cx.bank.factory.UserDaoFactory;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;


/**
 * ManagerImpl业务层实现
 *
 * @author admin
 */
public class ManagerImpl implements ManagerInterface {

    private static ManagerImpl instance = null;
    //MoneyBean moneyBean = MoneyBean.getInstance();//获取单例的模型层对象
    MoneyBean moneyBean = new MoneyBean();//获取单例的模型层对象
    private BankDaoInterface userDao = null;

    private ManagerImpl() throws Exception {
        UserDaoFactory DaoFactory = UserDaoFactory.getInstance();
        userDao = DaoFactory.createUserDao();
    }

    public static synchronized ManagerImpl getInstance() throws Exception {
        if (instance == null)
            instance = new ManagerImpl();
        return instance;
    }

    /**
     * 注册
     *
     * @param 用户名
     * @param 密码
     * @return boolean
     */
    public boolean register(String name, String psd) {
        UserBean user = new UserBean();
        user.setUserName(name);
        user.setPassword(psd);
        if ("".equals(name) || "".equals(psd)) {
            System.out.println("用户名或密码不能为空！");
            return false;
        }
        boolean flag = userDao.findByName(name);
        if (flag) {
            userDao.insertUser(user);
        }
        return flag;
    }

    /**
     * 登录方法的实现
     *
     * @param 用户名
     * @param 密码
     * @return boolean
     */
    public boolean login(String name, String psd) {
        UserBean user = new UserBean();
        user.setUserName(name);
        user.setPassword(psd);
        if ("".equals(name) || "".equals(psd)) {
            System.out.println("用户名或密码不能为空！");
            return false;
        }
        return userDao.findUser(user, moneyBean);
    }

    /**
     * 实现存款功能
     *
     * @param 存款金额
     * @return void
     * @throws InvalidDepositException,NumberFormatException
     */

    public void deposit(String smoney) throws InvalidDepositException, NumberFormatException {

        double money = Double.parseDouble(smoney);
//			如果用户存钱金额小于0，则抛出InvalidDepositException异常
        if (money <= 0)
            throw new InvalidDepositException("存款金额需大于0");

        double leave = moneyBean.getMoney();
        moneyBean.setMoney(leave + money);//	存入


    }

    /**
     * 实现取款功能
     *
     * @param 取款金额
     * @return void
     * @throws AccountOverDrawnException，NumberFormatException
     */
    public void withdrawals(String smoney) throws AccountOverDrawnException, NumberFormatException {

        double money = Double.parseDouble(smoney);

        double leave = moneyBean.getMoney();

        if (money > leave)
            //如果用户取款金额大于余额，则抛出AccountOverDrawnException异常
            throw new AccountOverDrawnException("余额不足");

        moneyBean.setMoney(leave - money);//存入

    }


    /**
     * 实现查看余额功能
     *
     * @return 余额
     */
    public double inquiry() {

        double money = moneyBean.getMoney();
        return money;
    }

    /**
     * 实现转账功能
     *
     * @param inAccount 转入账号
     * @param money     转账金额
     * @return boolean
     * @throws NumberFormatException，IOException
     */
    public boolean tranferMoney(String inAccount, String money) throws NumberFormatException, IOException {
        double dmoney = Double.parseDouble(money);//把转账金额转化为数字
        double moneyBalance = moneyBean.getMoney();

        if (dmoney < moneyBalance && dmoney > 0)//判断你的余额是否大于转账金额和转账金额是否大于零
        {
            userDao.updateMoney(moneyBean, inAccount, dmoney);//调用更新方法
            return true;
        } else {
            System.out.println("转账金额不足或金额不合法!");
            return false;
        }

    }


    /**
     * 退出系统
     *
     * @param logname 退出账号（用户名）
     * @return void
     */
    public void exitSystem(String logname) {
        try {
            userDao.saveMoney(logname, moneyBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("系统已经退出");
        System.exit(1);
    }


}
