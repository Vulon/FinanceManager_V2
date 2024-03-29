package FinanceManager_V2.Interface_controllers.ListViewCellsControllers;

import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionListViewCellController{
    @FXML    private ImageView icon_view;
    @FXML    private Label category_label;
    @FXML    private Label amount_label;
    @FXML    private Label date_label;
    @FXML    private Label note_label;
    @FXML    private GridPane main_pane;
    @FXML    private BorderPane image_box;

    public static IconLoader _iconLoader;
    public static Lang _lang;

    public static void setStaticData(IconLoader iconLoader, Lang lang){
        _iconLoader = iconLoader;
        _lang = lang;
    }

    public TransactionListViewCellController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listCells/transactionCell.fxml"));
        loader.setController(this);
        try{
            loader.load();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void setContent(Transaction item){
        icon_view.setImage(
                _iconLoader.icons.get(item.getCategory().getIcon_id()));
        Color c = Color.web(item.getCategory().getColor());
        image_box.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
        category_label.setText(_lang.getTextLine(Lang.TextLine.category) + ": " + item.getCategory().getName());
        amount_label.setText(_lang.getTextLine(Lang.TextLine.amount_of_transaction) + ": " + item.getAmount().toString());
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        date_label.setText(dateFormat.format(item.getDate()));
        note_label.setText(item.getNote());
    }

    public GridPane getRoot(){
        return main_pane;
    }
}
