package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.BudgetAction;
import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public interface BudgetActionRepository extends JpaRepository<BudgetAction, BudgetPK> {
    @Query
    ArrayList<BudgetAction> getAllByUser(Long user);


    @Modifying
    @Query
    @Transactional
    void deleteByUserAndBudgetAndCreate(Long user, Long budget, boolean create);

    @Modifying
    @Query
    @Transactional
    void deleteAllByUser(Long user);
}
