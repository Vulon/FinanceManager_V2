package FinanceManager_V2.Database.Entity;

import FinanceManager_V2.Database.Entity.Database_pk.BudgetPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "budget_action")
@IdClass(BudgetPK.class)
public class BudgetAction implements Serializable, Action {

    private static final long serialVersionUID = 574580749152845095L;
    @Column(name = "create")
    private boolean create;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;


    @Id
    @Column(name = "budget")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Float notifyLevel;

    private Long originalId;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "budget_category", joinColumns = {
            @JoinColumn(name = "budget", referencedColumnName = "budget"),
            @JoinColumn(name = "budget_user", referencedColumnName = "user")
    }, inverseJoinColumns = {
            @JoinColumn(name = "category", referencedColumnName = "category"),
            @JoinColumn(name = "category_user", referencedColumnName = "user")
    })
    private Set<Category> categories = new HashSet<>();

    public BudgetAction(boolean isCreate, Date commitDate, Long user_id, String name, Double amount, Date start, Date end, Float notifyLevel, Set<Category> categories) {
        this.create = isCreate;
        this.commitDate = commitDate;
        this.user = user_id;
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.notifyLevel = notifyLevel;
        this.categories = new HashSet<>(categories);
    }

    @Override
    public Long getOriginalId() {
        return originalId;
    }

    public BudgetAction() {
    }

    public BudgetAction(boolean isCreate, Date commitDate, Budget budget) {
        this.create = isCreate;
        this.commitDate = commitDate;
        this.budget = budget.getBudget();
        this.user = budget.getUser();
        this.name = budget.getName();
        this.amount = budget.getAmount();
        this.start = budget.getStart();
        this.end = budget.getEnd();
        this.notifyLevel = budget.getNotifyLevel();
        this.categories = new HashSet<>(budget.getCategories());
        this.originalId = budget.getBudget();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetAction that = (BudgetAction) o;
        return create == that.create &&
                Objects.equals(commitDate, that.commitDate) &&
                Objects.equals(budget, that.budget) &&
                Objects.equals(user, that.user) &&
                Objects.equals(name, that.name) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(notifyLevel, that.notifyLevel) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(create, commitDate, budget, user, name, amount, start, end, notifyLevel, categories);
    }

    @Override
    public String toString() {
        return "BudgetAction{" +
                "create=" + create +
                ", commitDate=" + commitDate +
                ", budget=" + budget +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", start=" + start +
                ", end=" + end +
                ", notifyLevel=" + notifyLevel +
                ", originalId=" + originalId +
                ", categories=" + categories +
                '}';
    }

    @Override
    public String getType() {
        return "budget";
    }

    @Override
    public boolean isCreate() {
        return create;
    }

    @Override
    public void setCreate(boolean create) {
        this.create = create;
    }

    @Override
    public Date getCommitDate() {
        return commitDate;
    }

    @Override
    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
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
