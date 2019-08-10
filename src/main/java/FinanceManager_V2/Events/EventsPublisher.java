package FinanceManager_V2.Events;

import FinanceManager_V2.Services.AuthenticationService;
import FinanceManager_V2.Services.CachedActionsManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
public class EventsPublisher {
    private ApplicationEventPublisher eventPublisher;
    private DataChangedEvent dataChangedEvent;

    public EventsPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishServerConnectionProgressEvent(Double value){
        eventPublisher.publishEvent(new ServerConnectionProgressEvent(this, value));
    }

    public DataChangedEvent buildDataChangedEvent(){
        dataChangedEvent = new DataChangedEvent(this, false, false, false);
        return dataChangedEvent;
    }

    public void publishDataChangedEvent(){
        eventPublisher.publishEvent(dataChangedEvent);
        dataChangedEvent = null;
    }

    public void publishServerErrorEvent(AuthenticationService.ServerResponseCode responseCode){
        eventPublisher.publishEvent(new ServerErrorEvent(this, responseCode));
    }

    public void publishDatabaseUpdateEvent(CachedActionsManager.ServerConnectionThread.DatabaseUpdateException e){
        eventPublisher.publishEvent(new DatabaseModifyEvent(this, e));
    }

    public void publishIncorrectInputEvent(String field, String advice){
        eventPublisher.publishEvent(new IncorrectInputEvent(this, field, advice));
    }

}
