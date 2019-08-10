package FinanceManager_V2.TransportableDataObjects;


import FinanceManager_V2.Database.Entity.Action;
import FinanceManager_V2.Database.Entity.BudgetAction;
import FinanceManager_V2.Database.Entity.CategoryAction;
import FinanceManager_V2.Database.Entity.TransactionAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties
public class ActionQueue implements Serializable {

    private static final long serialVersionUID = 6073060088616647684L;

    private ArrayList<BudgetAction> budgetActions;
    private ArrayList<TransactionAction> transactionActions;
    private ArrayList<CategoryAction> categoryActions;

    public Queue getActionsQueue(){
        ArrayList<Action> temp = new ArrayList<>();
        temp.addAll(budgetActions);
        temp.addAll(transactionActions);
        temp.addAll(categoryActions);
        Collections.sort(temp);
        return new LinkedList<Action>(temp);
    }


    public ActionQueue() {
        budgetActions = new ArrayList<>();
        transactionActions = new ArrayList<>();
        categoryActions = new ArrayList<>();
    }

    public void clear(){
        budgetActions.clear();
        transactionActions.clear();
        categoryActions.clear();
    }

    public void addAll(ActionQueue queue){
        this.budgetActions.addAll(queue.getBudgetActions());
        this.categoryActions.addAll(queue.getCategoryActions());
        this.transactionActions.addAll(queue.getTransactionActions());
    }


    public ActionQueue(ArrayList<BudgetAction> budgetActions, ArrayList<TransactionAction> transactionActions, ArrayList<CategoryAction> categoryActions) {
        this.budgetActions = budgetActions;
        this.transactionActions = transactionActions;
        this.categoryActions = categoryActions;
    }

    public int getTotalSize(){
        return budgetActions.size() + categoryActions.size() + transactionActions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionQueue that = (ActionQueue) o;
        return Objects.equals(budgetActions, that.budgetActions) &&
                Objects.equals(transactionActions, that.transactionActions) &&
                Objects.equals(categoryActions, that.categoryActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetActions, transactionActions, categoryActions);
    }

    @Override
    public String toString() {
        return "ActionQueue{" +
                "budgetActions=" + budgetActions +
                ", transactionActions=" + transactionActions +
                ", categoryActions=" + categoryActions +
                '}';
    }

    public void addBudgetAction(BudgetAction budgetAction){
        budgetActions.add(budgetAction);
    }
    public void addCategoryAction(CategoryAction categoryAction){
        categoryActions.add(categoryAction);
    }
    public void addTransactionAction(TransactionAction transactionAction){
        transactionActions.add(transactionAction);
    }

    public ArrayList<BudgetAction> getBudgetActions() {
        return budgetActions;
    }

    public void setBudgetActions(ArrayList<BudgetAction> budgetActions) {
        this.budgetActions = budgetActions;
    }

    public ArrayList<TransactionAction> getTransactionActions() {
        return transactionActions;
    }

    public void setTransactionActions(ArrayList<TransactionAction> transactionActions) {
        this.transactionActions = transactionActions;
    }

    public ArrayList<CategoryAction> getCategoryActions() {
        return categoryActions;
    }

    public void setCategoryActions(ArrayList<CategoryAction> categoryActions) {
        this.categoryActions = categoryActions;
    }
}
