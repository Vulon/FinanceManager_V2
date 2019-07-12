package FinanceManager_V2.database.entity.database_pk;

import java.io.Serializable;
import java.util.Objects;

public class CategoryPK implements Serializable {
    private static final long serialVersionUID = -2066601096306737417L;
    public Long category_id;
    public Long user_id;

    public CategoryPK(Long category_id, Long user_id) {
        this.category_id = category_id;
        this.user_id = user_id;
    }

    public CategoryPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPK that = (CategoryPK) o;
        return category_id.equals(that.category_id) &&
                user_id.equals(that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id, user_id);
    }
}
