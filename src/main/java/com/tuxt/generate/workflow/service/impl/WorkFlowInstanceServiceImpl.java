package com.tuxt.generate.workflow.service.impl;

import com.tuxt.generate.workflow.WorkflowStatus;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.tuxt.generate.workflow.mapper.WorkFlowInstanceMapper;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author tuxt
 */
@Service
public class WorkFlowInstanceServiceImpl extends ServiceImpl<WorkFlowInstanceMapper, WorkFlowInstance> implements IWorkFlowInstanceService {

    @Override
    public void updateStatus(Long id, WorkflowStatus status, String lastTask, String context, String errorMessage) {
        WorkFlowInstance workFlowInstance = new WorkFlowInstance();
        workFlowInstance.setId(id);
        workFlowInstance.setUpdateDate(new Date());
        if (lastTask != null) {
            workFlowInstance.setLastTask(lastTask);
        }
        workFlowInstance.setStatus(status.name());
        if (errorMessage != null) {
            workFlowInstance.setErrorMessage(errorMessage);
        }

        workFlowInstance.setContext(context);

        this.updateById(workFlowInstance);
    }

    @Override
    public void updateStatus(Long id, String errorMessage) {
        WorkFlowInstance workFlowInstance = new WorkFlowInstance();
        workFlowInstance.setId(id);
        workFlowInstance.setUpdateDate(new Date());
        workFlowInstance.setErrorMessage(errorMessage);
        this.updateById(workFlowInstance);
    }

    @Override
    public void updateStatus(Long id, WorkflowStatus status, String lastTask, String context) {
        this.updateStatus(id, status, lastTask, context, null);
    }
}


