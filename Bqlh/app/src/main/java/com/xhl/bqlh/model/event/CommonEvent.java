package com.xhl.bqlh.model.event;

/**
 * Created by Sum on 16/7/1.
 */
public class CommonEvent {

    public static final int ET_RELOGIN = 1;//重新登陆

    public static final int ET_MAIN = 2;//首页

    public static final int ET_RELOAD_USER_INFO = 3;//用户登录成功

    public static final int ET_RELOAD_USER_IMAGE_INFO = 7;//用户头像更新

    public static final int ET_RELOAD_CAR = 4;//刷新购物车

    public static final int ET_RELOAD_ORDER_NUM = 5;//刷新订单数量

    public static final int ET_RELOAD_COLLECTION_NUM = 8;//刷新收藏数量

    public static final int ET_RELOAD_ORDER_INFO = 6;//刷新订单

    public static final int ET_RELOAD_ADS = 9;//刷新广告

    public static final int ET_RELOAD_CAR_MONEY = 10;//购物车总额

    private int eventTag;

    public int getEventTag() {
        return eventTag;
    }

    public void setEventTag(int eventTag) {
        this.eventTag = eventTag;
    }

    public int refresh_order_type;//订单刷新信息
    public int refresh_order_view;//订单刷新的view
}
