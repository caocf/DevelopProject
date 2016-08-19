package com.xhl.bqlh.business.view.ui.callback;

import com.xhl.bqlh.business.Db.TaskShop;

/**
 * Created by Sum on 16/4/19.
 */
public interface TaskChangeListener {


    /**
     * 完成一个店铺的拜访任务
     *
     * @param shopId
     */
    void finishOneTask(String shopId);


    /**
     * 添加一个临时的店铺拜访任务
     *
     * @param temporaryTask
     */
    void addTemporaryTask(TaskShop temporaryTask);
}
