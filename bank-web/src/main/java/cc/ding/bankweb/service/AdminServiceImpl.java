package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.model.Account;
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
}
