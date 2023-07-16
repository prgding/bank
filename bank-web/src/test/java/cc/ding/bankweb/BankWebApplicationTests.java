package cc.ding.bankweb;

import cc.ding.bankweb.controller.BankController;
import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.model.LoginUser;
import cc.ding.bankweb.service.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankWebApplicationTests {
    @Autowired
    private BankService bankService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
        BankController bankController = new BankController();
        LoginUser loginUser = new LoginUser("11","22","capt");
        bankController.register(loginUser);
    }
}
