package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.exception.AccountOverDrawnException;
import cc.ding.bankweb.exception.InvalidDepositException;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public BigDecimal inquiry(String username) {
        return accountRepository.findByUsername(username).getBalance();
    }

    @Override
    public String withdrawals(String username, BigDecimal amount) throws AccountOverDrawnException {
        BigDecimal balance = inquiry(username);
        if (balance.compareTo(amount) < 0) {
            throw new AccountOverDrawnException("余额不足，无法取款");
        } else {
            balance = balance.subtract(amount);
            accountRepository.updateAccount(username, balance);
            Integer id = accountRepository.findByUsername(username).getId();
            Log log = new Log(null, "取款", amount, id);
            logRepository.save(log);
            return "取款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public String deposit(String username, BigDecimal amount) throws InvalidDepositException {
        if (amount.signum() < 0) {
            throw new InvalidDepositException("存款不能为负");
        } else {
            BigDecimal balance = inquiry(username).add(amount);
            accountRepository.updateAccount(username, balance);
            Integer id = accountRepository.findByUsername(username).getId();
            Log log = new Log(null, "存款", amount, id);
            logRepository.save(log);
            return "存款成功，余额：" + balance + "\n";
        }
    }

    @Override
    public String transfer(String fromName, String toName, BigDecimal transMoney) throws AccountOverDrawnException {
        Account act = accountRepository.findByUsername(toName);
        if (act == null) {
            return "对方账户不存在";
        } else {
            BigDecimal fromMoney = inquiry(fromName).subtract(transMoney);
            if (fromMoney.compareTo(BigDecimal.ZERO) < 0) {
                throw new AccountOverDrawnException("余额不足，无法转账\n");
            } else {
                accountRepository.updateAccount(fromName, fromMoney);
                Integer fromId = accountRepository.findByUsername(fromName).getId();
                Log fromLog = new Log(null, "转账", transMoney, fromId);
                logRepository.save(fromLog);
                entityManager.clear();
                BigDecimal toMoney = inquiry(toName).add(transMoney);
                Integer toId = accountRepository.findByUsername(toName).getId();
                Log toLog = new Log(null, "收款", transMoney, toId);
                logRepository.save(toLog);
                accountRepository.updateAccount(act.getUsername(), toMoney);
                return "转账成功";
            }
        }
    }
}
