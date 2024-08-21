package com.tuxt.generate.workflow.task;

import com.tuxt.generate.workflow.Context;
import com.tuxt.generate.workflow.Task;
import com.tuxt.generate.workflow.TaskStatus;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PreTrainTask implements Task {
    @Autowired
    private IWorkFlowInstanceService workFlowInstanceService;
    @Override
    public TaskStatus handle(Context context) {
        try {
            System.out.println("PreTrainTask finish");
        }catch (Exception e){
            log.error("PreTrainTaskError",e);
            workFlowInstanceService.updateStatus(context.getWorkFlowInstanceId(), ExceptionUtils.getMessage(e));
            return TaskStatus.fail;
        }

        return TaskStatus.finish;
    }
}
