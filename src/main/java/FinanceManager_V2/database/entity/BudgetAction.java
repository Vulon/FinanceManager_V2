package FinanceManager_V2.database.entity;

import FinanceManager_V2.database.entity.database_pk.BudgetPK;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "budget_action")
@IdClass(BudgetPK.class)
public class BudgetAction {
    @Column(name = "is_create")
    private boolean isCreate;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;


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

    public BudgetAction(boolean isCreate, Date commitDate, Long user_id, String name, Double amount, Date start, Date end, Float notifyLevel, Set<Category> categories) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.user_id = user_id;
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.notifyLevel = notifyLevel;
        this.categories = new HashSet<>(categories);
    }

    public BudgetAction() {
    }

    public BudgetAction(boolean isCreate, Date commitDate, Budget budget) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.user_id = budget.getUser_id();
        this.name = budget.getName();
        this.amount = budget.getAmount();
        this.start = budget.getStart();
        this.end = budget.getEnd();
        this.notifyLevel = budget.getNotifyLevel();
        this.categories = new HashSet<>(budget.getCategories());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetAction that = (BudgetAction) o;
        return isCreate == that.isCreate &&
                Objects.equals(commitDate, that.commitDate) &&
                Objects.equals(budget_id, that.budget_id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(notifyLevel, that.notifyLevel) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCreate, commitDate, budget_id, user_id, name, amount, start, end, notifyLevel, categories);
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
