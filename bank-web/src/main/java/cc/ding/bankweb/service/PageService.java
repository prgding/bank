package cc.ding.bankweb.service;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import org.springframework.data.domain.Page;


public interface PageService {

    int usersAmount();

    int logsAmount();

    Page<Account> findUsers(int page, int size);

    Page<Log> findLogs(int page, int size);
}
