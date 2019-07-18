package FinanceManager_V2.Database.Entity;

import java.util.Date;


public interface Action extends Comparable{

    @Override
    default public int compareTo(Object o) {
        Action other = (Action)o;
        return Long.compare(this.getCommitDate().getTime(), other.getCommitDate().getTime());
    }

    public abstract Date getCommitDate();

    public abstract void setCommitDate(Date commitDate);

    public abstract String getType();

    public abstract boolean isCreate();

    public abstract void setCreate(boolean create);
}
