package FinanceManager_V2.database.entity;

import FinanceManager_V2.database.entity.database_pk.TransactionPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "transaction_action")
@IdClass(TransactionPK.class)
public class TransactionAction implements Serializable {

    private static final long serialVersionUID = 8921236138362380586L;
    @Column(name = "is_create")
    private boolean isCreate;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;

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

    @Column(name = "category_id")
    private Long category_id;

    public TransactionAction(boolean isCreate, Date commitDate, Long user_id, Double amount, Date date, String note, Long category_id) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.user_id = user_id;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.category_id = category_id;
    }

    public TransactionAction(boolean isCreate, Date commitDate, Transaction transaction) {
        this.isCreate = isCreate;
        this.commitDate = commitDate;
        this.user_id = transaction.getUser_id();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.note = transaction.getNote();
        this.category_id = transaction.getCategory().getCategory_id();
    }

    public TransactionAction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionAction that = (TransactionAction) o;
        return isCreate == that.isCreate &&
                Objects.equals(commitDate, that.commitDate) &&
                Objects.equals(transaction_id, that.transaction_id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date) &&
                Objects.equals(note, that.note) &&
                Objects.equals(category_id, that.category_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCreate, commitDate, transaction_id, user_id, amount, date, note, category_id);
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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
