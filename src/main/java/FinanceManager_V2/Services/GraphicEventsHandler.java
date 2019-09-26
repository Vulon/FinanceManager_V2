package FinanceManager_V2.Services;


import FinanceManager_V2.Events.*;
import FinanceManager_V2.Interface_controllers.MainController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class GraphicEventsHandler {
    private Date notifyDate;
    private MainController mainController;
    private Lang lang;
    public GraphicEventsHandler(Lang lang) {
        this.lang = lang;
    }

    public void setUPMainController(MainController mainController){
        this.mainController = mainController;
    }

    private void hideInfoLabel(){
        notifyDate = Date.from(Instant.now());
        Thread thread = new Thread(() -> {
            try{
                Thread.sleep(5000);
            }catch (Exception e){
                mainController.info_label.setVisible(false);
            }
            if (Date.from(Instant.now()).getTime() - notifyDate.getTime() >= 5000){
                mainController.info_label.setVisible(false);
            }
        });
        Platform.runLater(thread);
    }
    @EventListener
    public void handleServerConnectionProgressEvent(ServerConnectionProgressEvent event){
        System.out.println("MC: GraphicEventsHandler handle server connection progress event");
        mainController.main_progressindicator.setVisible(true);
        if(event.getProgress() < 0){
            mainController.main_progressindicator.setProgress(0);
            mainController.main_progressindicator.setVisible(false);
        }else if(event.getProgress() < 100){
            mainController.main_progressindicator.setProgress(event.getProgress());
        }else{
            mainController.main_progressindicator.setProgress(1);
            Thread thread = new Thread(() -> {
                try{
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    if(mainController.main_progressindicator.getProgress() >= 1){
                        mainController.main_progressindicator.setVisible(false);
                        System.out.println("Progress indicator should hide now");
                    }
                });
            });
            thread.start();
        }
    }

    @EventListener
    public void handleDataChanged(DataChangedEvent dataChangedEvent){
        System.out.println("Caught data changed event ");
        Platform.runLater(() -> {
            if(!mainController.setupCompleted){
                mainController.manualSetUp();
            }
            if(dataChangedEvent.isCategoriesChanged()){
                mainController.tr_updateCategoriesList();
                mainController.ca_updateCategoriesList();
                mainController.ca_updateParentList();
            }
            if(dataChangedEvent.isTransactionsChanged()){
                mainController.tr_updateTransactionsList();
                mainController.bu_updateBudgetsList();
            }
            if(dataChangedEvent.isBudgetsChanged()){
                mainController.bu_updateBudgetsList();
            }
        });
    }

    @EventListener
    public void handleServerErrorEvent(ServerErrorEvent errorEvent){
        if(errorEvent.equals(AuthenticationService.ServerResponseCode.CONNECTION_TIMEOUT)
                || errorEvent.equals(AuthenticationService.ServerResponseCode.INTERNAL_SERVER_ERROR)
                || errorEvent.equals(AuthenticationService.ServerResponseCode.UNKNOWN)){
            mainController.info_label.setText(lang.getTextLine(Lang.TextLine.server_error));
        }
        hideInfoLabel();
    }
    @EventListener
    public void handleDatabaseUpdateException(DatabaseModifyEvent event){
        mainController.info_label.setText(lang.getTextLine(Lang.TextLine.database_update_exception));
        hideInfoLabel();
    }
    @EventListener
    public void handleIncorrectInputEvent(IncorrectInputEvent event){
        mainController.info_label.setText(event.getField() + " " + event.getAdvice());
        hideInfoLabel();
    }
}
