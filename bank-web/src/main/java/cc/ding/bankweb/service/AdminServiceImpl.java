package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void updateLog(Log log) {
        logRepository.updateLog(log.getLogId(), log.getLogAmount(), log.getLogType());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void freeze(Integer id) {
        accountRepository.updateFlag(id, 0);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void unfreeze(Integer id) {
        accountRepository.updateFlag(id, 1);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void updateUser(Account act) {
        accountRepository.save(act);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void deleteUser(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.AdminServiceImpl", allEntries = true),
            @CacheEvict(cacheNames = "cc.ding.bankweb.service.PageServiceImpl", allEntries = true)
    })
    public void deleteLog(Integer logId) {
        logRepository.deleteById(logId);
    }
}
