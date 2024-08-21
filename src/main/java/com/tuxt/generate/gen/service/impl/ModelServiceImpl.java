package com.tuxt.generate.gen.service.impl;

import com.alibaba.fastjson.JSON;
import com.tuxt.generate.gen.entity.Model;
import com.tuxt.generate.gen.mapper.ModelMapper;
import com.tuxt.generate.gen.service.IModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuxt.generate.workflow.Context;
import com.tuxt.generate.workflow.WorkFlow;
import com.tuxt.generate.workflow.WorkFlowEvent;
import com.tuxt.generate.workflow.WorkflowStatus;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author tuxt
*/
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {
    @Autowired
    private IWorkFlowInstanceService workFlowInstanceService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public Long saveModel(Model model) {
        this.save(model);
        createWorkFlow(model);
        return model.getId();
    }

    private void createWorkFlow(Model model) {
        WorkFlowInstance workFlowInstance=new WorkFlowInstance();
        workFlowInstance.setName(WorkFlow.Train.getName());
        workFlowInstance.setLastTask(WorkFlow.Train.getFirstTask());
        workFlowInstance.setStatus(WorkflowStatus.create.name());
        workFlowInstance.setContext(JSON.toJSONString(new Context(model.getId())));
        workFlowInstance.setCreateDate(new Date());
        workFlowInstanceService.save(workFlowInstance);
        Model updateModel=new Model();
        updateModel.setId(model.getId());
        updateModel.setWorkFlowInstanceId(workFlowInstance.getId());
        this.updateById(updateModel);
        applicationEventPublisher.publishEvent(new WorkFlowEvent(workFlowInstance.getId()));
    }
}


