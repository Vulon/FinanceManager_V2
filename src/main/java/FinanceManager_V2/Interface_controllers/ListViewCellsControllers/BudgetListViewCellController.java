package FinanceManager_V2.Interface_controllers.ListViewCellsControllers;

import FinanceManager_V2.Database.Entity.Budget;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class BudgetListViewCellController {
    @FXML    GridPane main_pane;
    @FXML    Label name_label;
    @FXML    Label amount_label;
    @FXML    Label start_label;
    @FXML    Label notifylevel_label;
    @FXML    BorderPane image_box;
    @FXML    ImageView icon_view;
    @FXML    Label category_label;
    @FXML    Label end_label;
    @FXML    Label repeatable_label;

    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang){
        _iconLoader = iconLoader;
        _lang = lang;
    }

    public BudgetListViewCellController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listCells/budgetCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setContent(Budget budget, double sum){
        name_label.setText(_lang.getTextLine(Lang.TextLine.name) + ": " + budget.getName());
        amount_label.setText(_lang.getTextLine(Lang.TextLine.amount_of_budget) + ": " + sum + " / " + budget.getAmount());
        start_label.setText(_lang.getTextLine(Lang.TextLine.start_date) + ": " + budget.getStart().toString());
        end_label.setText(_lang.getTextLine(Lang.TextLine.end_date) + ": " + budget.getEnd().toString());
        notifylevel_label.setText(_lang.getTextLine(Lang.TextLine.notify_level) + ": " + budget.getNotifyLevel() * 100 + "%");
        if(budget.isRepeatable()){
            repeatable_label.setText(_lang.getTextLine(Lang.TextLine.repeatable_monthly));
        }else{
            repeatable_label.setText(_lang.getTextLine(Lang.TextLine.not_repeatable));
        }
        category_label.setText(_lang.getTextLine(Lang.TextLine.category) + ": " + budget.getCategory().getName());
        image_box.setBackground(new Background(new BackgroundFill(Color.web(budget.getCategory().getColor()), CornerRadii.EMPTY, Insets.EMPTY)));
        icon_view.setImage(_iconLoader.icons.get(budget.getCategory().getIcon_id()));
    }

    public GridPane getRoot(){
        return  main_pane;
    }
}
