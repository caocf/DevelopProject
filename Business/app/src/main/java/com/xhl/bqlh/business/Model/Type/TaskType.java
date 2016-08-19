package com.xhl.bqlh.business.Model.Type;

/**
 * Created by Sum on 16/4/18.
 */
public interface TaskType {

    /**
     * 正常任务
     */
    int TYPE_Normal = 0;
    /**
     * 临时任务
     */
    int TYPE_Temporary = 1;


    //未开始
    int STATE_UN_START = 0;
    //拜访中
    int STATE_START = 1;
    //拜访完成
    int STATE_FINISH = 2;
    //未完成
    int STATE_UN_FINISH = 3;
}
