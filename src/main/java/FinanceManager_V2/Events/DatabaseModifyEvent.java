package FinanceManager_V2.Events;

import FinanceManager_V2.Services.CachedActionsManager;
import org.springframework.context.ApplicationEvent;

public class DatabaseModifyEvent extends ApplicationEvent {
    private CachedActionsManager.ServerConnectionThread.DatabaseUpdateException updateException;

    public DatabaseModifyEvent(Object source, CachedActionsManager.ServerConnectionThread.DatabaseUpdateException updateException) {
        super(source);
        this.updateException = updateException;
    }

    public CachedActionsManager.ServerConnectionThread.DatabaseUpdateException getUpdateException() {
        return updateException;
    }

    public void setUpdateException(CachedActionsManager.ServerConnectionThread.DatabaseUpdateException updateException) {
        this.updateException = updateException;
    }
}
