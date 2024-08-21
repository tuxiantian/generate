package com.tuxt.generate.workflow.controller;


import com.tuxt.generate.workflow.WorkFlowEvent;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuxt
 */
@RestController
@RequestMapping("workFlowInstance")
public class WorkFlowInstanceController {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("rerun")
    public String rerun(@RequestParam Long workFlowInstanceId){
        applicationEventPublisher.publishEvent(new WorkFlowEvent(workFlowInstanceId));
        return "ok";
    }
}

