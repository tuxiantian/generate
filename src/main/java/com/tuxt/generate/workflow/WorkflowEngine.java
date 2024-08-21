package com.tuxt.generate.workflow;

import com.alibaba.fastjson.JSON;
import com.tuxt.generate.workflow.entity.WorkFlowInstance;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class WorkflowEngine implements CommandLineRunner , ApplicationListener<WorkFlowEvent> {
    @Autowired
    private IWorkFlowInstanceService workFlowInstanceService;
    @Resource
    private ApplicationContext applicationContext;
    private final Map<String, Task> taskMap = new ConcurrentHashMap<String, Task>();
    Queue<Long> queue = new ConcurrentLinkedQueue<>();
    AtomicBoolean hasShutdown=new AtomicBoolean(true);

    @PreDestroy
    public void shutdown() {
        if (!queue.isEmpty()) {
            Long[] array = (Long[]) queue.toArray();
            workFlowInstanceService.shutdown(array);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Task> beansMap = applicationContext.getBeansOfType(Task.class);
        taskMap.putAll(beansMap);
    }

    @PostConstruct
    public void processShutdown(){
        while (hasShutdown.get()){
            WorkFlowInstance workFlowInstance = workFlowInstanceService.getOneShutdown();
            if (workFlowInstance!=null){
                execute(workFlowInstance.getId());
            }else {
                hasShutdown.getAndSet(false);
            }
        }
    }


    @Async("myAsyncExecutor")
    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        Long workFlowInstanceId = (Long) event.getSource();
        queue.add(workFlowInstanceId);
        execute(workFlowInstanceId);
        queue.remove(workFlowInstanceId);
    }

    private void execute(Long workFlowInstanceId) {
        WorkFlowInstance workFlowInstance = workFlowInstanceService.getById(workFlowInstanceId);
        if (workFlowInstance==null){
            log.error("WorkFlowInstance {} not exists ",workFlowInstanceId);
        }
        WorkFlow workFlow = WorkFlow.getByName(workFlowInstance.getName());
        String[] tasks = workFlow.getTaskTemplate().split(",");
        int lastTask = 0;
        if (workFlowInstance.getLastTask()!=null){
            lastTask =WorkFlow.getIndexByName(workFlow,workFlowInstance.getLastTask());
        }
        Context context = JSON.parseObject(workFlowInstance.getContext(), Context.class);
        context.setWorkFlowInstanceId(workFlowInstanceId);
        while (lastTask < tasks.length) {
            try {
                workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.runing,tasks[lastTask],JSON.toJSONString(context));
                TaskStatus taskStatus = taskMap.get(tasks[lastTask]).handle(context);
                switch (taskStatus) {
                    case finish:
                        lastTask++;
                        break;
                    case fail:
                        workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.fail,tasks[lastTask],JSON.toJSONString(context));
                        return;
                    case runing:
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }catch (Exception e){
                log.error("taskError",e);
                workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.fail,tasks[lastTask],JSON.toJSONString(context), ExceptionUtils.getStackTrace(e));
            }


        }

        workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.finish,tasks[tasks.length-1],JSON.toJSONString(context));
    }
}
