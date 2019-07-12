package FinanceManager_V2.database.Repositories;

import FinanceManager_V2.database.entity.CategoryAction;
import FinanceManager_V2.database.entity.database_pk.CategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryActionRepository extends JpaRepository<CategoryAction, CategoryPK> {
}
