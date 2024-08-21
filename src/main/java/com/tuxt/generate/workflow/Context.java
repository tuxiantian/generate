package com.tuxt.generate.workflow;

import lombok.Data;

@Data
public class Context {
    private Long businessId;
    private Long workFlowInstanceId;
    public Context() {
    }

    public Context(Long businessId) {
        this.businessId = businessId;
    }


}
