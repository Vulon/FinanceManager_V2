package FinanceManager_V2.Database.Entity.Database_pk;

import java.io.Serializable;
import java.util.Objects;

public class BudgetPK implements Serializable {
    private static final long serialVersionUID = -5154859865810069154L;
    public Long budget;
    public Long user;

    public BudgetPK() {
    }

    public BudgetPK(Long budget_id, Long user_id) {
        this.budget = budget_id;
        this.user = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetPK budgetPK = (BudgetPK) o;
        return budget.equals(budgetPK.budget) &&
                user.equals(budgetPK.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, user);
    }
}
