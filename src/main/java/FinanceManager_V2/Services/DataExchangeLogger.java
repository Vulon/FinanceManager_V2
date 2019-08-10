package FinanceManager_V2.Services;


import FinanceManager_V2.Database.Entity.*;
import FinanceManager_V2.Database.Repositories.LoggerRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
public class DataExchangeLogger {

    LoggerRepository loggerRepository;
    AuthenticationService authenticationService;

    public DataExchangeLogger(LoggerRepository loggerRepository, AuthenticationService authenticationService) {
        this.loggerRepository = loggerRepository;
        this.authenticationService = authenticationService;
    }

    public void logCategoryCreateDeleteAction(CategoryAction categoryAction){
        String note = buildCreateDeleteNote(categoryAction);
        note += " with name " + categoryAction.getName();
        note += " with id " + categoryAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), categoryAction.getClass().getName(), note));
    }

    public void logCategoryModifyAction(CategoryAction categoryAction){
        String note = "Modified " + categoryAction.getType() + " with name " + categoryAction.getName() + " and id" + categoryAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), categoryAction.getClass().getName(), note));
    }

    public void logTransactionCreateDeleteAction(TransactionAction transactionAction){
        String note = buildCreateDeleteNote(transactionAction);
        note += " with amount " + transactionAction.getAmount();
        note += " with id " + transactionAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), transactionAction.getClass().getName(), note));
    }

    public void logTransactionModifyAction(TransactionAction transactionAction){
        String note = "Modified " + transactionAction.getType() + " with amount " + transactionAction.getAmount() + " and id" + transactionAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), transactionAction.getClass().getName(), note));
    }

    public void logBudgetCreateDeleteAction(BudgetAction budgetAction){
        String note = buildCreateDeleteNote(budgetAction);
        note += " with name " + budgetAction.getName();
        note += " with id " + budgetAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), budgetAction.getClass().getName(), note));
    }

    public void logBudgetModifyAction(BudgetAction budgetAction){
        String note = "Modified " + budgetAction.getType() + " with name " + budgetAction.getName() + " and id" + budgetAction.getOriginalId();
        loggerRepository.saveAndFlush(new LoggerEntry(authenticationService.getUserId(), Date.from(Instant.now()), budgetAction.getClass().getName(), note));
    }





    private String buildCreateDeleteNote(Action action){
        String note = "";
        if(action.isCreate()){
            note += "Created ";
        }else{
            note += "Deleted ";
        }
        note += action.getType();
        return note;
    }

    public void logCategoryModifyAction(){

    }
}
