package FinanceManager_V2.Interface_controllers.ListViewCellsControllers;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CategoryListViewCellController {
    @FXML    GridPane main_pane;
    @FXML    BorderPane image_box;
    @FXML    ImageView icon_view;
    @FXML    Label name_label;
    @FXML    Label income_label;
    @FXML    BorderPane parent_box;
    @FXML    ImageView parent_view;
    @FXML    Label parent_label;

    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang){
        _iconLoader = iconLoader;
        _lang = lang;
    }

    public CategoryListViewCellController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listCells/categoryListCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setContent(Category item){
        icon_view.setImage(
                _iconLoader.icons.get(item.getIcon_id()));
        Color selfColor = Color.web(item.getColor());
        image_box.setBackground(new Background(new BackgroundFill(selfColor, CornerRadii.EMPTY, Insets.EMPTY)));
        name_label.setText(_lang.getTextLine(Lang.TextLine.name) + ": " + item.getName());
        if(item.isIncome()){
            income_label.setText(_lang.getTextLine(Lang.TextLine.type) + ": " +_lang.getTextLine(Lang.TextLine.income));
        }else{
            income_label.setText(_lang.getTextLine(Lang.TextLine.type) + ": " +_lang.getTextLine(Lang.TextLine.expense));
        }
        if(item.getParent() == null){
            parent_box.setBackground(new Background(new BackgroundFill(selfColor, CornerRadii.EMPTY, Insets.EMPTY)));
            parent_label.setText(_lang.getTextLine(Lang.TextLine.parent) + ": " + item.getName());
            parent_view.setImage( _iconLoader.icons.get(item.getIcon_id()));
        }else{
            Category parent = item.getParent();
            Color parentColor = Color.web(parent.getColor());
            parent_box.setBackground(new Background(new BackgroundFill(parentColor, CornerRadii.EMPTY, Insets.EMPTY)));
            parent_label.setText(_lang.getTextLine(Lang.TextLine.parent) + ": " + item.getName());
            parent_view.setImage(_iconLoader.icons.get(parent.getIcon_id()));
        }
    }
    public GridPane getRoot(){
        return main_pane;
    }
}
