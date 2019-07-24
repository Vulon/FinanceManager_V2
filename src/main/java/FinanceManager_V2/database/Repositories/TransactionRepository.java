package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionPK> {


    @Modifying
    @Transactional
    void deleteByUserAndTransaction(Long user, Long transaction);



    @Query
    List<Transaction> getAllByUserAndCategory(Long user, Category category);

    @Query
    Transaction getByUserAndTransaction(Long user, Long transaction);

    @Query
    int countAllByUser(Long user);


    @Query("SELECT t FROM Transaction t WHERE t.user = :user group by t.user HAVING COUNT(t) <= :num ORDER BY t.date DESC")
    ArrayList<Transaction> findFirstTransactions(@Param("user") Long user, @Param("num") Long num);


}

