package cc.ding.bankweb.dao;

import cc.ding.bankweb.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByUserId(Integer userId);

    // select a.username, l.* from account a,log l where a.id = l.user_id
    @Query(value = "select a.username, l.* from account a,log l where a.id = l.user_id", nativeQuery = true)
    List<Object[]> findAllLog();

    @Modifying
    @Query("UPDATE Log l SET l.logAmount = :amount, l.logType =:logType  where l.logId= :id")
    @Transactional
    void updateLog(@Param("id") Integer id, @Param("amount") BigDecimal amount, @Param("logType") String type);

    @Query(value = "select a.username, l.* from account a, log l where a.id = l.user_id",
            nativeQuery = true)
    Page<Object[]> findAllLogWithUsername(Pageable pageable);

    @Query(value = "select a.username, l.* from account a, log l where a.id = l.user_id and a.id = :userId",
            nativeQuery = true)
    Page<Object[]> findLogWithUsernameByUserId(PageRequest pageRequest, @Param("userId") int userId);


}
