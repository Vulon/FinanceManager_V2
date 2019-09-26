package FinanceManager_V2.Interface_controllers;


import FinanceManager_V2.Database.Entity.*;
import FinanceManager_V2.Database.Repositories.*;
import FinanceManager_V2.Events.*;
import FinanceManager_V2.Interface_controllers.ListViewCellsControllers.TransactionListViewCellController;
import FinanceManager_V2.ListViewCells.BudgetListViewCell;
import FinanceManager_V2.ListViewCells.CategoryListViewCell;
import FinanceManager_V2.ListViewCells.TransactionListViewCell;
import FinanceManager_V2.MainApplication;
import FinanceManager_V2.Services.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    private EventsPublisher eventsPublisher;
    public IconLoader iconLoader;
    public Lang lang;

    private GraphicEventsHandler graphicEventsHandler;



    private MainApplication mainApplication;

    public MainController(TransactionRepository transactionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, AuthenticationService authenticationService, BudgetActionRepository budgetActionRepository, TransactionActionRepository transactionActionRepository, CategoryActionRepository categoryActionRepository, CachedActionsManager cachedActionsManager, IconLoader iconLoader, Lang lang, EventsPublisher eventsPublisher, GraphicEventsHandler graphicEventsHandler) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.authenticationService = authenticationService;
        this.budgetActionRepository = budgetActionRepository;
        this.transactionActionRepository = transactionActionRepository;
        this.categoryActionRepository = categoryActionRepository;
        this.cachedActionsManager = cachedActionsManager;
        this.iconLoader = iconLoader;
        this.lang = lang;
        this.eventsPublisher = eventsPublisher;
        this.graphicEventsHandler = graphicEventsHandler;
        graphicEventsHandler.setUPMainController(this);
    }


    public boolean setupCompleted  = false;
    //Main Control buttons
    @FXML    private ToggleButton main_transactions;
    @FXML    private ToggleButton main_categories;
    @FXML    private ToggleButton main_budgets;
    @FXML    private ToggleButton main_connection;
    @FXML    private ToggleButton main_settings;
    @FXML    public ProgressIndicator main_progressindicator;
    @FXML    public StackPane stack_pane;
    @FXML    public Label info_label;
    @FXML void handle_tab_selection(ActionEvent actionEvent){
        ToggleButton toggleButton = (ToggleButton)actionEvent.getSource();
        main_transactions.setSelected(false);
        main_categories.setSelected(false);
        main_budgets.setSelected(false);
        main_connection.setSelected(false);
        main_settings.setSelected(false);
        ca_pane.setVisible(false);
        tr_pane.setVisible(false);
        bu_pane.setVisible(false);
        se_pane.setVisible(false);
        if(toggleButton.getId().equals(main_transactions.getId())){
            tr_pane.setVisible(true);
            main_transactions.setSelected(true);
        }else if(toggleButton.getId().equals(main_categories.getId())){
            ca_pane.setVisible(true);
            main_categories.setSelected(true);
        }else if(toggleButton.getId().equals(main_budgets.getId())){
            bu_pane.setVisible(true);
            main_budgets.setSelected(true);
        }else if(toggleButton.getId().equals(main_connection.getId())){

        }else if(toggleButton.getId().equals(main_settings.getId())){
            se_pane.setVisible(true);
            main_settings.setSelected(true);
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
    @FXML    Button tr_save;
    @FXML    Button tr_delete;
    @FXML    Button tr_cancel;
    @FXML    CheckBox tr_repeatable;

    @FXML void tr_handle_cancel(){
        tr_amount.setText("");
        tr_date.setValue(LocalDate.now());
        tr_note.setText("");
        tr_category.getSelectionModel().selectFirst();
        tr_repeatable.disarm();
        tr_selected_transactionID = -1;
    }
    @FXML void tr_handle_save(){
        Double amount;
        try{
            amount = Double.parseDouble(tr_amount.getText());
        }catch (Exception e){
            eventsPublisher.publishIncorrectInputEvent("", lang.getTextLine(Lang.TextLine.amount_advice));
            return;
        }
        if(tr_selected_transactionID < 0){//create new

            Instant instant = Instant.from(tr_date.getValue().atStartOfDay(ZoneId.systemDefault()));
            TransactionAction transactionAction = new TransactionAction(true, Date.from(Instant.now()), authenticationService.getUserId(),
                    amount, Date.from(instant), tr_note.getText(), ((Category)tr_category.getSelectionModel().getSelectedItem()).getCategory(),0l,
                    tr_repeatable.isArmed());
            cachedActionsManager.cacheAction(transactionAction, TransactionAction.class);
        }else{ // modify
            TransactionAction deleteAction = new TransactionAction(false, Date.from(Instant.now()), tr_transactionObservableList.get(tr_selected_transactionID));
            cachedActionsManager.cacheAction(deleteAction, TransactionAction.class);
            TransactionAction createAction =  new TransactionAction(true, Date.from(Instant.now().plusSeconds(1)), authenticationService.getUserId(),
                    amount, Date.from(Instant.now()), tr_note.getText(), ((Category)tr_category.getSelectionModel().getSelectedItem()).getCategory(),0l,
                    tr_repeatable.isArmed());
            cachedActionsManager.cacheAction(createAction, TransactionAction.class);
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

    private int tr_pagesCount;
    private static int MAX_PAGE_TRANSACTIONS_COUNT = 10;

    public void tr_updateCategoriesList(){
        tr_categoryObservableList.clear();
        ArrayList<Category> categories = categoryRepository.findAllByUser(authenticationService.getUserId());
        tr_categoryObservableList.addAll(categories);
        System.out.println("Found categories count: " + categories.size());
    }
    public void tr_updateTransactionsList(){
        Pageable pageable = PageRequest.of(tr_page.getSelectionModel().getSelectedIndex(), MAX_PAGE_TRANSACTIONS_COUNT);
        List<Transaction> newTransactions = transactionRepository.findAllByUserOrderByDateDesc(authenticationService.getUserId(), pageable);
        System.out.println("Found transactions: " + newTransactions);
        tr_transactionObservableList.setAll(newTransactions);
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
    @FXML    ComboBox<Category> ca_parent;
    @FXML    RadioButton ca_edit_income;
    @FXML    RadioButton ca_edit_expense;
    @FXML    ImageView ca_icon;
    @FXML    Button ca_browse;
    @FXML    Button ca_save;
    @FXML    Button ca_delete;
    @FXML    Button ca_cancel;

    private int ca_selected_categoryID;

    public void ca_setIcon(int iconId){
        ca_icon.setImage(iconLoader.icons.get(iconId));
    }
    @FXML
    void ca_handle_radio_list_toggle(){
        ca_updateCategoriesList();
        System.out.println("Category list items " + ca_list.getItems());
    }

    @FXML
    void ca_handle_radio_edit_toggle(){
        ca_updateParentList();
    }

    @FXML
    void ca_handle_browse(){
        mainApplication.startNewScene(IconDialogController.class);
    }

    @FXML
    void ca_handle_save(){
        System.out.println("color: " + ca_colorpicker.getValue().toString());
        System.out.println("Category radio button armed? " + ca_edit_income.isArmed());
        if(ca_parent.getSelectionModel().getSelectedIndex() < 0){
            eventsPublisher.publishIncorrectInputEvent("", lang.getTextLine(Lang.TextLine.parent_advice));
            return;
        }
        if(ca_name.getText().length() < 1){
            eventsPublisher.publishIncorrectInputEvent(lang.getTextLine(Lang.TextLine.name), lang.getTextLine(Lang.TextLine.name_advice));
            return;
        }
        if(ca_icon.getImage() == null){
            eventsPublisher.publishIncorrectInputEvent("", lang.getTextLine(Lang.TextLine.icon_advice));
            return;
        }
        if(ca_selected_categoryID < 0){ //Create new
            ca_categoriesObservableList.forEach(category -> {
                if(category.getName().equals(ca_name.getText())){
                    eventsPublisher.publishIncorrectInputEvent(lang.getTextLine(Lang.TextLine.name), lang.getTextLine(Lang.TextLine.name_advice));
                    return;
                }
            });
            CategoryAction action = new CategoryAction(true, Date.from(Instant.now()), authenticationService.getUserId(),
                    ca_colorpicker.getValue().toString(), ca_name.getText(), iconLoader.indexOf(ca_icon.getImage()), ca_edit_income.isArmed(),
                    ca_parent.getValue().getCategory());
            cachedActionsManager.cacheAction(action, CategoryAction.class);
        }else{ //Modify last
            ca_categoriesObservableList.forEach(category -> {
                if(category.getName().equals(ca_name.getText()) && ca_categoriesObservableList.indexOf(category) != ca_selected_categoryID){
                    eventsPublisher.publishIncorrectInputEvent(lang.getTextLine(Lang.TextLine.name), lang.getTextLine(Lang.TextLine.name_advice));
                    return;
                }
            });
            Category category = ca_categoriesObservableList.get(ca_selected_categoryID);
            CategoryAction deleteAction = new CategoryAction(false, Date.from(Instant.now()), category);
            CategoryAction createAction = new CategoryAction(true, Date.from(Instant.now().plusSeconds(1)), authenticationService.getUserId(),
                    ca_colorpicker.getValue().toString(), ca_name.getText(), iconLoader.indexOf(ca_icon.getImage()), ca_edit_income.isArmed(),
                    ca_parent.getValue().getCategory());
            cachedActionsManager.cacheAction(deleteAction, CategoryAction.class);
            cachedActionsManager.cacheAction(createAction, CategoryAction.class);
        }
        ca_delete.setVisible(false);
        ca_handle_cancel();
    }

    @FXML
    void ca_handle_delete(){
        Category category = ca_categoriesObservableList.get(ca_selected_categoryID);
        CategoryAction deleteAction = new CategoryAction(false, Date.from(Instant.now()), category);
        cachedActionsManager.cacheAction(deleteAction, CategoryAction.class);
        ca_delete.setVisible(false);
        ca_save.setVisible(false);
        ca_handle_cancel();
    }

    @FXML
    void ca_handle_cancel(){
        ca_selected_categoryID = -1;
        ca_name.setText("");
        ca_colorpicker.setValue(Color.WHITE);
        ca_edit_expense.fire();
        ca_parent.getSelectionModel().selectFirst();
        ca_icon.setImage(null);
        ca_save.setVisible(true);
        ca_parent.getSelectionModel().selectFirst();
        ca_icon.setImage(iconLoader.icons.get(0));
        ca_save.setVisible(true);
        ca_delete.setVisible(false);
        ca_cancel.setVisible(true);
    }

    public void ca_updateCategoriesList(){
        ca_categoriesObservableList.clear();
        tr_categoryObservableList.forEach(category -> {
            if(category.isIncome() == ca_list_income.isArmed()){
                ca_categoriesObservableList.add(category);
            }
        });
        System.out.println("Found categories: " + ca_categoriesObservableList);
    }

    public void ca_updateParentList(){
        Category selected;
        if(ca_selected_categoryID < 0){
            selected = null;
        }else {
            selected = ca_categoriesObservableList.get(ca_selected_categoryID);
        }
        ca_parentObservableList.clear();
        tr_categoryObservableList.forEach(category -> {
            if(category.isIncome() == ca_edit_income.isArmed() && !category.equals(selected)){
                ca_parentObservableList.add(category);
            }
        });

    }

    private ObservableList<Category> ca_categoriesObservableList;
    private ObservableList<Category> ca_parentObservableList;


    //Budget tab ----------------------------------------------------------
    @FXML    GridPane bu_pane;
    @FXML    ListView bu_list;
    @FXML    TextField bu_name;
    @FXML    TextField bu_amount;
    @FXML    DatePicker bu_start;
    @FXML    DatePicker bu_end;
    @FXML    ComboBox<Category> bu_category;
    @FXML    ComboBox<Double> bu_notifyLevel;
    @FXML    CheckBox bu_repeatable;
    @FXML    Button bu_save;
    @FXML    Button bu_delete;
    @FXML    Button bu_cancel;

    private int bu_selectedBudgetId = -1;
    private ArrayList<Double> bu_sumsList;
    private ObservableList<Budget> bu_budgetsObservableList;
    private ObservableList<Double> bu_notifyObservableList;

    public void bu_updateBudgetsList(){

        ArrayList<Budget> tempList = new ArrayList<>();
        tempList.addAll(budgetRepository.getAllByUser(authenticationService.getUserId()));
        ArrayList<Transaction> transactions = transactionRepository.findAllByUserOrderByDateDesc(authenticationService.getUserId());
        bu_sumsList.clear();
        for(Budget b : tempList){
            double sum = 0;
            for(Transaction t : transactions){
                boolean condition1 = false;
                Category category = t.getCategory();
                while(category != null){
                    if(b.getCategory().getCategory().equals(category.getCategory())){
                        condition1 = true;
                        break;
                    }
                    category = category.getParent();
                }
                if(condition1){
                    Calendar thisMonthDate = GregorianCalendar.getInstance();
                    Calendar now = GregorianCalendar.getInstance();
                    thisMonthDate.setTime(t.getDate());
                    thisMonthDate.set(Calendar.MONTH, now.get(Calendar.MONTH));
                    thisMonthDate.set(Calendar.YEAR, now.get(Calendar.YEAR));
                    if(t.getDate().getTime() >= b.getStart().getTime() && t.getDate().getTime() <= b.getEnd().getTime()){
                        sum += t.getAmount();
                    }else if(b.isRepeatable() && t.getDate().getTime() >= b.getStart().getTime()){
                        sum += t.getAmount();
                    }else if(t.isRepeatable() && thisMonthDate.getTimeInMillis() >= b.getStart().getTime() && thisMonthDate.getTimeInMillis() <= b.getEnd().getTime()){
                        sum += t.getAmount();
                    }

                }
            }
            bu_sumsList.add(sum);
        }
        bu_budgetsObservableList.setAll(tempList);
    }

    @FXML    private void bu_handle_cancel(){
        bu_selectedBudgetId = -1;
        bu_name.setText("");
        bu_amount.setText("0");
        bu_start.setValue(LocalDate.now());
        bu_end.setValue(LocalDate.now());
        bu_category.getSelectionModel().selectFirst();
        bu_notifyLevel.getSelectionModel().selectFirst();
        bu_repeatable.setSelected(false);
        bu_delete.setVisible(false);

    }
    @FXML    private void bu_handle_save(){
        if(bu_name.getText().length() < 1){
            eventsPublisher.publishIncorrectInputEvent(lang.getTextLine(Lang.TextLine.name), lang.getTextLine(Lang.TextLine.name_advice));
            return;
        }
        Double amount;
        try{
            amount = Double.parseDouble(bu_amount.getText());
        }catch (Exception e){
            eventsPublisher.publishIncorrectInputEvent("", lang.getTextLine(Lang.TextLine.amount_advice));
            return;
        }
        if(bu_start.getValue() == null || bu_end.getValue() == null || bu_end.getValue().toEpochDay() < bu_start.getValue().toEpochDay()){
            eventsPublisher.publishIncorrectInputEvent("", lang.getTextLine(Lang.TextLine.budget_dates_advice));
            return;
        }
        if(bu_repeatable.isSelected()){
            System.out.println("BUDGET REPEATABLE ARMED");
        }
        if(bu_selectedBudgetId < 0){ //save
            Instant startInstant = Instant.from(bu_start.getValue().atStartOfDay(ZoneId.systemDefault()));
            Instant endInstant = Instant.from(bu_end.getValue().atStartOfDay(ZoneId.systemDefault()));
            BudgetAction createAction = new BudgetAction(true, Date.from(Instant.now()), authenticationService.getUserId()
            , bu_name.getText(), amount, Date.from(startInstant), Date.from(endInstant), bu_notifyLevel.getSelectionModel().getSelectedItem(),
                    bu_category.getSelectionModel().getSelectedItem().getCategory(), bu_repeatable.isSelected());
            cachedActionsManager.cacheAction(createAction, BudgetAction.class);
        }else{ //modify
            Budget budget = bu_budgetsObservableList.get(bu_selectedBudgetId);
            BudgetAction deleteAction = new BudgetAction(false, Date.from(Instant.now()), budget);
            cachedActionsManager.cacheAction(deleteAction, BudgetAction.class);
            Instant startInstant = Instant.from(bu_start.getValue().atStartOfDay(ZoneId.systemDefault()));
            Instant endInstant = Instant.from(bu_end.getValue().atStartOfDay(ZoneId.systemDefault()));
            BudgetAction createAction = new BudgetAction(true, Date.from(Instant.now().plusSeconds(1)), authenticationService.getUserId()
                    , bu_name.getText(), amount, Date.from(startInstant), Date.from(endInstant), bu_notifyLevel.getSelectionModel().getSelectedItem(),
                    bu_category.getSelectionModel().getSelectedItem().getCategory(), bu_repeatable.isSelected());
            cachedActionsManager.cacheAction(createAction, BudgetAction.class);
        }
    }
    @FXML    private void bu_handle_delete(){
        Budget budget = bu_budgetsObservableList.get(bu_selectedBudgetId);
        BudgetAction deleteAction = new BudgetAction(false, Date.from(Instant.now()), budget);
        cachedActionsManager.cacheAction(deleteAction, BudgetAction.class);
        bu_handle_cancel();
    }

    @FXML    private void bu_handle_repeatableCheck(){

        if(bu_repeatable.isSelected()){
            System.out.println("Handle repeatable check: selected");
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            bu_start.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
            bu_end.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }else{
            System.out.println("Handle repeatable check: not selected");
        }
    }


    @FXML
    private GridPane se_pane;
    @FXML
    private ComboBox<Lang.Language> se_lang_box;
    private ObservableList<Lang.Language> se_observableLangList;


    public void setUpMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    public void setUpLanguage(){
        main_transactions.setText(lang.getTextLine(Lang.TextLine.transactions));
        main_categories.setText(lang.getTextLine(Lang.TextLine.categories));
        main_budgets.setText(lang.getTextLine(Lang.TextLine.budgets));
        main_connection.setText(lang.getTextLine(Lang.TextLine.logger));
        main_settings.setText(lang.getTextLine(Lang.TextLine.settings));
        tr_save.setText(lang.getTextLine(Lang.TextLine.save));
        tr_delete.setText(lang.getTextLine(Lang.TextLine.delete));
        tr_cancel.setText(lang.getTextLine(Lang.TextLine.cancel));
        ca_save.setText(lang.getTextLine(Lang.TextLine.save));
        ca_delete.setText(lang.getTextLine(Lang.TextLine.delete));
        ca_cancel.setText(lang.getTextLine(Lang.TextLine.cancel));
        bu_save.setText(lang.getTextLine(Lang.TextLine.save));
        bu_delete.setText(lang.getTextLine(Lang.TextLine.delete));
        bu_cancel.setText(lang.getTextLine(Lang.TextLine.cancel));
        tr_page.setPromptText(lang.getTextLine(Lang.TextLine.page));
        tr_amount.setPromptText(lang.getTextLine(Lang.TextLine.amount_of_transaction));
        tr_date.setPromptText(lang.getTextLine(Lang.TextLine.date_of_transaction));
        tr_repeatable.setText(lang.getTextLine(Lang.TextLine.repeatable_monthly));
        tr_note.setPromptText(lang.getTextLine(Lang.TextLine.note));
        ca_list_income.setText(lang.getTextLine(Lang.TextLine.income));
        ca_list_expense.setText(lang.getTextLine(Lang.TextLine.expense));
        ca_edit_income.setText(lang.getTextLine(Lang.TextLine.income));
        ca_edit_expense.setText(lang.getTextLine(Lang.TextLine.expense));
        ca_name.setPromptText(lang.getTextLine(Lang.TextLine.name));
        ca_parent.setPromptText(lang.getTextLine(Lang.TextLine.parent));
        bu_name.setPromptText(lang.getTextLine(Lang.TextLine.name));
        bu_amount.setPromptText(lang.getTextLine(Lang.TextLine.amount_of_budget));
        bu_start.setPromptText(lang.getTextLine(Lang.TextLine.start_date));
        bu_end.setPromptText(lang.getTextLine(Lang.TextLine.end_date));
        bu_category.setPromptText(lang.getTextLine(Lang.TextLine.categories));
        bu_notifyLevel.setPromptText(lang.getTextLine(Lang.TextLine.notify_level));
        bu_repeatable.setText(lang.getTextLine(Lang.TextLine.repeatable_monthly));

    }

    public synchronized void manualSetUp(){
        if(!setupCompleted){
            System.out.println("FIRST SETUP INITIATED");
            iconLoader.loadIcons();
            manualTransactionsSetUp();
            manualCategoriesSetUp();
            manualBudgetsSetUp();
            manualSettingsSetUp();
            main_transactions.fire();
            setUpLanguage();

            setupCompleted = true;
        }
    }

    private void manualSettingsSetUp(){
        se_observableLangList = FXCollections.observableArrayList();
        se_observableLangList.addAll(Lang.Language.values());
        se_lang_box.setItems(se_observableLangList);
        se_lang_box.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lang.setLanguage(newValue);
            setUpLanguage();
        });
    }
    private void manualTransactionsSetUp(){
        System.out.println("TRANSACTION SETUP INITIATED");
        tr_selected_transactionID = -1;
        int transactionsCount = transactionRepository.countAllByUser(authenticationService.getUserId());
        tr_pagesCount = transactionsCount / MAX_PAGE_TRANSACTIONS_COUNT;
        tr_pagesCount += (tr_pagesCount % MAX_PAGE_TRANSACTIONS_COUNT == 0) ? 0 : 1;
        ObservableList<Integer> pageObservableList = FXCollections.observableArrayList();
        for(int i = 0; i < tr_pagesCount; i++){
            pageObservableList.add(i);
        }
        if(tr_pagesCount < 1){
            pageObservableList.add(0);
        }
        tr_page.setItems(pageObservableList);

        tr_page.getSelectionModel().selectFirst();
        tr_page.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected new: " + newValue.toString());
            Pageable pageable = PageRequest.of(0, MAX_PAGE_TRANSACTIONS_COUNT);
            List<Transaction> newTransactions = transactionRepository.findAllByUserOrderByDateDesc(authenticationService.getUserId(), pageable);
            tr_transactionObservableList.clear();
            tr_transactionObservableList.addAll(newTransactions);
        });

        tr_transactionObservableList = FXCollections.observableArrayList();
        tr_updateTransactionsList();
        TransactionListViewCellController._iconLoader = iconLoader;

        TransactionListViewCell.setStaticData(iconLoader, lang);
        tr_list.setCellFactory(param -> new TransactionListViewCell());
        tr_list.setItems(tr_transactionObservableList);
        tr_list.setOnMouseClicked(event -> {
            Transaction transaction = (Transaction)tr_list.getSelectionModel().getSelectedItem();
            System.out.println("Selected " + transaction);
            tr_selected_transactionID = tr_transactionObservableList.indexOf(transaction);

            if(transaction == null || tr_selected_transactionID < 0 ){
                tr_handle_cancel();
            }else{
                tr_amount.setText(transaction.getAmount().toString());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(transaction.getDate());
                tr_date.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                tr_note.setText(transaction.getNote());
                tr_category.getSelectionModel().select(transaction.getCategory());
                if(transaction.isRepeatable()){
                    tr_repeatable.fire();
                }
            }
            tr_cancel.setVisible(true);
            tr_delete.setVisible(true);
        });

        tr_categoryObservableList = FXCollections.observableArrayList();
        tr_updateCategoriesList();

        Callback<ListView<Category>, ListCell<Category>> cellFactory = createCategoryComboBoxCellFactory();

        tr_category.setCellFactory(cellFactory);
        tr_category.setButtonCell(cellFactory.call(null));
        tr_category.setItems(tr_categoryObservableList);

        tr_cancel.setVisible(false);
        tr_delete.setVisible(false);
        tr_category.getSelectionModel().selectFirst();
        tr_handle_cancel();

    }

    private void manualCategoriesSetUp(){
        System.out.println("CATEGORIES SETUP INITIATED");
        ca_selected_categoryID = -1;
        ca_categoriesObservableList = FXCollections.observableArrayList();
        ca_parentObservableList = FXCollections.observableArrayList();
        ca_updateCategoriesList();
        ca_updateParentList();
        ToggleGroup listToggleGroup = new ToggleGroup();
        ToggleGroup editToggleGroup = new ToggleGroup();
        ca_list_income.setToggleGroup(listToggleGroup);
        ca_list_expense.setToggleGroup(listToggleGroup);

        ca_edit_income.setToggleGroup(editToggleGroup);
        ca_edit_expense.setToggleGroup(editToggleGroup);

        ca_list_expense.fire();
        ca_edit_expense.fire();


        CategoryListViewCell.setStaticData(iconLoader, lang);
        ca_list.setCellFactory(param -> new CategoryListViewCell());
        ca_list.setItems(ca_categoriesObservableList);
        ca_list.setOnMouseClicked(event -> {
            Category category = (Category)ca_list.getSelectionModel().getSelectedItem();
            ca_selected_categoryID = ca_categoriesObservableList.indexOf(category);
            System.out.println("Selected " + category + " with index " + ca_selected_categoryID);
            if(category == null || ca_selected_categoryID < 0){
                ca_handle_cancel();
            }else if(category.getParent() == null){
                ca_name.setText(category.getName());
                ca_colorpicker.setValue(Color.web(category.getColor()));
                if(category.isIncome()){
                    ca_edit_income.fire();
                }else{
                    ca_edit_expense.fire();
                }
                ca_parent.getSelectionModel().clearSelection();
                ca_save.setVisible(false);
                ca_delete.setVisible(false);
                ca_cancel.setVisible(true);
            }else{
                ca_name.setText(category.getName());
                ca_colorpicker.setValue(Color.web(category.getColor()));
                if(category.isIncome()){
                    ca_edit_income.fire();
                }else{
                    ca_edit_expense.fire();
                }

                ca_parent.getSelectionModel().select(ca_parentObservableList.indexOf(category.getParent()));
                ca_icon.setImage(iconLoader.icons.get(category.getIcon_id()));
                ca_save.setVisible(true);
                ca_delete.setVisible(true);
                ca_cancel.setVisible(true);
            }
        });
        ca_parent.setItems(ca_parentObservableList);

        Callback<ListView<Category>, ListCell<Category>> parentCellFactory = createCategoryComboBoxCellFactory();
        ca_parent.setCellFactory(parentCellFactory);
        ca_parent.setButtonCell(parentCellFactory.call(null));
        ca_handle_cancel();
    }

    private void manualBudgetsSetUp(){
        bu_budgetsObservableList = FXCollections.observableArrayList();
        bu_sumsList = new ArrayList<>();
        bu_notifyObservableList = FXCollections.observableArrayList();
        bu_updateBudgetsList();

        bu_list.setItems(bu_budgetsObservableList);
        BudgetListViewCell.setStaticData(iconLoader, lang, bu_sumsList,bu_budgetsObservableList);
        bu_list.setCellFactory(param -> new BudgetListViewCell());
        bu_list.setOnMouseClicked(event -> {
            Budget budget = (Budget)bu_list.getSelectionModel().getSelectedItem();
            bu_selectedBudgetId = bu_budgetsObservableList.indexOf(budget);
            System.out.println("Selected " + budget + " with index " + bu_selectedBudgetId);
            if(bu_selectedBudgetId < 0 || budget == null){
                bu_handle_cancel();
            }else{
                bu_save.setVisible(true);
                bu_delete.setVisible(true);
                bu_cancel.setVisible(true);
                bu_name.setText(budget.getName());
                bu_amount.setText(budget.getAmount().toString());
                if(budget.isRepeatable()){
                    bu_repeatable.setSelected(true);
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    bu_start.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
                    bu_end.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }else{
                    bu_repeatable.setSelected(false);
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(budget.getStart());
                    bu_start.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    calendar.setTime(budget.getEnd());
                    bu_end.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                Category category = budget.getCategory();
                category = categoryRepository.getByUserAndCategory(authenticationService.getUserId(), category.getCategory());
                bu_category.getSelectionModel().select(category);
                bu_notifyLevel.getSelectionModel().select(budget.getNotifyLevel());
            }
        });
        Callback<ListView<Category>, ListCell<Category>> cellFactory = createCategoryComboBoxCellFactory();
        bu_category.setCellFactory(cellFactory);
        bu_category.setButtonCell(cellFactory.call(null));
        bu_category.setItems(tr_categoryObservableList);
        bu_notifyObservableList.add(1.0);
        bu_notifyObservableList.add(0.9);
        bu_notifyObservableList.add(0.8);
        bu_notifyObservableList.add(0.75);
        bu_notifyObservableList.add(0.5);
        bu_notifyObservableList.add(0.25);
        bu_notifyLevel.setItems(bu_notifyObservableList);
        bu_notifyLevel.setCellFactory(new Callback<ListView<Double>, ListCell<Double>>() {
            @Override
            public ListCell<Double> call(ListView<Double> param) {
                return new ListCell<Double>(){
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item * 100 + "%");
                        }
                    }
                };
            }
        });

    }



    private Callback<ListView<Category>, ListCell<Category>> createCategoryComboBoxCellFactory(){
        return new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> param) {
                return new ListCell<Category>(){
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        };
    }

}