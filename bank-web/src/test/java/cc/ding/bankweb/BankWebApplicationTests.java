package cc.ding.bankweb;

import cc.ding.bankweb.dao.AccountRepository;
import cc.ding.bankweb.dao.LogRepository;
import cc.ding.bankweb.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BankWebApplicationTests {
    @Autowired
    private LoginService loginService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
        List<Object[]> allLog = logRepository.findAllLog();
        for (Object[] objects : allLog) {
            for (Object object : objects) {
                System.out.println(object);
            }
            System.out.println();
        }
    }
}
