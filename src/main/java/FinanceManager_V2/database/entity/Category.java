package FinanceManager_V2.Database.Entity;


import FinanceManager_V2.Database.Entity.Database_pk.CategoryPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "category")
@IdClass(CategoryPK.class)
public class Category implements Serializable {
    private static final long serialVersionUID = -6265203350166782424L;
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

    @Column(name = "income")
    private boolean income;

    @OneToOne
    private Category parent;


    public Category() {
    }


    public Category(Long category, Long user, String color, String name, Integer icon_id, boolean income, Category parent) {
        this.category = category;
        this.user = user;
        this.color = color;
        this.name = name;
        this.icon_id = icon_id;
        this.income = income;
        this.parent = parent;
    }

    public Category(CategoryAction action, Category parent){
        this.category = action.getOriginalId();
        this.user = action.getUser();
        this.color = action.getColor();
        this.name = action.getName();
        this.icon_id = action.getIcon_id();
        this.parent = parent;
        this.income = action.isIncome();

    }

    public void updateData(CategoryAction categoryAction, Category parent){
        this.category = categoryAction.getOriginalId();
        this.user = categoryAction.getUser();
        this.color = categoryAction.getColor();
        this.name = categoryAction.getName();
        this.icon_id = categoryAction.getIcon_id();
        this.parent = parent;
        this.income = categoryAction.isIncome();
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return "Category{" +
                "category=" + category +
                ", user=" + user +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", icon_id=" + icon_id +
                ", income=" + income +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return income == category1.income &&
                category.equals(category1.category) &&
                user.equals(category1.user) &&
                color.equals(category1.color) &&
                name.equals(category1.name) &&
                icon_id.equals(category1.icon_id) &&
                Objects.equals(parent, category1.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, user, color, name, icon_id, income, parent);
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

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    public void setIcon_id(Integer icon_id) {
        this.icon_id = icon_id;
    }
}
