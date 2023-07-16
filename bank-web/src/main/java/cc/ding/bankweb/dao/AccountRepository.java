package cc.ding.bankweb.dao;

import cc.ding.bankweb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.username = :username")
    void updateAccount(@Param("username") String username, @Param("balance") BigDecimal balance);

    Account findByUsername(String username);

    Account findByUsernameAndPassword(String username, String password);
}
