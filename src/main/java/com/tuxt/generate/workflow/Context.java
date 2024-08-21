package com.tuxt.generate.workflow;

import lombok.Data;

@Data
public class Context {
    private Long businessId;
    public Context() {
    }

    public Context(Long businessId) {
        this.businessId = businessId;
    }


}
