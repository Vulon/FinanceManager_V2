package FinanceManager_V2.database.entity;


import FinanceManager_V2.database.entity.database_pk.CategoryPK;

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
    @Column(name = "category_id")
    private Long category_id;

    @Id
    @Column(name = "user_id")
    private Long user_id;

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
        this.user_id = user_id;
        this.color = color;
        this.name = name;
        this.icon_id = icon_id;
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
                "category_id=" + category_id +
                ", user_id=" + user_id +
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
        return category_id.equals(category.category_id) &&
                user_id.equals(category.user_id) &&
                color.equals(category.color) &&
                name.equals(category.name) &&
                icon_id.equals(category.icon_id) &&
                parent.equals(category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id, user_id, color, name, icon_id, parent);
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
