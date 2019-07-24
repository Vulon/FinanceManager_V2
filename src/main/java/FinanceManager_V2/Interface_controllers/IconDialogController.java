package FinanceManager_V2.Interface_controllers;


import FinanceManager_V2.Services.IconLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class IconDialogController {
    @FXML    GridPane main_pane;
    @FXML    Button ok_button;
    @FXML    Button cancel_button;
    @FXML    GridPane icon_pane;

    @FXML void handle_save(){
        mainController.setIcon(selectedId);
        main_pane.getScene().getWindow().hide();
    }

    @FXML void handle_cancel(){
        main_pane.getScene().getWindow().hide();
    }


    private static int COLUMNS_COUNT = 6;
    private IconLoader iconLoader;
    private ArrayList<Image> icons;
    private MainController mainController;

    public IconDialogController(IconLoader iconLoader) {
        this.iconLoader = iconLoader;
    }

    int selectedId = 0;


    public void manualSetUp(MainController mainController){
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
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ImageView source = (ImageView)event.getSource();
                        selectedId = icons.indexOf(source.getImage());
                        imageView.setStyle("-fx-background-color: red");
                    }
                });
                icon_pane.getChildren().add(imageView);
            }
        }
        icon_pane.getChildren().get(0).setStyle("-fx-background-color: red");
    }
}
