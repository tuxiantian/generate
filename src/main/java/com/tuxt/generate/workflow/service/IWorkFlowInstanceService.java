package com.tuxt.generate.workflow.service;

import com.tuxt.generate.workflow.WorkflowStatus;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tuxt
*/
public interface IWorkFlowInstanceService extends IService<WorkFlowInstance> {

    void updateStatus(Long id, WorkflowStatus status, String lastTask,String context, String errorMessage);

    void updateStatus(Long id, String errorMessage);

    void updateStatus(Long id, WorkflowStatus status, String lastTask, String context);
}


