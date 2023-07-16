package cc.ding.bankweb.service.impl;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.MoneyBean;
import cc.ding.bankweb.model.UserBean;
import cc.ding.bankweb.service.BankService;
import cc.ding.bankweb.util.AccountOverDrawnException;
import cc.ding.bankweb.util.InvalidDepositException;
import cc.ding.bankweb.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BankServiceImpl
 * 实现ManagerInterface接口
 *
 * @author dingshuai
 * @version 1.8
 */
@Service
public class BankServiceImpl implements BankService {
    private static BankService instance;
    private MoneyBean moneyBean;
    private UserBean userBean;
    private AccountRepository dao;

    public BankServiceImpl() {
    }

    public BankServiceImpl(MoneyBean moneyBean, UserBean userBean) {
        this.moneyBean = moneyBean;
        this.userBean = userBean;
    }

    @Autowired
    public BankServiceImpl(AccountRepository dao) {
        this.dao = dao;
    }

    public static synchronized BankService getInstance() {
        if (instance == null) {
            instance = new BankServiceImpl();
        }
        return instance;
    }

    @Override
    public BankService login(String username, String password) {
        password = MD5Utils.hash(password);
        Account byUsernameAndPassword = dao.findByUsernameAndPassword(username, password);
        if (byUsernameAndPassword != null) {
            instance = getInstance();
            moneyBean = new MoneyBean();
            moneyBean.setBalance(byUsernameAndPassword.getBalance());
            userBean = new UserBean();
            userBean.setUsername(username);
            userBean.setPassword(password);
            return instance;
        } else {
            return null;
        }
    }

    public boolean checkIfExists(String username) {
        return dao.findByUsername(username) != null;
    }

    public Account checkPwd(String username, String password) {
        password = MD5Utils.hash(password);
        return dao.findByUsernameAndPassword(username, password);
    }


    @Override
    public synchronized String deposit(BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负\n");
        } else {
            BigDecimal balance = inquiry(userBean.getUsername()).add(amount);
            moneyBean.setBalance(balance);
            dao.updateAccount(userBean.getUsername(), balance);
            return "存款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public synchronized String withdrawals(BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = inquiry(userBean.getUsername());
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款\n");
        } else {
            balance = balance.subtract(amount);
            moneyBean.setBalance(balance);
            dao.updateAccount(userBean.getUsername(), balance);
            return "取款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public BigDecimal inquiry(String username) {
        return dao.findByUsername(username).getBalance();
    }

    @Override
    public void exitSystem() {
        dao.updateAccount(userBean.getUsername(), moneyBean.getBalance());
        System.out.println("系统已退出");
        System.exit(0);
    }

    @Override
    public String register(String username, String password) {
        password = MD5Utils.hash(password);
        dao.save(new Account(null, username, password, new BigDecimal(10)));
        return "注册成功";
    }


    public String transfer(String toName, BigDecimal transMoney) throws AccountOverDrawnException {
        Account act = dao.findByUsername(toName);
        if (act == null) {
            return "对方账户不存在";
        } else {
            BigDecimal fromMoney = moneyBean.getBalance().subtract(transMoney);
            if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountOverDrawnException("余额不足，无法转账\n");
            } else {
                moneyBean.setBalance(fromMoney);
                dao.updateAccount(userBean.getUsername(), fromMoney);
                BigDecimal toMoney = act.getBalance().add(transMoney);
                dao.updateAccount(act.getUsername(), toMoney);
                return "转账成功，自己账户余额 = " + fromMoney + "\n";
            }
        }
    }

    public MoneyBean getMoneyBean() {
        return moneyBean;
    }

    public void setMoneyBean(MoneyBean moneyBean) {
        this.moneyBean = moneyBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
