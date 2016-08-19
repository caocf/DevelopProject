package com.xhl.bqlh.business.view.helper;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.Model.App.SearchShopModel;
import com.xhl.bqlh.business.view.event.ClickShopEvent;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.event.SelectTaskEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/4/20.
 */
public class EventHelper {

    /**
     * 搜索的店铺查询
     *
     * @param shop
     */
    public static void postSearchShop(SearchShopModel shop) {
        ClickShopEvent event = new ClickShopEvent();
        event.shopId = shop.getShopId();
        event.latitude = shop.getLatitude();
        event.longitude = shop.getLongitude();
        event.shopName = shop.getShopName();
        event.shopType = GlobalParams.VISIT_SHOP_SEARCH;
        EventBus.getDefault().post(event);
    }


    public static void postTaskShop(TaskShop shop) {
        ClickShopEvent event = new ClickShopEvent();
        event.shopId = shop.getShopId();
        event.latitude = shop.getShopLatitude();
        event.longitude = shop.getShopLongitude();
        event.shopName = shop.getShopName();
        event.city = shop.getShopCity();
        event.address = shop.getShopAddress();


        //任务门店
        event.shopType = GlobalParams.VISIT_SHOP_TASK;
        //店铺状态
        event.shopTaskState = shop.getTaskState();
        //拜访状态
        event.shopTaskType = shop.getTaskType();
        //日期
        event.dayInterval = shop.getDayInterval();

        EventBus.getDefault().post(event);
    }


    public static void postFinishTask(String shopId) {
        SelectTaskEvent event = new SelectTaskEvent();
        event.shopId = shopId;
        event.type = SelectTaskEvent.type_finish;
        EventBus.getDefault().post(event);
    }

    /**
     * 发送常用的事件
     */
    public static void postCommonEvent(int type) {
        CommonEvent event = new CommonEvent();
        event.eventType = type;
        postDefaultEvent(event);
    }

    public static void postDefaultEvent(Object event) {
        EventBus.getDefault().post(event);
    }


}
