package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Budget;
import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, BudgetPK> {

    @Query
    @Modifying
    @Transactional
    void deleteByUserAndBudget(Long user, Long budget);

    @Query
    ArrayList<Budget> getAllByUser(Long user);

    @Query
    Budget getByUserAndBudget(Long user, Long budget);


}
