package FinanceManager_V2.Events;

import FinanceManager_V2.Services.AuthenticationService;
import org.springframework.context.ApplicationEvent;

public class ServerErrorEvent extends ApplicationEvent {
    private AuthenticationService.ServerResponseCode code;

    public ServerErrorEvent(Object source, AuthenticationService.ServerResponseCode code) {
        super(source);
        this.code = code;
    }

    public AuthenticationService.ServerResponseCode getCode() {
        return code;
    }

    public void setCode(AuthenticationService.ServerResponseCode code) {
        this.code = code;
    }
}
