package FinanceManager_V2.Services;


import FinanceManager_V2.Database.Entity.*;
import FinanceManager_V2.Database.Repositories.*;


import FinanceManager_V2.Events.DataChangedEvent;
import FinanceManager_V2.TransportableDataObjects.ActionQueue;
import FinanceManager_V2.TransportableDataObjects.TokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@Service
public class CachedActionsManager {
    private BudgetActionRepository budgetActionRepository;
    private TransactionActionRepository transactionActionRepository;
    private CategoryActionRepository categoryActionRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private AuthenticationService authenticationService;
    private ActionQueue cachedActions;
    private ServerDataManager serverDataManager;

    private ApplicationEventPublisher applicationEventPublisher;

    private ServerConnectionThread connectionThread;



    private boolean isThreadActive;

    public CachedActionsManager(BudgetActionRepository budgetActionRepository, TransactionActionRepository transactionActionRepository, CategoryActionRepository categoryActionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, TransactionRepository transactionRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor, AuthenticationService authenticationService, ServerDataManager serverDataManager, ApplicationEventPublisher applicationEventPublisher) {
        this.budgetActionRepository = budgetActionRepository;
        this.transactionActionRepository = transactionActionRepository;
        this.categoryActionRepository = categoryActionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.authenticationService = authenticationService;
        this.serverDataManager = serverDataManager;
        this.applicationEventPublisher = applicationEventPublisher;
        cachedActions = new ActionQueue();
        setThreadActive(false);
    }


    @Scheduled(fixedDelay = 1000 * 60 * 10) //get updates every 10 minutes
    public void processUpdates(){
        if (authenticationService.isInitialized()){
            if (!isThreadActive()){
                connectionThread = new ServerConnectionThread();
                threadPoolTaskExecutor.execute(connectionThread);
            }
        }
    }


    public class ServerConnectionThread implements Runnable{

        private ActionQueue localQueue;
        private TokenData tokenData;

        DataChangedEvent dataChangedEvent;

        @Override
        public void run() {
            setThreadActive(true);
            dataChangedEvent = new DataChangedEvent(this, false, false, false);
            System.out.println("Server connection thread started");
            localQueue = new ActionQueue();
            localQueue.setBudgetActions(budgetActionRepository.getAllByUser(authenticationService.getUserId()));
            localQueue.setCategoryActions(categoryActionRepository.getAllByUser(authenticationService.getUserId()));
            localQueue.setTransactionActions(transactionActionRepository.getAllByUser(authenticationService.getUserId()));
            synchronized (cachedActions){
                localQueue.addAll(cachedActions);
            }
            loadCachedActionsToDatabase(cachedActions);

            if (!isAccessTokenValid()){
                setThreadActive(false);
                System.out.println("CachedActionsManager: token not valid");
                return;
            }
            System.out.println("CachedActionsManager: token valid");
            //get updates from server
            ActionQueue updatesQueue;
            int categoriesAmount = categoryRepository.findAllByUser(authenticationService.getUserId()).size();
            if(categoriesAmount < 2){
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(2000, Calendar.JANUARY, 1);
                System.out.println("Update date set to 1.1.2000 because categories amount: " + categoriesAmount);
                updatesQueue =  serverDataManager.getLastUpdates(
                        tokenData.getAccess_token(),
                        Date.from(calendar.toInstant()));
            }else{
                System.out.println("Update date set to " + authenticationService.getLastUpdateDate().toString());
                updatesQueue = serverDataManager.getLastUpdates(
                        tokenData.getAccess_token(),
                        authenticationService.getLastUpdateDate());
            }

            if(updatesQueue == null || serverDataManager.lastResponseCode != AuthenticationService.ServerResponseCode.OK){
                //Server did not return correct answer
                setThreadActive(false);
                System.out.println("CachedActionsManager: error getting updates server response code " + serverDataManager.lastResponseCode.toString());
                return;
            }
            System.out.println("CachedActionsManager: getLastUpdates server code OK");
            try{
                System.out.println("Started processing update queue from server with size " + updatesQueue.getTotalSize());
                processActionQueue(updatesQueue);
            }catch (DatabaseUpdateException e){ //TODO send message, and try to get updates again
                e.printStackTrace();
                setThreadActive(false);
                return;
            }
            System.out.println("CachedActionsManager: Finished processing updates");

            //post updates to server and get back completed ones
            System.out.println("Posting to server " + localQueue.getTotalSize() + " actions");
            System.out.println("ACTIONS TO POST: " + localQueue);
            ActionQueue completedUpdates = serverDataManager.postUpdates(localQueue, tokenData.getAccess_token());
            if(completedUpdates == null || serverDataManager.lastResponseCode != AuthenticationService.ServerResponseCode.OK){
                //Server did not return correct answer
                setThreadActive(false);
                System.out.println("CachedActionsManager: error posting updates server response code " + serverDataManager.lastResponseCode.toString());
                return;
            }
            System.out.println("CachedActionsManager: Updates posted. Processing " + completedUpdates.getTotalSize() + " updates");
            try{
                processActionQueue(completedUpdates);
            }catch (DatabaseUpdateException e){
                e.printStackTrace();
                Queue<Action> queue = completedUpdates.getActionsQueue();
                while (queue.size() > 0){
                    clearActionRepositories(queue, e.failedAction);
                }

            }
            clearActionRepositories(completedUpdates.getActionsQueue(), null);
            applicationEventPublisher.publishEvent(dataChangedEvent);
            dataChangedEvent = null;
            setThreadActive(false);
        }

        private void clearActionRepositories(Queue<Action> actions, @Nullable Action stopAction){
            System.out.println("Clearing action repositories");
            categoryActionRepository.deleteAllByUser(authenticationService.getUserId());
            transactionActionRepository.deleteAllByUser(authenticationService.getUserId());
            budgetActionRepository.deleteAllByUser(authenticationService.getUserId());
//            while (actions.size() > 0){
//                Action action = actions.remove();
//                switch (action.getType()){
//                    case "budget":{
//                        BudgetAction budgetAction = (BudgetAction)action;
//                        if(stopAction != null && stopAction.getType().equals("budget") && ((BudgetAction)stopAction).getBudget().equals(budgetAction.getBudget())){
//                            actions.clear();
//                            break;
//                        }
//                        budgetActionRepository.deleteByUserAndBudgetAndCreate(authenticationService.getUserId(), budgetAction.getBudget(), budgetAction.isCreate());
//                        break;
//                    }
//                    case "transaction":{
//                        TransactionAction transactionAction = (TransactionAction)action;
//                        if(stopAction != null && stopAction.getType().equals("transaction") && ((TransactionAction)stopAction).getTransaction().equals(transactionAction.getTransaction())){
//                            actions.clear();
//                            break;
//                        }
//                        transactionActionRepository.deleteByUserAndTransactionAndCreate(authenticationService.getUserId(), transactionAction.getTransaction(), transactionAction.isCreate());
//                        break;
//                    }
//                    case "category":{
//                        CategoryAction categoryAction = (CategoryAction)action;
//                        if(stopAction != null && stopAction.getType().equals("category") && ((CategoryAction)stopAction).getCategory().equals(categoryAction.getCategory())){
//                            actions.clear();
//                            break;
//                        }
//                        categoryActionRepository.deleteByUserAndCategoryAndCreate(authenticationService.getUserId(), categoryAction.getCategory(), categoryAction.isCreate());
//                        break;
//                    }
//                }
//            }
        }

        private void loadCachedActionsToDatabase(ActionQueue queue){
            ArrayList<BudgetAction> budgetActions = queue.getBudgetActions();
            ArrayList<TransactionAction> transactionActions = queue.getTransactionActions();
            ArrayList<CategoryAction> categoryActions = queue.getCategoryActions();
            for(BudgetAction b : budgetActions){
                budgetActionRepository.save(b);
            }
            budgetActionRepository.flush();
            for(TransactionAction t : transactionActions){
                transactionActionRepository.save(t);
            }
            transactionActionRepository.flush();
            for(CategoryAction c : categoryActions){
                categoryActionRepository.save(c);
            }
            categoryActionRepository.flush();
        }

        private void processActionQueue(ActionQueue processableQueue) throws DatabaseUpdateException{
            Queue<Action> queue = processableQueue.getActionsQueue();
            Long userId = authenticationService.getUserId();
            while (queue.size() > 0){
                Action action = queue.remove();
                switch (action.getType()){
                    case "transaction":{
                        TransactionAction transactionAction = (TransactionAction) action;
                        transactionAction.setUser(authenticationService.getUserId());
                        try{
                            if(transactionAction.isCreate()){
                                Category category = categoryRepository.getByUserAndCategory(userId, transactionAction.getCategory_id());

                                Transaction existing = transactionRepository.getByUserAndTransaction(authenticationService.getUserId(), transactionAction.getOriginalId());
                                if(existing == null){
                                    existing = new Transaction(transactionAction, category);
                                }else{
                                    existing.updateData(transactionAction, category);
                                }
                                existing = transactionRepository.saveAndFlush(existing);
                                System.out.println("Created transaction " + existing.toString());
                            }else{
                                transactionRepository.deleteByUserAndTransaction(userId, transactionAction.getOriginalId());
                                transactionRepository.flush();
                                System.out.println("Deleted transaction " + transactionAction.toString());
                            }
                            dataChangedEvent.setTransactionsChanged(true);
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new DatabaseUpdateException(transactionAction.getClass(), action, e.getMessage());
                        }

                        break;
                    }
                    case "budget":{
                        BudgetAction budgetAction = (BudgetAction)action;
                        budgetAction.setUser(authenticationService.getUserId());
                        try{
                            if(budgetAction.isCreate()){
                               Budget budget = budgetRepository.getByUserAndBudget(authenticationService.getUserId(), budgetAction.getOriginalId());
                               Set<Category> categorySet = budgetAction.getCategories();
                               Set<Category> actualCategories = new HashSet<>();
                               for(Category category : categorySet){
                                   actualCategories.add(categoryRepository.getByUserAndCategory(userId, category.getCategory()));
                               }
                               if(budget == null){
                                   budget = new Budget(budgetAction);
                                   budget.setCategories(actualCategories);
                               }else{
                                   budget.updateData(budgetAction, actualCategories);
                               }
                               budget = budgetRepository.saveAndFlush(budget);
                                System.out.println("Created budget " + budget.toString());
                            }else{
                                budgetRepository.deleteByUserAndBudget(userId, budgetAction.getOriginalId());
                                budgetRepository.flush();
                                System.out.println("Deleted budget " + budgetAction.toString());
                            }
                            dataChangedEvent.setBudgetsChanged(true);
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new DatabaseUpdateException(budgetAction.getClass(), action, e.getMessage());
                        }

                        break;
                    }
                    case "category":{
                        CategoryAction categoryAction = (CategoryAction)action;
                        categoryAction.setUser(authenticationService.getUserId());
                        try{
                            if(categoryAction.isCreate()){
                                Category parent;
                                if(categoryAction.getParent_id() < 0){
                                    parent = null;
                                }else{
                                    parent = categoryRepository.getByUserAndCategory(userId, categoryAction.getParent_id());
                                }

                                Category existing = categoryRepository.getByUserAndCategory(authenticationService.getUserId(), categoryAction.getOriginalId());
                                if(existing == null){
                                    existing = new Category(categoryAction, parent);
                                }else {
                                    existing.updateData(categoryAction, parent);
                                }
                                existing = categoryRepository.saveAndFlush(existing);
                                System.out.println("Created category " + existing.toString());
                            }else{
                                Category parent = categoryRepository.getByUserAndCategory(userId, categoryAction.getParent_id());
                                Category thisCategory = categoryRepository.getByUserAndCategory(userId, categoryAction.getOriginalId());
                                List<Budget> budgetList = budgetRepository.getAllByUserAndCategory(userId, thisCategory);
                                for(Budget b : budgetList){ //swap category reference with parent reference in budget repository
                                    Set<Category> categories = b.getCategories();
                                    categories.removeIf(category -> category.getCategory().equals(thisCategory.getCategory()));
                                    categories.add(parent);
                                    b.setCategories(categories);
                                    budgetRepository.save(b);
                                }
                                budgetRepository.flush();
                                List<Transaction> transactionList = transactionRepository.getAllByUserAndCategory(userId, thisCategory);
                                for(Transaction t : transactionList){
                                    t.setCategory(parent);
                                    transactionRepository.save(t);
                                }
                                transactionRepository.flush();
                                ArrayList<Category> categoryArrayList = categoryRepository.getAllByUserAndAndParent(userId, parent);
                                for(Category c : categoryArrayList){
                                    c.setParent(parent);
                                    categoryRepository.save(c);
                                }
                                categoryRepository.deleteByUserAndCategory(userId, thisCategory.getCategory());
                                categoryRepository.flush();
                                System.out.println("Deleted category " + categoryAction.toString());
                            }
                            dataChangedEvent.setCategoriesChanged(true);
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new DatabaseUpdateException(categoryAction.getClass(), action, e.getMessage());
                        }

                    }
                }
            }

        }

        private boolean isAccessTokenValid(){
            synchronized (authenticationService.tokenData){
                tokenData = new TokenData(authenticationService.tokenData);
            }
            AuthenticationService.ServerResponseCode code = getNewAccessToken();
            if (code.equals(AuthenticationService.ServerResponseCode.CONNECTION_TIMEOUT) ||
            code.equals(AuthenticationService.ServerResponseCode.INTERNAL_SERVER_ERROR)){
                //TODO SEND MESSAGE that server is unavailable
                return false;
            }
            if(code.equals(AuthenticationService.ServerResponseCode.USER_NOT_FOUND)){
                //email does not match password, ask user to sign in again
                return false;
            }
            if (code.equals(AuthenticationService.ServerResponseCode.OK)){
                synchronized (authenticationService.tokenData){
                    tokenData = new TokenData(authenticationService.tokenData);
                }
                return true;
            }else{
                //Notify that something went wrong
                return false;
            }
        }

        private AuthenticationService.ServerResponseCode getNewAccessToken(){
            AuthenticationService.ServerResponseCode code;
            if(tokenData.getRefresh_token_expire_date().getTime() < Date.from(Instant.now()).getTime()){
                return getNewRefreshToken();
            }else{
                code = authenticationService.attemptRefresh(tokenData.getRefresh_token());
            }

            if (code.equals(AuthenticationService.ServerResponseCode.INVALID_TOKEN)){
                return getNewRefreshToken();
            }else{
                return code;
            }
        }
        private AuthenticationService.ServerResponseCode getNewRefreshToken(){
            String email;
            String password;
            synchronized (authenticationService.user){
                email = authenticationService.getUserEmail();
                password = authenticationService.getUserPassword();
            }
            return authenticationService.attemptLogin(email, password);
        }

        public class DatabaseUpdateException extends Exception{
            public Class repositoryClass;
            public Action failedAction;
            public String errorMessage;
            public DatabaseUpdateException(Class repositoryClass, Action failedAction, String errorMessage) {
                super(errorMessage);
                this.repositoryClass = repositoryClass;
                this.failedAction = failedAction;
                this.errorMessage = errorMessage;
            }
        }
    }


    public void cacheAction(Action action, Class actionClass){
        if(actionClass.equals(CategoryAction.class)){
            CategoryAction categoryAction = (CategoryAction) action;
            System.out.println("CachedActionsManager: cached category action " + categoryAction );
            if (isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addCategoryAction(categoryAction);
                    System.out.println("CachedActionsManager: CACHED category action " + categoryAction );
                }
            }else{
                categoryAction = categoryActionRepository.saveAndFlush(categoryAction);
                System.out.println("CachedActionsManager: SAVED TO DATABASE category action " + categoryAction );
            }
        }else if(actionClass.equals(TransactionAction.class)){
            TransactionAction transactionAction = (TransactionAction)action;
            if(isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addTransactionAction(transactionAction);
                    System.out.println("CachedActionsManager: CACHED transaction action " + transactionAction );
                }
            }else{
                transactionAction = transactionActionRepository.saveAndFlush(transactionAction);
                System.out.println("CachedActionsManager: SAVED TO DATABASE transaction action " + transactionAction );
            }
        }else if(actionClass.equals(BudgetAction.class)){
            BudgetAction budgetAction = (BudgetAction)action;
            if(isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addBudgetAction(budgetAction);
                    System.out.println("CachedActionsManager: CACHED budget action " + budgetAction);
                }
            }else{
                budgetAction = budgetActionRepository.saveAndFlush(budgetAction);
                System.out.println("CachedActionsManager: SAVED TO DATABASE budget action " + budgetAction);
            }
        }
        processUpdates();
    }



    public synchronized boolean isThreadActive(){
        return isThreadActive;
    }
    private synchronized void setThreadActive(boolean state){
        this.isThreadActive = state;
    }
}
