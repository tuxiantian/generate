package com.tuxt.generate.workflow;

public enum WorkFlow {
    Train("trainModel","preTrainTask,trainTask,checkTrainTask");
    final String name;
    final String taskTemplate;


    WorkFlow(String name, String taskTemplate) {
        this.name=name;
        this.taskTemplate=taskTemplate;
    }

    public String getName() {
        return name;
    }

    public String getTaskTemplate() {
        return taskTemplate;
    }
}
