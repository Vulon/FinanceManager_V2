package FinanceManager_V2.Interface_controllers.ListViewCells;

import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Services.IconLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionListViewCell extends ListCell<Transaction> {
    @FXML    private ImageView icon_view;
    @FXML    private Label category_label;
    @FXML    private Label amount_label;
    @FXML    private Label date_label;
    @FXML    private Label note_label;
    @FXML    private GridPane main_pane;

    private FXMLLoader loader;

    IconLoader iconLoader;

    public TransactionListViewCell(IconLoader iconLoader) {
        this.iconLoader = iconLoader;
    }

    @Override
    protected void updateItem(Transaction item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setText(null);
            setGraphic(null);
        }else{
            if(loader == null){
                loader = new FXMLLoader(getClass().getResource("/fxml/listCells/transactionCell.fxml"));
                loader.setController(this);
                try{
                    loader.load();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            icon_view.setImage(iconLoader.icons.get(item.getCategory().getIcon_id()));
            category_label.setText(item.getCategory().getName());
            amount_label.setText("Amount: " + item.getAmount().toString());
            DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
            date_label.setText(dateFormat.format(item.getDate()));
            note_label.setText(item.getNote());
            setGraphic(main_pane);
        }
    }
}
