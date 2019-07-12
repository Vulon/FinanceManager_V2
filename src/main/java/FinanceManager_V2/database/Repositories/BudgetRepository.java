package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.Budget;
import FinanceManager_V2.database.entity.database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, BudgetPK> {

}
