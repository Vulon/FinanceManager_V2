package FinanceManager_V2.ListViewCells;

import FinanceManager_V2.Database.Entity.Budget;
import FinanceManager_V2.Interface_controllers.ListViewCellsControllers.BudgetListViewCellController;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;

import java.util.List;

public class BudgetListViewCell extends ListCell<Budget> {
    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang, List<Double> transactionsSumList, ObservableList<Budget> budgetObservableList){
        _iconLoader = iconLoader;
        _lang = lang;
        sums = transactionsSumList;
        _budgetObservableList = budgetObservableList;
    }
    private static List<Double> sums;
    private static ObservableList<Budget> _budgetObservableList;


    protected void updateItem(Budget item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setText(null);
            setGraphic(null);
        }else{
            BudgetListViewCellController.setStaticData(_iconLoader, _lang);
            BudgetListViewCellController cellController = new BudgetListViewCellController();
            System.out.println("Sums list: " + sums);
            System.out.println("BudgetListViewCell DEBUG budgetObservableList.index: " + _budgetObservableList.indexOf(item) + " for item: "
            + item + " and sum: " + sums.get(_budgetObservableList.indexOf(item)));
            cellController.setContent(item,
                    sums.get(_budgetObservableList.indexOf(item)));
            setGraphic(cellController.getRoot());
        }
    }
}
