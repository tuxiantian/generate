package com.tuxt.generate.workflow.service;

import com.tuxt.generate.workflow.WorkflowStatus;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author tuxt
*/
public interface IWorkFlowInstanceService extends IService<WorkFlowInstance> {

    void shutdown(List<Long> array);

    WorkFlowInstance getOneShutdown();

    boolean updateVersion(Long id, Integer version);

    void updateStatus(Long id, WorkflowStatus status, String lastTask, String context, String errorMessage);

    void updateStatus(Long id, String errorMessage);

    void updateStatus(Long id, WorkflowStatus status, String lastTask, String context);
}


