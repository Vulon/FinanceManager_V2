package FinanceManager_V2.database.entity;


import FinanceManager_V2.TransportableDataObjects.TokenData;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "lang")
    private String lang;

    @Column(name = "access_token")
    private String access_token;

    @Column(name = "refresh_token")
    private String refresh_token;

    @Column(name = "access_token_exp")
    private Date access_token_exp;

    @Column(name = "refresh_token_exp")
    private Date refresh_token_exp;



    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_transaction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "transaction_id")}
    )
    private Set<Transaction> transactions;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_category",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_budget",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "budget_id")}
    )
    private Set<Budget> budgets;

    public User(String email, String password, String lang) {
        this.email = email;
        this.password = password;
        this.lang = lang;
        transactions = new HashSet<>();
        categories = new HashSet<>();
        budgets = new HashSet<>();
    }

    public User() {
    }

    public void setTokenData(TokenData tokenData){
        access_token = tokenData.getAccess_token();
        refresh_token = tokenData.getRefresh_token();
        access_token_exp = tokenData.getAccess_token_expire_date();
        refresh_token_exp = tokenData.getRefresh_token_expire_date();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lang='" + lang + '\'' +
                ", transactions=" + transactions +
                ", categories=" + categories +
                ", budgets=" + budgets +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = new HashSet<>(transactions);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = new HashSet<>(categories);
    }

    public Set<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        this.budgets = new HashSet<>(budgets);
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Date getAccess_token_exp() {
        return access_token_exp;
    }
    public Date getRefresh_token_exp() {
        return refresh_token_exp;
    }
}
