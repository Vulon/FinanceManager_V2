package FinanceManager_V2.database.entity.database_pk;

import java.io.Serializable;
import java.util.Objects;

public class TransactionPK implements Serializable {
    public Long transaction_id;
    public Long user_id;

    public TransactionPK(Long transaction_id, Long user_id) {
        this.transaction_id = transaction_id;
        this.user_id = user_id;
    }

    public TransactionPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionPK that = (TransactionPK) o;
        return transaction_id.equals(that.transaction_id) &&
                user_id.equals(that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction_id, user_id);
    }
}
