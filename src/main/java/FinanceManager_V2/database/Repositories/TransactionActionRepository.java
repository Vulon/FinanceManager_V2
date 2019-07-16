package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.TransactionAction;
import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface TransactionActionRepository extends JpaRepository<TransactionAction, TransactionPK> {
    @Query
    ArrayList<TransactionAction> getAllByUser(Long user);
}
