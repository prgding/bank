package cc.ding.bankweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BankWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankWebApplication.class, args);
    }
}
