package FinanceManager_V2.Database.Entity;


import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "category_action")
@IdClass(CategoryPK.class)
public class CategoryAction extends Action implements Serializable {

    private static final long serialVersionUID = -7163736650756578806L;
    @Column(name = "is_create")
    private boolean isCreate;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;

    @Id
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

    @Column(name = "parent_id")
    private Long parent_id;

    public CategoryAction(boolean isCreate, Date commitDate, Long category_id, Long user_id, String color, String name, Integer icon_id, Long parent_id) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.category = category_id;
        this.user = user_id;
        this.color = color;
        this.name = name;
        this.icon_id = icon_id;
        this.parent_id = parent_id;
    }

    public CategoryAction() {
    }

    public CategoryAction(boolean isCreate, Date commitDate, Category category) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.category = category.getCategory();
        this.user = category.getUser();
        this.color = category.getColor();
        this.name = category.getName();
        this.icon_id = category.getIcon_id();
        this.parent_id = category.getParent().getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAction that = (CategoryAction) o;
        return isCreate == that.isCreate &&
                Objects.equals(commitDate, that.commitDate) &&
                Objects.equals(category, that.category) &&
                Objects.equals(user, that.user) &&
                Objects.equals(color, that.color) &&
                Objects.equals(name, that.name) &&
                Objects.equals(icon_id, that.icon_id) &&
                Objects.equals(parent_id, that.parent_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCreate, commitDate, category, user, color, name, icon_id, parent_id);
    }

    @Override
    public String getType() {
        return "category";
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
}
