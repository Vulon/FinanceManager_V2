package FinanceManager_V2.database;

import FinanceManager_V2.database.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

}
