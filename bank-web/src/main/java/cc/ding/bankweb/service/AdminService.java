package cc.ding.bankweb.service;

import cc.ding.bankweb.model.Account;

import java.util.List;

public interface AdminService {
    List<Account> userList();

    List<Object[]> logList();
}
