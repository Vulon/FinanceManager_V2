package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.Category;
import FinanceManager_V2.database.entity.database_pk.CategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, CategoryPK> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.id NOT IN (SELECT t.category.id FROM Transaction t)")
    public int deleteUnused();
}
