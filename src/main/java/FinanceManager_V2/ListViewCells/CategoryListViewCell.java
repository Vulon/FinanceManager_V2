package FinanceManager_V2.ListViewCells;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Interface_controllers.ListViewCellsControllers.CategoryListViewCellController;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.scene.control.ListCell;

public class CategoryListViewCell extends ListCell<Category> {
    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang){
        _iconLoader = iconLoader;
        _lang = lang;
    }

    @Override
    protected void updateItem(Category item, boolean empty) {
        super.updateItem(item, empty);if(empty || item == null){
            setText(null);
            setGraphic(null);
        }else{
            CategoryListViewCellController.setStaticData(_iconLoader, _lang);
            CategoryListViewCellController cellController = new CategoryListViewCellController();
            cellController.setContent(item);
            setGraphic(cellController.getRoot());
        }

    }
}
