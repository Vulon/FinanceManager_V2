package FinanceManager_V2.database.entity;

import FinanceManager_V2.database.entity.database_pk.TransactionPK;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long transaction_id;

    @Id
    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "amount")
    private Double amount;


    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Column(name = "note")
    private String note;

    @OneToOne
    private Category category;


    public Transaction(Long user_id, Double amount, Date date, String note, Category category) {
        this.user_id = user_id;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.category = category;
    }

    public Transaction() {
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_id=" + transaction_id +
                ", user_id=" + user_id +
                ", amount=" + amount +
                ", date=" + date +
                ", note='" + note + '\'' +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transaction_id.equals(that.transaction_id) &&
                user_id.equals(that.user_id) &&
                amount.equals(that.amount) &&
                date.equals(that.date) &&
                Objects.equals(note, that.note) &&
                category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction_id, user_id, amount, date, note, category);
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
}
