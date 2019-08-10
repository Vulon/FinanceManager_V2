package FinanceManager_V2.Events;

import org.springframework.context.ApplicationEvent;

public class IncorrectInputEvent extends ApplicationEvent {
    private String field;
    private String advice;

    public IncorrectInputEvent(Object source, String field, String advice) {
        super(source);
        this.field = field;
        this.advice = advice;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
