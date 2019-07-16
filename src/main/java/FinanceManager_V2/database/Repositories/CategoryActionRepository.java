package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.CategoryAction;
import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface CategoryActionRepository extends JpaRepository<CategoryAction, CategoryPK> {
    @Query
    ArrayList<CategoryAction> getAllByUser(Long user);
}
