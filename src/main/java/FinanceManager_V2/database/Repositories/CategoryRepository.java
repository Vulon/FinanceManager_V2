package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface CategoryRepository extends JpaRepository<Category, CategoryPK> {

    @Query
    Category getByUserAndCategory(Long user_id, Long category_id);

    @Query
    ArrayList<Category> getAllByUserAndAndParent(Long user, Category parent);

    @Modifying
    @Transactional
    @Query
    void deleteByUserAndCategory(Long user, Long category);

    @Query
    ArrayList<Category> findAllByUser(Long user);
    
    
}
