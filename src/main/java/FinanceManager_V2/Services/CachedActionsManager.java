package FinanceManager_V2.Services;


import FinanceManager_V2.Database.Entity.*;
import FinanceManager_V2.Database.Repositories.*;


import FinanceManager_V2.TransportableDataObjects.ActionQueue;
import FinanceManager_V2.TransportableDataObjects.TokenData;
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


    private ServerConnectionThread connectionThread;

    private boolean isThreadActive;

    public CachedActionsManager(BudgetActionRepository budgetActionRepository, TransactionActionRepository transactionActionRepository, CategoryActionRepository categoryActionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, TransactionRepository transactionRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor, AuthenticationService authenticationService) {
        this.budgetActionRepository = budgetActionRepository;
        this.transactionActionRepository = transactionActionRepository;
        this.categoryActionRepository = categoryActionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.authenticationService = authenticationService;
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

        @Override
        public void run() {
            setThreadActive(true);
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
                return;
            }
            //get updates from server
            ActionQueue updatesQueue = serverDataManager.getLastUpdates(tokenData.getAccess_token(), authenticationService.getLastUpdateDate());
            if(updatesQueue == null || serverDataManager.lastResponseCode != AuthenticationService.ServerResponseCode.OK){
                //Server did not return correct answer
                setThreadActive(false);
                return;
            }
            try{
                processActionQueue(updatesQueue);
            }catch (DatabaseUpdateException e){ //TODO send message, and try to get updates again
                e.printStackTrace();
                setThreadActive(false);
                return;
            }
            //post updates to server and get back completed ones
            ActionQueue completedUpdates = serverDataManager.postUpdates(localQueue, tokenData.getAccess_token());
            if(completedUpdates == null || serverDataManager.lastResponseCode != AuthenticationService.ServerResponseCode.OK){
                //Server did not return correct answer
                setThreadActive(false);
                return;
            }
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
            setThreadActive(false);
        }

        private void clearActionRepositories(Queue<Action> actions, @Nullable Action stopAction){
            while (actions.size() > 0){
                Action action = actions.remove();
                switch (action.getType()){
                    case "budget":{
                        BudgetAction budgetAction = (BudgetAction)action;
                        if(stopAction != null && stopAction.getType().equals("budget") && ((BudgetAction)stopAction).getBudget().equals(budgetAction.getBudget())){
                            actions.clear();
                            break;
                        }
                        budgetActionRepository.deleteByUserAndBudgetAndCreate(authenticationService.getUserId(), budgetAction.getBudget(), budgetAction.isCreate());
                        break;
                    }
                    case "transaction":{
                        TransactionAction transactionAction = (TransactionAction)action;
                        if(stopAction != null && stopAction.getType().equals("transaction") && ((TransactionAction)stopAction).getTransaction().equals(transactionAction.getTransaction())){
                            actions.clear();
                            break;
                        }
                        transactionActionRepository.deleteByUserAndTransactionAndCreate(authenticationService.getUserId(), transactionAction.getTransaction(), transactionAction.isCreate());
                        break;
                    }
                    case "category":{
                        CategoryAction categoryAction = (CategoryAction)action;
                        if(stopAction != null && stopAction.getType().equals("category") && ((CategoryAction)stopAction).getCategory().equals(categoryAction.getCategory())){
                            actions.clear();
                            break;
                        }
                        categoryActionRepository.deleteByUserAndCategoryAndCreate(authenticationService.getUserId(), categoryAction.getCategory(), categoryAction.isCreate());
                        break;
                    }
                }
            }
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
                        try{
                            if(transactionAction.isCreate()){
                                Category category = categoryRepository.getByUserAndCategory(userId, transactionAction.getCategory_id());
                                Transaction transaction = new Transaction(transactionAction, category);
                                transactionRepository.saveAndFlush(transaction);
                            }else{
                                transactionRepository.deleteByUserAndTransaction(userId, transactionAction.getTransaction());
                                transactionRepository.flush();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new DatabaseUpdateException(transactionAction.getClass(), action, e.getMessage());
                        }
                        break;
                    }
                    case "budget":{
                        BudgetAction budgetAction = (BudgetAction)action;
                        try{
                            if(budgetAction.isCreate()){
                               Budget budget = new Budget(budgetAction);
                               Set<Category> categorySet = budgetAction.getCategories();
                               Set<Category> actualCategories = new HashSet<>();
                               for(Category category : categorySet){
                                    actualCategories.add(categoryRepository.getByUserAndCategory(userId, category.getCategory()));
                               }
                               budget.setCategories(actualCategories);
                               budgetRepository.saveAndFlush(budget);
                            }else{
                                budgetRepository.deleteByUserAndBudget(userId, budgetAction.getBudget());
                                budgetRepository.flush();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new DatabaseUpdateException(budgetAction.getClass(), action, e.getMessage());
                        }
                        break;
                    }
                    case "category":{
                        CategoryAction categoryAction = (CategoryAction)action;
                        try{
                            if(categoryAction.isCreate()){
                                Category parent = categoryRepository.getByUserAndCategory(userId, categoryAction.getParent_id());
                                Category category = new Category(categoryAction, parent);
                                categoryRepository.saveAndFlush(category);
                            }else{
                                Category parent = categoryRepository.getByUserAndCategory(userId, categoryAction.getParent_id());
                                Category thisCategory = categoryRepository.getByUserAndCategory(userId, categoryAction.getCategory());
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
                            }
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
            if (isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addCategoryAction(categoryAction);
                }
            }else{
                categoryActionRepository.saveAndFlush(categoryAction);
            }
        }else if(actionClass.equals(TransactionAction.class)){
            TransactionAction transactionAction = (TransactionAction)action;
            if(isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addTransactionAction(transactionAction);
                }
            }else{
                transactionActionRepository.saveAndFlush(transactionAction);
            }
        }else if(actionClass.equals(BudgetAction.class)){
            BudgetAction budgetAction = (BudgetAction)action;
            if(isThreadActive()){
                synchronized (this.cachedActions){
                    cachedActions.addBudgetAction(budgetAction);
                }
            }else{
                budgetActionRepository.saveAndFlush(budgetAction);
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
