package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.BudgetAction;
import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface BudgetActionRepository extends JpaRepository<BudgetAction, BudgetPK> {
    @Query
    ArrayList<BudgetAction> getAllByUser(Long user);
}
