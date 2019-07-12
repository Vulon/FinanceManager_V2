package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.TransactionAction;
import FinanceManager_V2.database.entity.database_pk.TransactionPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionActionRepository extends JpaRepository<TransactionAction, TransactionPK> {

}
