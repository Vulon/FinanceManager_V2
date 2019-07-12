package FinanceManager_V2.database.entity.database_pk;

import java.io.Serializable;
import java.util.Objects;

public class BudgetPK implements Serializable {
    private static final long serialVersionUID = -5154859865810069154L;
    public Long budget_id;
    public Long user_id;

    public BudgetPK() {
    }

    public BudgetPK(Long budget_id, Long user_id) {
        this.budget_id = budget_id;
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetPK budgetPK = (BudgetPK) o;
        return budget_id.equals(budgetPK.budget_id) &&
                user_id.equals(budgetPK.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget_id, user_id);
    }
}
