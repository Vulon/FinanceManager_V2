package FinanceManager_V2.ListViewCells;

import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Interface_controllers.ListViewCellsControllers.TransactionListViewCellController;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.scene.control.ListCell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionListViewCell  extends ListCell<Transaction> {
    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang){
        _iconLoader = iconLoader;
        _lang = lang;
    }

    @Override
    protected void updateItem(Transaction item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setText(null);
            setGraphic(null);
        }else{
            TransactionListViewCellController.setStaticData(_iconLoader, _lang);
            TransactionListViewCellController cellController = new TransactionListViewCellController();
            cellController.setContent(item);
            setGraphic(cellController.getRoot());
        }
    }
}
