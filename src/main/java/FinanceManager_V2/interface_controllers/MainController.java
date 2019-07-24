package FinanceManager_V2.Interface_controllers;


import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Database.Entity.TransactionAction;
import FinanceManager_V2.Database.Repositories.*;
import FinanceManager_V2.Events.DataChangedEvent;
import FinanceManager_V2.Services.AuthenticationService;
import FinanceManager_V2.Services.CachedActionsManager;
import FinanceManager_V2.Services.IconLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

@Component
public class MainController {
    //AutoWired:
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;
    private AuthenticationService authenticationService;
    private BudgetActionRepository budgetActionRepository;
    private TransactionActionRepository transactionActionRepository;
    private CategoryActionRepository categoryActionRepository;
    private CachedActionsManager cachedActionsManager;
    private IconLoader iconLoader;

    public MainController(TransactionRepository transactionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, AuthenticationService authenticationService, BudgetActionRepository budgetActionRepository, TransactionActionRepository transactionActionRepository, CategoryActionRepository categoryActionRepository, CachedActionsManager cachedActionsManager, IconLoader iconLoader) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.authenticationService = authenticationService;
        this.budgetActionRepository = budgetActionRepository;
        this.transactionActionRepository = transactionActionRepository;
        this.categoryActionRepository = categoryActionRepository;
        this.cachedActionsManager = cachedActionsManager;
        this.iconLoader = iconLoader;
    }

    @EventListener
    public void handleDataChanged(DataChangedEvent dataChangedEvent){
        if(dataChangedEvent.isCategoriesChanged()){
            tr_updateCategoriesList();
        }
        if(dataChangedEvent.isTransactionsChanged()){
            tr_updateTransactionsList();
        }
        if(dataChangedEvent.isBudgetsChanged()){

        }
    }



    //Main Control buttons
    @FXML    ToggleButton main_transactions;
    @FXML    ToggleButton main_categories;
    @FXML    ToggleButton main_budgets;
    @FXML    ToggleButton main_connection;
    @FXML    ToggleButton main_settings;
    @FXML    ProgressIndicator main_progressindicator;
    @FXML    public StackPane stack_pane;
    @FXML void handle_tab_selection(ActionEvent actionEvent){
        ToggleButton toggleButton = (ToggleButton)actionEvent.getSource();
        main_transactions.setSelected(false);
        main_categories.setSelected(false);
        main_budgets.setSelected(false);
        main_connection.setSelected(false);
        main_settings.setSelected(false);
        ca_pane.setVisible(false);
        tr_pane.setVisible(false);
        if(toggleButton.getId().equals(main_transactions.getId())){
            tr_pane.setVisible(true);
            main_transactions.setSelected(true);
        }else if(toggleButton.getId().equals(main_categories.getId())){
            ca_pane.setVisible(true);
            main_categories.setSelected(true);
        }else if(toggleButton.getId().equals(main_budgets.getId())){

        }else if(toggleButton.getId().equals(main_connection.getId())){

        }else if(toggleButton.getId().equals(main_settings.getId())){

        }
    }

    //Transactions tab --------------------------------------------------------------------------------------------
    @FXML    GridPane tr_pane;
    @FXML    ComboBox<Integer> tr_page;
    @FXML    ListView tr_list;
    @FXML    TextField tr_amount;
    @FXML    DatePicker tr_date;
    @FXML    TextArea tr_note;
    @FXML    ComboBox<Category> tr_category;
    //@FXML    Button tr_browse;
    @FXML    Button tr_save;
    @FXML    Button tr_delete;
    @FXML    Button tr_cancel;
    @FXML void tr_handle_cancel(){
        tr_amount.setText("");
        tr_date.setValue(LocalDate.now());
        tr_note.setText("");
        tr_category.getSelectionModel().selectFirst();
        tr_selected_transactionID = -1;
    }
    @FXML void tr_handle_save(){
        if(tr_selected_transactionID < 0){//create new
            Double amount = -1.0;
            try{
                amount = Double.parseDouble(tr_amount.getText());
            }catch (Exception e){
                //TODO send message about incorrect input
                return;
            }
            Instant instant = Instant.from(tr_date.getValue().atStartOfDay(ZoneId.systemDefault()));
            TransactionAction transactionAction = new TransactionAction(true, Date.from(Instant.now()), authenticationService.getUserId(),
                    amount, Date.from(instant), tr_note.getText(), ((Category)tr_category.getSelectionModel().getSelectedItem()).getCategory());
            cachedActionsManager.cacheAction(transactionAction, TransactionAction.class);
        }else{ // modify
            TransactionAction transactionAction = new TransactionAction(true, Date.from(Instant.now()), tr_transactionObservableList.get(tr_selected_transactionID));
            cachedActionsManager.cacheAction(transactionAction, TransactionAction.class);
        }

        tr_cancel.setVisible(false);
        tr_delete.setVisible(false);
        tr_handle_cancel();
        tr_selected_transactionID = -1;
    }
    @FXML void tr_handle_delete(){
        TransactionAction transactionAction = new TransactionAction(false, Date.from(Instant.now()), tr_transactionObservableList.get(tr_selected_transactionID));
        cachedActionsManager.cacheAction(transactionAction, TransactionAction.class);
        tr_cancel.setVisible(false);
        tr_delete.setVisible(false);
        tr_handle_cancel();
        tr_selected_transactionID = -1;
    }
    private void tr_updateCategoriesList(){
        tr_categoryObservableList.clear();
        ArrayList<Category> categories = categoryRepository.findAllByUser(authenticationService.getUserId());
        tr_categoryObservableList.addAll(categories);
        System.out.println("Found categories count: " + categories.size());
    }
    private void tr_updateTransactionsList(){
        Long value = tr_page.getSelectionModel().getSelectedIndex() * 10l + 10l;
        ArrayList<Transaction> newTransactions = transactionRepository.findFirstTransactions(authenticationService.getUserId(), value);
        while(newTransactions.size() > MAX_PAGE_TRANSACTIONS_COUNT){
            newTransactions.remove(0);
        }
        tr_transactionObservableList.clear();
        tr_transactionObservableList.addAll(newTransactions);
    }





    private ObservableList<Transaction> tr_transactionObservableList;
    private ObservableList<Category> tr_categoryObservableList;
    private int tr_selected_transactionID;



    //Categories tab  ----------------------------------------------------------------------------------
    @FXML    GridPane ca_pane;
    @FXML    RadioButton ca_list_income;
    @FXML    RadioButton ca_list_expense;
    @FXML    ListView ca_list;
    @FXML    TextField ca_name;
    @FXML    ColorPicker ca_colorpicker;
    @FXML    ChoiceBox ca_parent;
    @FXML    RadioButton ca_edit_income;
    @FXML    RadioButton ca_edit_expense;
    @FXML    ImageView ca_icon;
    @FXML    Button ca_browse;
    @FXML    Button ca_save;
    @FXML    Button ca_delete;
    @FXML    Button ca_cancel;


    private int tr_pagesCount;
    private static int MAX_PAGE_TRANSACTIONS_COUNT = 10;

    public void setIcon(int iconId){

    }

    public void manualSetUp(){
        iconLoader.loadIcons();
        tr_pane.setVisible(true);
        ca_pane.setVisible(false);
        main_transactions.setSelected(true);
        manualTransactionsSetUp();

    }
    public void manualTransactionsSetUp(){
        tr_selected_transactionID = -1;
        int transactionsCount = transactionRepository.countAllByUser(authenticationService.getUserId());

        tr_pagesCount = transactionsCount / MAX_PAGE_TRANSACTIONS_COUNT;
        tr_pagesCount += (tr_pagesCount % MAX_PAGE_TRANSACTIONS_COUNT == 0) ? 0 : 1;
        ObservableList<Integer> observableList = FXCollections.observableArrayList();
        for(int i = 0; i < tr_pagesCount; i++){
            observableList.add(i);
        }
        tr_page.setItems(observableList);
        tr_page.getSelectionModel().selectFirst();
        tr_page.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected new: " + newValue.toString());
            ArrayList<Transaction> newTransactions = transactionRepository.findFirstTransactions(authenticationService.getUserId(), newValue.longValue() * 10l + 10l);
            while(newTransactions.size() > MAX_PAGE_TRANSACTIONS_COUNT){
                newTransactions.remove(0);
            }
            tr_transactionObservableList.clear();
            tr_transactionObservableList.addAll(newTransactions);
        });

        tr_transactionObservableList = FXCollections.observableArrayList();
        tr_updateTransactionsList();
        tr_list.setItems(tr_transactionObservableList);
        tr_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Transaction transaction = (Transaction)newValue;
            tr_selected_transactionID = tr_transactionObservableList.indexOf(transaction);
            tr_cancel.setVisible(true);
            tr_delete.setVisible(true);
            tr_amount.setText(transaction.getAmount().toString());
            tr_date.setValue(transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            tr_note.setText(transaction.getNote());
            tr_category.getSelectionModel().select(transaction.getCategory());

        });
        tr_categoryObservableList = FXCollections.observableArrayList();
        tr_updateCategoriesList();
        tr_category.setItems(tr_categoryObservableList);



        tr_cancel.setVisible(false);
        tr_delete.setVisible(false);

    }
}
