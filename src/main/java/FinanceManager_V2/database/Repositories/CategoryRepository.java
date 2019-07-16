package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, CategoryPK> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.id NOT IN (SELECT t.category.id FROM Transaction t)")
    public int deleteUnused();
}
