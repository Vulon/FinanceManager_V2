package FinanceManager_V2.Database.Entity;

import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "transaction")
@IdClass(TransactionPK.class)
public class Transaction implements Serializable {
    private static final long serialVersionUID = -5744874026510540290L;
    @Id
    @Column(name = "transaction")
    private Long transaction;

    @Id
    @Column(name = "user")
    private Long user;

    @Column(name = "amount")
    private Double amount;


    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Column(name = "note")
    private String note;

    @OneToOne
    private Category category;

    @Column(name = "repeatable")
    private boolean repeatable;


    public Transaction(Long transaction, Long user, Double amount, Date date, String note, Category category, boolean repeatable) {
        this.transaction = transaction;
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.category = category;
        this.repeatable = repeatable;
    }

    public Transaction(TransactionAction action, Category category){
        this.transaction = action.getOriginalId();
        this.user = action.getUser();
        this.amount = action.getAmount();
        this.date = action.getDate();
        this.note = action.getNote();
        this.category = category;
        this.repeatable = action.isRepeatable();
    }

    public void updateData(TransactionAction transactionAction, Category category){
        this.transaction = transactionAction.getOriginalId();
        this.user = transactionAction.getUser();
        this.amount = transactionAction.getAmount();
        this.date = transactionAction.getDate();
        this.note = transactionAction.getNote();
        this.category = category;
        this.repeatable = transactionAction.isRepeatable();
    }

    public Transaction() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction=" + transaction +
                ", user=" + user +
                ", amount=" + amount +
                ", date=" + date +
                ", note='" + note + '\'' +
                ", category=" + category +
                ", repeatable=" + repeatable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return repeatable == that.repeatable &&
                transaction.equals(that.transaction) &&
                user.equals(that.user) &&
                amount.equals(that.amount) &&
                date.equals(that.date) &&
                note.equals(that.note) &&
                category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, user, amount, date, note, category, repeatable);
    }

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }
}
