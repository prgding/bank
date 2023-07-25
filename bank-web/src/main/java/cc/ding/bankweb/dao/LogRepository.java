package cc.ding.bankweb.dao;

import cc.ding.bankweb.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByUserId(Integer userId);

    // select a.username, l.* from account a,log l where a.id = l.user_id
    @Query(value = "select a.username, l.* from account a,log l where a.id = l.user_id", nativeQuery = true)
    List<Object[]> findAllLog();

}
