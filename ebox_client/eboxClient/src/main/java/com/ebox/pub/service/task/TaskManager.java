package com.ebox.pub.service.task;

import com.ebox.pub.service.global.Constants;

import java.util.Vector;

public class TaskManager {

    Vector<TaskData> vector = null;

    public TaskManager(){
        vector=new Vector<TaskData>();
    }

    public synchronized void addTask(TaskData task) {
        if (vector==null) {
             throw new NullPointerException();
        }
        if (!vector.contains(task)) {
            vector.add(task);
            startTask(task);
        }
    }

    public synchronized void removeTask(TaskData task){
        vector.removeElement(task);
        clearTask(task);
    }

    public void checkAllTasks() {

        for (int i = vector.size()-1; i >= 0; i--) {
            TaskUtil.checkTask(vector.elementAt(i));
            if (Constants.DEBUG) {
              //  Log.i("AppService", "TaskType:" + vector.elementAt(i).getType());
            }
        }
    }

    public void clearAllTasks() {
        for (int i = vector.size()-1; i >= 0; i--) {

            removeTask(vector.elementAt(i));
        }
    }


    private void startTask(TaskData task){
        TaskUtil.startTask(task);
    }

    private void clearTask(TaskData task){
        TaskUtil.cleanTimerTask(task);
    }

}
