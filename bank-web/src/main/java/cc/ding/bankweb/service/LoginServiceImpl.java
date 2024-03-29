package cc.ding.bankweb.service;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.util.MD5Utils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * LoginServiceImpl
 * 实现ManagerInterface接口
 *
 * @author dingshuai
 * @version 1.8
 */
@Service
@NoArgsConstructor
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AccountRepository accountRepository;

    public Account checkIfExists(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public String register(String username, String password) {
        password = MD5Utils.hash(password);
        accountRepository.save(new Account(null, username, password, new BigDecimal(10), 1, null));
        return "注册成功";
    }

    @Override
    public Account checkPwd(String username, String password) {
        password = MD5Utils.hash(password);
        return accountRepository.findByUsernameAndPassword(username, password);
    }
}
