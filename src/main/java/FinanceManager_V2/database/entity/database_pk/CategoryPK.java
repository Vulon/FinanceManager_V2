package FinanceManager_V2.Database.Entity.Database_pk;

import java.io.Serializable;
import java.util.Objects;

public class CategoryPK implements Serializable {
    private static final long serialVersionUID = -2066601096306737417L;
    public Long category;
    public Long user;

    public CategoryPK(Long category_id, Long user_id) {
        this.category = category_id;
        this.user = user_id;
    }

    public CategoryPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPK that = (CategoryPK) o;
        return category.equals(that.category) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, user);
    }
}
