package com.xhl.bqlh.business.Db;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.Date;

/**
 * Created by Sum on 16/4/18.
 * <p/>
 * 记录每天的任务完成情况
 */
@Table(name = "task_record")
public class TaskRecord {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "shopId")
    private String shopId;

    @Column(name = "taskState")
    private int taskState;//任务状态:未开始，未完成，进行中，已完成

    @Column(name = "taskTime")
    private Date taskTime;//任务执行时间

    @Column(name = "taskType")
    private int taskType;//正常任务:0 临时任务:1

    private TaskShop mTaskShop;//任务对应的店铺信息

    //获取任务对于的门店数据
    public TaskShop getShop(DbManager dbManager) {
        try {
            return dbManager.selector(TaskShop.class).where("shopId", "=", this.shopId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public TaskShop getTaskShop() {
        return mTaskShop;
    }

    public void setTaskShop(TaskShop taskType) {
        this.mTaskShop = taskType;
    }

    @Override
    public String toString() {
        return "TaskRecord{" +
                "id=" + id +
                ", shopId='" + shopId + '\'' +
                ", taskState=" + taskState +
                ", taskTime=" + taskTime +
                ", taskType=" + taskType +
                '}';
    }
}
