package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query
    User findByEmail(String email);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.access_token = :access, u.refresh_token = :refresh, u.access_token_exp = :access_exp, u.refresh_token_exp = :refresh_exp WHERE u.id = :id")
    void updateTokens(@Param("id")Long user_id, @Param("access") String access_token, @Param("access_exp")Date access_exp,
                      @Param("refresh") String refresh_token, @Param("refresh_exp") Date refresh_exp);

    @Query
    User getById(Long id);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query
    void deleteByEmail(String email);
}
