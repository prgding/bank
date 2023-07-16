package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.util.AccountOverDrawnException;
import cc.ding.bankweb.util.InvalidDepositException;
import cc.ding.bankweb.util.MD5Utils;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class BankServiceImpl implements BankService {
    @Autowired
    private AccountRepository dao;
    @Autowired
    private EntityManager entityManager;

    public boolean checkIfExists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Override
    public String register(String username, String password) {
        password = MD5Utils.hash(password);
        dao.save(new Account(null, username, password, new BigDecimal(10)));
        return "注册成功";
    }

    @Override
    public Account checkPwd(String username, String password) {
        password = MD5Utils.hash(password);
        return dao.findByUsernameAndPassword(username, password);
    }

    @Override
    public BigDecimal inquiry(String username) {
        return dao.findByUsername(username).getBalance();
    }

    @Override
    public String withdrawals(String username, BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = inquiry(username);
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款");
        } else {
            balance = balance.subtract(amount);
            dao.updateAccount(username, balance);
            return "取款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public String deposit(String username, BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负");
        } else {
            BigDecimal balance = inquiry(username).add(amount);
            dao.updateAccount(username, balance);
            return "存款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public String transfer(String fromName, String toName, BigDecimal transMoney) throws AccountOverDrawnException {
        Account act = dao.findByUsername(toName);
        if (act == null) {
            return "对方账户不存在";
        } else {
            BigDecimal fromMoney = inquiry(fromName).subtract(transMoney);
            if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountOverDrawnException("余额不足，无法转账\n");
            } else {
                dao.updateAccount(fromName, fromMoney);
                entityManager.clear();
                BigDecimal toMoney = inquiry(toName).add(transMoney);
                dao.updateAccount(act.getUsername(), toMoney);
                return "转账成功";
            }
        }
    }
}
