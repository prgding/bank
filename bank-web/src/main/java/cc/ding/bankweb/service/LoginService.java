package cc.ding.bankweb.service;


import cc.ding.bankweb.model.Account;

/**
 * LoginService
 * 业务类接口
 *
 * @author dingshuai
 * @version 1.8
 */
public interface LoginService {
    boolean checkIfExists(String username);

    String register(String username, String password);

    Account checkPwd(String username, String password);

}
