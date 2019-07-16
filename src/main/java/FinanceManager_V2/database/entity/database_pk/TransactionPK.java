package FinanceManager_V2.Database.Entity.Database_pk;

import java.io.Serializable;
import java.util.Objects;

public class TransactionPK implements Serializable {
    public Long transaction;
    public Long user;

    public TransactionPK(Long transaction_id, Long user_id) {
        this.transaction = transaction_id;
        this.user = user_id;
    }

    public TransactionPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionPK that = (TransactionPK) o;
        return transaction.equals(that.transaction) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, user);
    }
}
