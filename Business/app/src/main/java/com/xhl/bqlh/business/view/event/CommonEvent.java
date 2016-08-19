package com.xhl.bqlh.business.view.event;

/**
 * Created by Sum on 16/5/15.
 */
public class CommonEvent {

    //打开菜单
    public static final int EVENT_OPEN_DRAWER = 1;
    //用户头像刷新
    public static final int EVENT_REFRESH_INFO = 2;
    //装车单刷新
    public static final int EVENT_REFRESH_STORE = 3;
    //任务刷新
    public static final int EVENT_REFRESH_TASK = 4;
    //加载任务
    public static final int EVENT_RELOAD_TASK = 5;



    public int eventType;

}
