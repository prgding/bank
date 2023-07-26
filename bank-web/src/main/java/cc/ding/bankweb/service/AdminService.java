package cc.ding.bankweb.service;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;

import java.util.List;

public interface AdminService {
    List<Account> userList();

    List<Object[]> logList();

    void freeze(Integer id);

    void unfreeze(Integer id);

    void updateUser(Account act);

    void updateLog(Log log);

    void deleteUser(Integer id);

    void deleteLog(Integer logId);
}
