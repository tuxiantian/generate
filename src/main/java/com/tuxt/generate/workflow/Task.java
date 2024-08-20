package com.tuxt.generate.workflow;

public interface Task {
    TaskStatus handle(Context context);
}
