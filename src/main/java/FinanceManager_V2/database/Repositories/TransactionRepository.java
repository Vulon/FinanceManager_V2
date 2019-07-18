package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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


}

