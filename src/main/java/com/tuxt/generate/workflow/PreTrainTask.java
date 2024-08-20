package com.tuxt.generate.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PreTrainTask implements Task{

    @Override
    public TaskStatus handle(Context context) {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("PreTrainTask finish");
        return TaskStatus.finish;
    }
}
