package com.tuxt.generate.workflow;

public enum WorkFlow {
    Train("trainModel","preTrainTask,trainTask,checkTrainTask");
    final String name;
    final String taskTemplate;


    WorkFlow(String name, String taskTemplate) {
        this.name=name;
        this.taskTemplate=taskTemplate;
    }

    public static WorkFlow getByName(String name){
        for (WorkFlow value : WorkFlow.values()) {
            if (value.name.equals(name)){
                return value;
            }
        }
        return null;
    }

    public static  int getIndexByName(WorkFlow workFlow,String name){
        String[] tasks = workFlow.taskTemplate.split(",");
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].equals(name)){
                return i;
            }
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getTaskTemplate() {
        return taskTemplate;
    }

    public String getFirstTask(){
        return this.taskTemplate.split(",")[0];
    }
}
