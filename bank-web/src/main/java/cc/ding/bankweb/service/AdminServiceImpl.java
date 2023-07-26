package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LogRepository logRepository;

    @Override
    public List<Account> userList() {
        return accountRepository.findAll();
    }

    @Override
    public List<Object[]> logList() {
        return logRepository.findAllLog();
    }

    @Override
    public void freeze(Integer id) {
        accountRepository.updateFlag(id, 0);
    }

    @Override
    public void unfreeze(Integer id) {
        accountRepository.updateFlag(id, 1);
    }

    @Override
    public void updateUser(Account act) {
        accountRepository.updateUser(act.getId(), act.getUsername(), act.getBalance());
    }

    @Override
    public void updateLog(Log log) {
        logRepository.updateLog(log.getLogId(), log.getLogAmount(), log.getLogType());
    }

    @Override
    public void deleteUser(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    public void deleteLog(Integer logId) {
        logRepository.deleteById(logId);
    }
}
