package com.tuxt.generate.workflow;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class WorkFlowEvent extends ApplicationEvent {
    private Long businessId;
    public WorkFlowEvent(Object source) {
        super(source);
    }

    public WorkFlowEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public WorkFlowEvent(Object source,Long businessId) {
        super(source);
        this.businessId=businessId;
    }

    public Long getBusinessId() {
        return businessId;
    }
}
