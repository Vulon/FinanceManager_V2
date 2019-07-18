package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Budget;
import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, BudgetPK> {

    @Query
    @Modifying
    @Transactional
    void deleteByUserAndBudget(Long user, Long budget);

    @Query(value = "SELECT b FROM Budget b WHERE b.user = :user AND :category IN (b.categories)") //TODO CHECK THAT
    List<Budget> getAllByUserAndCategory(@Param(value = "user")Long user, @Param(value = "category") Category category);

    @Query
    Budget findByUserAndBudget(Long user, Long budget);
}
