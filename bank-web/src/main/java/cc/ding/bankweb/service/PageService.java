package cc.ding.bankweb.service;

import cc.ding.bankweb.model.Account;
import org.springframework.data.domain.Page;


public interface PageService {

    int usersAmount();

    int logsAmount();

    Page<Account> findUsers(int page, int size);

    Page<Object[]> findLogs(int page, int size);

    Page<Object[]> findLogsByUserId(int page, int size, int userId);

    int logsAmountByUserId(Integer id);
}
