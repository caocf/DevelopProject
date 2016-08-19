package com.xhl.bqlh.business.view.event;

import com.xhl.bqlh.business.Db.TaskShop;

import java.util.List;

/**
 * Created by Sum on 16/4/19.
 */
public class SelectTaskEvent {
    public static final int type_finish = 0;//任务完成
    public static final int type_add = 1;//添加任务

    public int type;//任务类型

    public String shopId;

    public List<TaskShop> shops;

}
