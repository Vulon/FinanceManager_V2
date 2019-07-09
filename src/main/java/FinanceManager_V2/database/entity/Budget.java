package FinanceManager_V2.database.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "budget")
public class Budget {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end")
    private Date end;


    @Column(name = "notifyLevel")
    private Float notifyLevel;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "budget_category", joinColumns = {@JoinColumn(name = "budget_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Set<Category> categories = new HashSet<>();

    public Budget() {
    }

    public Budget(String name, Double amount, Date start, Date end, Set<Category> categories, Float notifyLevel) {
        this.name = name;
        this.amount = amount;
        this.start = start;
        this.end = end;
        this.categories = categories;
        this.notifyLevel = notifyLevel;
    }

    public Budget(String name, Double amount, Date start, Date end, Set<Category> categories) {
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
        return id.equals(budget.id) &&
                name.equals(budget.name) &&
                amount.equals(budget.amount) &&
                start.equals(budget.start) &&
                end.equals(budget.end) &&
                notifyLevel.equals(budget.notifyLevel) &&
                categories.equals(budget.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, start, end, notifyLevel, categories);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
