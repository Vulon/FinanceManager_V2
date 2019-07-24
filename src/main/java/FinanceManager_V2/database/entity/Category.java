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

    @OneToOne
    private Category parent;


    public Category() {
    }





    public Category(Long user_id, String color, String name, Integer icon_id, Category parent) {
        this.user = user_id;
        this.color = color;
        this.name = name;
        this.icon_id = icon_id;
        this.parent = parent;
    }
    public Category(CategoryAction action, Category parent){
        this.category = action.getCategory();
        this.user = action.getUser();
        this.color = action.getColor();
        this.name = action.getName();
        this.icon_id = action.getIcon_id();
        this.parent = parent;
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
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return this.category.equals(category.category) &&
                user.equals(category.user) &&
                color.equals(category.color) &&
                name.equals(category.name) &&
                icon_id.equals(category.icon_id) &&
                parent.equals(category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, user, color, name, icon_id, parent);
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
}
