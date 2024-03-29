package FinanceManager_V2.Database.Entity;


import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "category_action")
@IdClass(CategoryPK.class)
public class CategoryAction implements Serializable, Action {

    private static final long serialVersionUID = -7163736650756578806L;
    @Column(name = "create")
    private boolean create;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category")
    private Long category;

    @Id
    @Column(name = "user")
    private Long user;

    @Column(name = "color")
    private String color;

    @Column(name = "name")
    private String name;

    @Column(name = "icon_id")
    private Integer icon_id;

    @Column(name = "income")
    private boolean income;

    @Column(name = "parent_id")
    private Long parent_id;



    private Long originalId;

    public CategoryAction(boolean create, Date commitDate, Long user, String color, String name, Integer icon_id, boolean income, Long parent_id) {
        this.create = create;
        this.commitDate = commitDate;
        this.user = user;
        this.color = color;
        this.name = name;
        this.icon_id = icon_id;
        this.income = income;
        this.parent_id = parent_id;
    }

    public CategoryAction() {
    }

    @Override
    public Long getOriginalId() {
        return originalId;
    }

    public CategoryAction(boolean isCreate, Date commitDate, Category category) {
        this.create = isCreate;
        this.category = category.getCategory();
        this.commitDate = commitDate;
        this.category = category.getCategory();
        this.user = category.getUser();
        this.color = category.getColor();
        this.name = category.getName();
        this.icon_id = category.getIcon_id();
        this.parent_id = category.getParent().getCategory();
        originalId = category.getCategory();
        this.income = category.isIncome();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAction that = (CategoryAction) o;
        return create == that.create &&
                income == that.income &&
                commitDate.equals(that.commitDate) &&
                category.equals(that.category) &&
                user.equals(that.user) &&
                color.equals(that.color) &&
                name.equals(that.name) &&
                icon_id.equals(that.icon_id) &&
                Objects.equals(parent_id, that.parent_id) &&
                originalId.equals(that.originalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(create, commitDate, category, user, color, name, icon_id, income, parent_id, originalId);
    }

    @Override
    public String toString() {
        return "CategoryAction{" +
                "create=" + create +
                ", commitDate=" + commitDate +
                ", category=" + category +
                ", user=" + user +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", icon_id=" + icon_id +
                ", income=" + income +
                ", parent_id=" + parent_id +
                ", originalId=" + originalId +
                '}';
    }

    @Override
    public String getType() {
        return "category";
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(Integer icon_id) {
        this.icon_id = icon_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }
}
