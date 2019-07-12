package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.Transaction;
import FinanceManager_V2.database.entity.database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
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
