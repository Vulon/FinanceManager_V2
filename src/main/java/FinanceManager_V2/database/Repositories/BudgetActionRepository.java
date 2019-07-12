package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.BudgetAction;
import FinanceManager_V2.database.entity.database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetActionRepository extends JpaRepository<BudgetAction, BudgetPK> {

}
