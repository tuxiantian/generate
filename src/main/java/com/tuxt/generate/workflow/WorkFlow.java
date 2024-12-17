package com.tuxt.generate.workflow;

public enum WorkFlow {
    Train("trainModel","preTrainTask,trainTask,checkTrainTask");
    final String name;
    /**
     * 工作流中的任务以逗号分隔
     */
    final String taskSequence;


    WorkFlow(String name, String taskTemplate) {
        this.name=name;
        this.taskSequence =taskTemplate;
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
        String[] tasks = workFlow.taskSequence.split(",");
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

    public String getTaskSequence() {
        return taskSequence;
    }

    public String getFirstTask(){
        return this.taskSequence.split(",")[0];
    }
}
