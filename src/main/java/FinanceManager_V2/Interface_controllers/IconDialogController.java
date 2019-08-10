package FinanceManager_V2.Interface_controllers;


import FinanceManager_V2.Services.IconLoader;
import FinanceManager_V2.Services.Lang;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class IconDialogController {
    @FXML    BorderPane main_pane;
    @FXML    Button ok_button;
    @FXML    Button cancel_button;
    @FXML    GridPane icon_pane;

    @FXML void handle_save(){
        mainController.ca_setIcon(selectedId);
        main_pane.getScene().getWindow().hide();
    }

    @FXML void handle_cancel(){
        main_pane.getScene().getWindow().hide();
    }


    private static int COLUMNS_COUNT = 6;
    private ArrayList<Image> icons;
    private MainController mainController;
    private Lang lang;

    public IconDialogController(Lang lang) {
        this.lang = lang;
    }

    int selectedId = 0;


    public void manualSetUp(MainController mainController, IconLoader iconLoader){
        this.mainController = mainController;
        icon_pane.getChildren().clear();
        iconLoader.loadIcons();
        icons = iconLoader.icons;
        int rows_count = icons.size() / 6;
        rows_count += (icons.size() % rows_count) == 0 ? 0 : 1;
        for(int i = 0; i < rows_count; i++){
            for(int j = 0; j < COLUMNS_COUNT; j++){
                int currentId = i * COLUMNS_COUNT + j;
                if (currentId >= icons.size()){
                    break;
                }
                ImageView imageView = new ImageView();
                imageView.setImage(icons.get(currentId));
                BorderPane imageBox = new BorderPane();
                imageBox.setMinHeight(64);
                imageBox.setMinWidth(64);
                imageBox.setPrefSize(64, 64);
                imageBox.setCenter(imageView);
                imageBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        BorderPane prevBox = (BorderPane)icon_pane.getChildren().get(selectedId);
                        prevBox.setBackground(Background.EMPTY);
                        BorderPane source = (BorderPane)event.getSource();
                        ImageView imageNode= (ImageView)source.getCenter();
                        selectedId = icons.indexOf(imageNode.getImage());

                        source.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                });
                icon_pane.add(imageBox, j, i, 1, 1);
            }
        }
        ok_button.setText(lang.getTextLine(Lang.TextLine.save));
        cancel_button.setText(lang.getTextLine(Lang.TextLine.cancel));
    }
}
