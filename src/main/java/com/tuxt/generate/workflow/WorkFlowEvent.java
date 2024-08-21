package com.tuxt.generate.workflow;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class WorkFlowEvent extends ApplicationEvent {
    public WorkFlowEvent(Object source) {
        super(source);
    }

    public WorkFlowEvent(Object source, Clock clock) {
        super(source, clock);
    }

}
