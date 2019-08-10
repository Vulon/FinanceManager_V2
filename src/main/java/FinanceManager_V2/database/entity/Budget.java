package FinanceManager_V2.Database.Entity;

import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@IdClass(BudgetPK.class)
public class Budget implements Serializable {
    private static final long serialVersionUID = -5300057576001284517L;

    @Id
    @Column(name = "budget")
    private Long budget;


    @Id
    @Column(name = "user")
    private Long user;


    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "start")
    private Date start;

    @Temporal(TemporalType.DATE)
    @Column(name = "end")
    private Date end;

    @Column(name = "notifyLevel")
    private Double notifyLevel;

    @OneToOne
    private Category category;

    @Column(name = "repeatable")
    private boolean repeatable;

    public Budget() {
    }


    public Budget(Long budget, Long user, String name, Double amount, Date start, Date end, Double notifyLevel, Category category, boolean repeatable) {
        this.budget = budget;
        this.user = user;
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.notifyLevel = notifyLevel;
        this.category = category;
        this.repeatable = repeatable;
    }

    public Budget(BudgetAction action, Category category){
        this.budget = action.getOriginalId();
        this.user = action.getUser();
        this.name = action.getName();
        this.amount = action.getAmount();
        this.start = action.getStart();
        this.end = action.getEnd();
        this.notifyLevel = action.getNotifyLevel();
        this.category = category;
        this.repeatable = action.isRepeatable();
    }


    public void updateData(BudgetAction budgetAction, Category category){
        this.budget = budgetAction.getOriginalId();
        this.user = budgetAction.getUser();
        this.name = budgetAction.getName();
        this.amount = budgetAction.getAmount();
        this.start = budgetAction.getStart();
        this.end = budgetAction.getEnd();
        this.category = category;
        this.repeatable = budgetAction.isRepeatable();
        this.notifyLevel = budgetAction.getNotifyLevel();
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budget=" + budget +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", start=" + start +
                ", end=" + end +
                ", notifyLevel=" + notifyLevel +
                ", category=" + category +
                ", repeatable=" + repeatable +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget1 = (Budget) o;
        return repeatable == budget1.repeatable &&
                Objects.equals(budget, budget1.budget) &&
                Objects.equals(user, budget1.user) &&
                Objects.equals(name, budget1.name) &&
                Objects.equals(amount, budget1.amount) &&
                Objects.equals(start, budget1.start) &&
                Objects.equals(end, budget1.end) &&
                Objects.equals(notifyLevel, budget1.notifyLevel) &&
                Objects.equals(category, budget1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, user, name, amount, start, end, notifyLevel, category, repeatable);
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Double getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(Double notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }
}
