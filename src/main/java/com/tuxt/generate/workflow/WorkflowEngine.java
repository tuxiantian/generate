package com.tuxt.generate.workflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WorkflowEngine implements CommandLineRunner , ApplicationListener<WorkFlowEvent> {

    @Resource
    private ApplicationContext applicationContext;
    private final Map<String, Task> taskMap = new ConcurrentHashMap<String, Task>();

    @Override
    public void run(String... args) throws Exception {
        Map<String, Task> beansMap = applicationContext.getBeansOfType(Task.class);
        taskMap.putAll(beansMap);
    }


    @Async("myAsyncExecutor")
    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        WorkFlow workFlow = (WorkFlow) event.getSource();
        String[] tasks = workFlow.getTaskTemplate().split(",");
        int lastTask = 0;
        while (lastTask < tasks.length) {
            TaskStatus taskStatus = taskMap.get(tasks[lastTask]).handle(new Context());
            switch (taskStatus) {
                case finish:
                    lastTask++;
                    break;
                case fail:

                    return;
                case runing:
                    break;
            }
        }
    }
}
