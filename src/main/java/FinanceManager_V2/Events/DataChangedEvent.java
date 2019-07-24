package FinanceManager_V2.Events;

import org.springframework.context.ApplicationEvent;

public class DataChangedEvent extends ApplicationEvent {
    private boolean categoriesChanged;
    private boolean transactionsChanged;
    private boolean budgetsChanged;

    public DataChangedEvent(Object source, boolean categoriesChanged, boolean transactionsChanged, boolean budgetsChanged) {
        super(source);
        this.categoriesChanged = categoriesChanged;
        this.transactionsChanged = transactionsChanged;
        this.budgetsChanged = budgetsChanged;
    }

    public boolean isCategoriesChanged() {
        return categoriesChanged;
    }

    public void setCategoriesChanged(boolean categoriesChanged) {
        this.categoriesChanged = categoriesChanged;
    }

    public boolean isTransactionsChanged() {
        return transactionsChanged;
    }

    public void setTransactionsChanged(boolean transactionsChanged) {
        this.transactionsChanged = transactionsChanged;
    }

    public boolean isBudgetsChanged() {
        return budgetsChanged;
    }

    public void setBudgetsChanged(boolean budgetsChanged) {
        this.budgetsChanged = budgetsChanged;
    }
}
