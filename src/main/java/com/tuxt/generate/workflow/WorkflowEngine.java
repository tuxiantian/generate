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

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class WorkflowEngine implements CommandLineRunner , ApplicationListener<WorkFlowEvent> {
    @Autowired
    private IWorkFlowInstanceService workFlowInstanceService;
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ThreadPoolExecutor dtpExecutor1;
    private final Map<String, Task> taskMap = new ConcurrentHashMap<String, Task>();
    Queue<Long> queue = new ConcurrentLinkedQueue<>();
    AtomicBoolean hasShutdown=new AtomicBoolean(true);
    int retryThreshold=5;

    @PreDestroy
    public void shutdown() {
        System.out.println("shutdown");
        if (!queue.isEmpty()) {
            List<Long> elementsList = new ArrayList<>(queue);
            System.out.println(JSON.toJSON(elementsList));
            workFlowInstanceService.shutdown(elementsList);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Task> beansMap = applicationContext.getBeansOfType(Task.class);
        taskMap.putAll(beansMap);
        while (hasShutdown.get()){
            WorkFlowInstance workFlowInstance = workFlowInstanceService.getOneShutdown();
            if (workFlowInstance!=null){
                boolean updateVersion = workFlowInstanceService.updateVersion(workFlowInstance.getId(), workFlowInstance.getVersion());
                if (updateVersion) {
                    execute(workFlowInstance.getId());
                }
            }else {
                hasShutdown.getAndSet(false);
            }
        }
    }


    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        dtpExecutor1.execute(()->{
            Long workFlowInstanceId = (Long) event.getSource();
            queue.add(workFlowInstanceId);
            execute(workFlowInstanceId);
            queue.remove(workFlowInstanceId);
        });

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
                int attempts=0;
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

                        while (attempts<retryThreshold){
                            taskStatus = taskMap.get(tasks[lastTask]).handle(context);
                            attempts=attempts+1;
                            if (taskStatus.equals(TaskStatus.finish)){
                                lastTask++;
                                break;
                            }
                            if (taskStatus.equals(TaskStatus.fail)){
                                workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.fail,tasks[lastTask],JSON.toJSONString(context));
                                return;
                            }
                        }

                        if (taskStatus.equals(TaskStatus.runing) && attempts==retryThreshold){
                            String errorMessage=String.format("已经尝试了%d次，工作流程结束",retryThreshold);
                            workFlowInstanceService.updateStatus(workFlowInstanceId,WorkflowStatus.fail,tasks[lastTask],JSON.toJSONString(context),errorMessage);
                            return;
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
