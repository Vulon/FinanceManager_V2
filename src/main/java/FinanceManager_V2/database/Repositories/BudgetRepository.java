package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Budget;
import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, BudgetPK> {

}
