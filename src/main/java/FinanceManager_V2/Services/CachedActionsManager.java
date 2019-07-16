package FinanceManager_V2.Services;


import FinanceManager_V2.Database.Entity.*;
import FinanceManager_V2.Database.Repositories.BudgetActionRepository;
import FinanceManager_V2.Database.Repositories.CategoryActionRepository;
import FinanceManager_V2.Database.Repositories.TransactionActionRepository;


import FinanceManager_V2.TransportableDataObjects.ActionQueue;
import FinanceManager_V2.TransportableDataObjects.TokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
public class CachedActionsManager {
    BudgetActionRepository budgetActionRepository;
    TransactionActionRepository transactionActionRepository;
    CategoryActionRepository categoryActionRepository;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    AuthenticationService authenticationService;
    ActionQueue actionQueue;

    public CachedActionsManager(BudgetActionRepository budgetActionRepository, TransactionActionRepository transactionActionRepository, CategoryActionRepository categoryActionRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor, AuthenticationService authenticationService) {
        this.budgetActionRepository = budgetActionRepository;
        this.transactionActionRepository = transactionActionRepository;
        this.categoryActionRepository = categoryActionRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.authenticationService = authenticationService;
        actionQueue = new ActionQueue();
    }


    private boolean isThreadActive;

    public class ServerConnectionThread implements Runnable{

        ActionQueue localQueue;

        @Override
        public void run() {
            localQueue = new ActionQueue();
            localQueue.setBudgetActions(budgetActionRepository.getAllByUser(authenticationService.getUserId()));
            localQueue.setCategoryActions(categoryActionRepository.getAllByUser(authenticationService.getUserId()));
            localQueue.setTransactionActions(transactionActionRepository.getAllByUser(authenticationService.getUserId()));
            synchronized (actionQueue){
                localQueue.addAll(actionQueue);
            }
            TokenData tokenData = authenticationService.getTokenData();
            synchronized (tokenData){
                if (tokenData.getAccess_token_expire_date().getTime() >= Date.from(Instant.now()).getTime()){
                    if(tokenData.getRefresh_token_expire_date().getTime() >= Date.from(Instant.now()).getTime()){
                        String email;
                        String password;
                        synchronized (authenticationService.user){
                            email = authenticationService.getUserEmail();
                            password = authenticationService.getUserPassword();
                        }

                    }
                }
            }
        }
    }

    public void cacheAction(Action action, Class actionClass){
        if(actionClass.equals(CategoryAction.class)){
            CategoryAction categoryAction = (CategoryAction) action;
            if (isThreadActive()){
                synchronized (this.actionQueue){
                    actionQueue.addCategoryAction(categoryAction);
                }
            }else{
                categoryActionRepository.saveAndFlush(categoryAction);
            }
        }else if(actionClass.equals(TransactionAction.class)){
            TransactionAction transactionAction = (TransactionAction)action;
            if(isThreadActive()){
                synchronized (this.actionQueue){
                    actionQueue.addTransactionAction(transactionAction);
                }
            }else{
                transactionActionRepository.saveAndFlush(transactionAction);
            }
        }else if(actionClass.equals(BudgetAction.class)){
            BudgetAction budgetAction = (BudgetAction)action;
            if(isThreadActive()){
                synchronized (this.actionQueue){
                    actionQueue.addBudgetAction(budgetAction);
                }
            }else{
                budgetActionRepository.saveAndFlush(budgetAction);
            }
        }
        //Start server upload maybe
    }



    public synchronized boolean isThreadActive(){
        return isThreadActive;
    }
    private synchronized void setThreadActive(boolean state){
        this.isThreadActive = state;
    }
}
