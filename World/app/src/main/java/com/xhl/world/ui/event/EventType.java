package com.xhl.world.ui.event;

/**
 * Created by Sum on 15/12/5.
 */
public interface EventType {

    public static final int Event_Scan = 1;
    public static final int Event_Go_HOME = 2;
    public static final int Event_ReLogin = 3;
    public static final int Event_Exit_Login = 6;
    public static final int Event_SingleImageDetails = 4;
    public static final int Event_MulitImageDetails = 5;
    public static final int Event_CheckVersion = 7;
    public static final int Event_Reload_Info = 8;

    public static final int Shopping_Event_item_select_all = 7;//全选子商品
    public static final int Shopping_Event_item_un_select_all = 8;
    public static final int Shopping_Event_item_child_select_all = 9;//全选一个商品（个数不一）
    public static final int Shopping_Event_item_child_un_select_all = 10;
    public static final int Shopping_Event_item_child_add_goods = 5;//添加一个商品
    public static final int Shopping_Event_item_child_reduce_goods = 6;//减少一个商品

    public static final int Shopping_Event_Refresh = 100;//通知刷新购物车
}
