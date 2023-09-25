package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@CacheConfig(cacheNames = "cc.ding.bankweb.service.PageServiceImpl")
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

    @Cacheable(key = "'users:page:' + #page + ':size:' + #size")
    @Override
    public Page<Account> findUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return accountRepository.findAll(pageRequest);
    }

    @Cacheable(key = "'logs:page:' + #page + ':size:' + #size")
    @Override
    public Page<Object[]> findLogs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return logRepository.findAllLogWithUsername(pageRequest);
    }

    @Override
    public Page<Object[]> findLogsByUserId(int page, int size, int userId) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        logRepository.findLogWithUsernameByUserId(pageRequest, userId).getSize();
        return logRepository.findLogWithUsernameByUserId(pageRequest, userId);
    }

    @Override
    public int logsAmountByUserId(Integer id) {
        return logRepository.findByUserId(id).size();
    }

}
