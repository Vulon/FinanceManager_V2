package FinanceManager_V2.database.entity;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon_id", nullable = false)
    private Integer icon_id;

    @OneToOne
    private Category parent;


    public Category() {
    }

    public Category(String color, String name, Integer icon_id, Category parent) {
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
                "id=" + id +
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
        return id.equals(category.id) &&
                color.equals(category.color) &&
                name.equals(category.name) &&
                icon_id.equals(category.icon_id) &&
                Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, name, icon_id, parent);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
