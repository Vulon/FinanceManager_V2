package FinanceManager_V2.Database.Entity;

import FinanceManager_V2.Database.Entity.Database_pk.TransactionPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "transaction_action")
@IdClass(TransactionPK.class)
public class TransactionAction implements Serializable, Action {

    private static final long serialVersionUID = 8921236138362380586L;
    @Column(name = "create")
    private boolean create;

    @Column(name = "commit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "category")
    private Long category_id;

    private Long originalId;

    public TransactionAction(boolean isCreate, Date commitDate, Long user_id, Double amount, Date date, String note, Long category_id) {
        this.create = isCreate;
        this.commitDate = commitDate;
        this.user = user_id;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.category_id = category_id;
    }

    public TransactionAction(boolean isCreate, Date commitDate, Transaction transaction) {
        this.create = isCreate;
        this.transaction = transaction.getTransaction();
        this.commitDate = commitDate;
        this.user = transaction.getUser();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.note = transaction.getNote();
        this.category_id = transaction.getCategory().getCategory();
        this.originalId = transaction.getTransaction();
    }

    public TransactionAction() {
    }

    @Override
    public Long getOriginalId() {
        return originalId;
    }


    @Override
    public String toString() {
        return "TransactionAction{" +
                "create=" + create +
                ", commitDate=" + commitDate +
                ", transaction=" + transaction +
                ", user=" + user +
                ", amount=" + amount +
                ", date=" + date +
                ", note='" + note + '\'' +
                ", category_id=" + category_id +
                ", originalId=" + originalId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionAction that = (TransactionAction) o;
        return create == that.create &&
                Objects.equals(commitDate, that.commitDate) &&
                Objects.equals(transaction, that.transaction) &&
                Objects.equals(user, that.user) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date) &&
                Objects.equals(note, that.note) &&
                Objects.equals(category_id, that.category_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(create, commitDate, transaction, user, amount, date, note, category_id);
    }

    @Override
    public String getType() {
        return "transaction";
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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
