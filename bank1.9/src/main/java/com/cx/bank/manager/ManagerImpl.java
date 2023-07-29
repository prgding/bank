package com.cx.bank.manager;

import com.cx.bank.dao.BankDao;
import com.cx.bank.dao.LogDao;
import com.cx.bank.dao.LogDaoImpl;
import com.cx.bank.exception.AccountOverDrawnException;
import com.cx.bank.exception.InvalidDepositException;
import com.cx.bank.factory.UserDaoFactory;
import com.cx.bank.model.Account;
import com.cx.bank.model.Log;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import lombok.Data;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * ManagerImpl
 * 实现ManagerInterface接口
 *
 * @author dingshuai
 * @version 1.9
 */
@Data
public class ManagerImpl implements ManagerInterface {
    private static ManagerInterface instance;
    private MoneyBean moneyBean;
    private UserBean userBean;
    private BankDao bankDao;
    private LogDao logDao = new LogDaoImpl();

    public ManagerImpl() {
        this.bankDao = UserDaoFactory.getInstance().getDao();
    }

    public ManagerImpl(MoneyBean moneyBean, UserBean userBean) {
        this.moneyBean = moneyBean;
        this.userBean = userBean;
    }

    public static synchronized ManagerInterface getInstance() {
        if (instance == null) {
            instance = new ManagerImpl();
        }
        return instance;
    }

    @Override
    public String register(String username, String password) {
        boolean byName = bankDao.findByName(username);
        if (byName) {
            return "该用户名已存在\n";
        } else {
            bankDao.insertUser(username, password);
        }
        return "注册成功";
    }

    @Override
    public ManagerInterface login(String username, String password) {
        instance = bankDao.findUser(username, password);
        if (instance != null) {
            System.out.println("登录成功\n");
            BigDecimal fileMoney = instance.inquiry(username);
            moneyBean = new MoneyBean();
            moneyBean.setBalance(fileMoney);
            userBean = new UserBean();
            userBean.setUsername(username);
            userBean.setPassword(password);
            return instance;
        } else {
            System.out.println("登录失败，检查你的用户名和密码\n");
            return null;
        }
    }

    @Override
    public BigDecimal inquiry(String username) {
        return bankDao.getMoney(username);
    }

    @Override
    public synchronized void deposit(BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负\n");
        } else {
            BigDecimal balance = inquiry(userBean.getUsername()).add(amount);
            moneyBean.setBalance(balance);
            System.out.println("存款成功，余额：" + balance + "\n");
        }
        Account one = bankDao.findOne(userBean.getUsername());
        logDao.insert("存款", amount, one.getUserId());
        bankDao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
    }

    @Override
    public synchronized void withdrawals(BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = inquiry(userBean.getUsername());
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款\n");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            System.out.println("取款成功，余额：" + balance + "\n");
        }
        Account one = bankDao.findOne(userBean.getUsername());
        logDao.insert("取款", amount, one.getUserId());
        bankDao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
    }

    public void transfer(String toName, BigDecimal transMoney) throws IOException, AccountOverDrawnException {
        boolean exist = bankDao.findByName(toName);
        if (!exist) {
            throw new IOException("对方账户不存在");
        } else {
            BigDecimal fromMoney = inquiry(userBean.getUsername()).subtract(transMoney);
            if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountOverDrawnException("余额不足，无法转账\n");
            } else {
                moneyBean.setBalance(fromMoney);
                bankDao.updateMoney(userBean.getUsername(), fromMoney);
                Account fromAct = bankDao.findOne(userBean.getUsername());
                logDao.insert("转账", transMoney, fromAct.getUserId());
                BigDecimal toMoney = inquiry(toName).add(transMoney);
                bankDao.updateMoney(toName, toMoney);
                Account toAct = bankDao.findOne(toName);
                logDao.insert("收款", transMoney, toAct.getUserId());
                System.out.println("转账成功，自己账户余额 = " + fromMoney + "\n");
            }
        }
    }


    @Override
    public void exitSystem() {
        if (moneyBean != null && userBean != null) {
            bankDao.updateMoney(userBean.getUsername(), moneyBean.getBalance());
        }
        System.out.println("系统已退出");
        System.exit(0);
    }


    @Override
    public List<Account> inquiryAllUser() {
        return bankDao.findAll();
    }

    @Override
    public Account inquiryUser(String username) {
        return bankDao.findOne(username);
    }

    @Override
    public void freezeUser(String username) {
        bankDao.updateFlag(username, 0);
    }

    @Override
    public void unfreezeUser(String username) {
        bankDao.updateFlag(username, 1);
    }

    @Override
    public List<Log> findLogsByName(String username) {
        return logDao.findLogsByName(username);
    }
}
