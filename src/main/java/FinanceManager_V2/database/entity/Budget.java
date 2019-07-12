package FinanceManager_V2.database.entity;

import FinanceManager_V2.database.entity.database_pk.BudgetPK;

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
    @Column(name = "budget_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long budget_id;


    @Id
    @Column(name = "user_id")
    private Long user_id;


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
    private Float notifyLevel;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "budget_category", joinColumns = {
            @JoinColumn(name = "budget_id", referencedColumnName = "budget_id"),
            @JoinColumn(name = "budget_user", referencedColumnName = "user_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "category_id", referencedColumnName = "category_id"),
            @JoinColumn(name = "category_user", referencedColumnName = "user_id")
    })

    private Set<Category> categories = new HashSet<>();

    public Budget() {
    }


    public Budget(Long user_id, String name, Double amount, Date start, Date end, Float notifyLevel, Set<Category> categories) {
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.notifyLevel = notifyLevel;
        this.categories = categories;
    }

    public Budget(Long user_id, String name, Double amount, Date start, Date end, Set<Category> categories) {
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.categories = categories;
        notifyLevel = 0.25f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return budget_id.equals(budget.budget_id) &&
                user_id.equals(budget.user_id) &&
                name.equals(budget.name) &&
                amount.equals(budget.amount) &&
                start.equals(budget.start) &&
                end.equals(budget.end) &&
                notifyLevel.equals(budget.notifyLevel) &&
                Objects.equals(categories, budget.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget_id, user_id, name, amount, start, end, notifyLevel, categories);
    }

    public Long getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(Long budget_id) {
        this.budget_id = budget_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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

    public Float getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(Float notifyLevel) {
        this.notifyLevel = notifyLevel;
    }
}
