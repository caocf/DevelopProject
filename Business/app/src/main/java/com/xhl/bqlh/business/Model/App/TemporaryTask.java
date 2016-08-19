package com.xhl.bqlh.business.Model.App;

import com.xhl.bqlh.business.Db.TaskShop;

/**
 * Created by Sum on 16/4/19.
 */
public class TemporaryTask {

    private TaskShop taskShop;//选择的店铺

    private boolean isChecked = false;//选择状态

    public TemporaryTask(TaskShop taskShop) {
        this.taskShop = taskShop;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public TaskShop getTaskShop() {
        return taskShop;
    }

    public void setTaskShop(TaskShop taskShop) {
        this.taskShop = taskShop;
    }
}
