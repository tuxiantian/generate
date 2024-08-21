package com.tuxt.generate.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuxt.generate.workflow.WorkflowStatus;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.tuxt.generate.workflow.mapper.WorkFlowInstanceMapper;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuxt
 */
@Service
public class WorkFlowInstanceServiceImpl extends ServiceImpl<WorkFlowInstanceMapper, WorkFlowInstance> implements IWorkFlowInstanceService {

    @Override
    public void shutdown(Long[] array){
        List<WorkFlowInstance> list=new ArrayList<>();
        for (Long id : array) {
            WorkFlowInstance workFlowInstance=new WorkFlowInstance();
            workFlowInstance.setId(id);
            workFlowInstance.setShutdown(1);
        }
        this.updateBatchById(list);
    }

    @Override
    public WorkFlowInstance getOneShutdown(){
        LambdaQueryWrapper<WorkFlowInstance> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkFlowInstance::getShutdown,1)
                .eq(WorkFlowInstance::getStatus,WorkflowStatus.runing.name()).orderByAsc(WorkFlowInstance::getId);
        WorkFlowInstance workFlowInstance = this.getOne(queryWrapper);
        WorkFlowInstance update=new WorkFlowInstance();
        update.setShutdown(0);
        update.setVersion(workFlowInstance.getVersion()+1);
        LambdaUpdateWrapper<WorkFlowInstance> updateWrapper=new LambdaUpdateWrapper<>();
                updateWrapper.eq(WorkFlowInstance::getVersion,workFlowInstance.getVersion())
                        .eq(WorkFlowInstance::getId,workFlowInstance.getId());
        boolean updated = this.update(update, updateWrapper);
        if (updated){
            return workFlowInstance;
        }
        return null;
    }

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


