package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LogRepository logRepository;

    @Override
    public int usersAmount() {
        return accountRepository.findAll().size();
    }

    @Override
    public int logsAmount() {
        return logRepository.findAll().size();
    }

    @Override
    public Page<Account> findUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return accountRepository.findAll(pageRequest);
    }

    @Override
    public Page<Object[]> findLogs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return logRepository.findAllLogWithUsername(pageRequest);
    }
}
