package FinanceManager_V2.Events;

import org.springframework.context.ApplicationEvent;

public class ServerConnectionProgressEvent extends ApplicationEvent {
    private Double progress;

    public ServerConnectionProgressEvent(Object source, Double progress) {
        super(source);
        this.progress = progress;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }
}
