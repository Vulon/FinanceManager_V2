package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionPK> {
    //@Modifying
    //@Query("DELETE FROM Transaction WHERE note LIKE '%test'")
   // public void deleteTests();


    //@Query("SELECT t FROM Transaction t WHERE t.note LIKE '%test'")
    //public List<Transaction> getTestTransactions();
    @Transactional public long deleteByNoteLike(String param);
    public List<Transaction> findByNoteLike(String param);
}

