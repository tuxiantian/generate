package com.tuxt.generate.workflow.task;

import com.tuxt.generate.workflow.Context;
import com.tuxt.generate.workflow.Task;
import com.tuxt.generate.workflow.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainTask implements Task {

    @Override
    public TaskStatus handle(Context context) {
        System.out.println("TrainTask finish");
        return TaskStatus.finish;
    }
}
