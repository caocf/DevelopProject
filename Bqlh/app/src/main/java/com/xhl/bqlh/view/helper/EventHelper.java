package com.xhl.bqlh.view.helper;

import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.model.event.DetailsEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/4/20.
 */
public class EventHelper {


    //通用事件发送
    public static void postCommonEvent(int tag) {
        CommonEvent event = new CommonEvent();
        event.setEventTag(tag);
        postDefaultEvent(event);
    }

    //刷新购物车
    public static void postReLoadCarEvent() {
        CommonEvent event = new CommonEvent();
        event.setEventTag(CommonEvent.ET_RELOAD_CAR);
        postDefaultEvent(event);
    }

    //刷新订单数量
    public static void postReLoadOrderNumEvent() {
        CommonEvent event = new CommonEvent();
        event.setEventTag(CommonEvent.ET_RELOAD_ORDER_NUM);
        postDefaultEvent(event);
    }

    //刷新收藏数量
    public static void postReLoadCollectionNumEvent() {
        CommonEvent event = new CommonEvent();
        event.setEventTag(CommonEvent.ET_RELOAD_COLLECTION_NUM);
        postDefaultEvent(event);
    }

    public static void postDefaultEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    //商品
    public static void postProduct(String productId) {
        DetailsEvent event = new DetailsEvent();
        event.setTag(DetailsEvent.TAG_PRODUCT);
        event.setInfoId(productId);
        postDefaultEvent(event);
    }

    //店铺
    public static void postShop(String shopId) {
        DetailsEvent event = new DetailsEvent();
        event.setTag(DetailsEvent.TAG_SHOP);
        event.setInfoId(shopId);
        postDefaultEvent(event);
    }


    //广告处理
    public static void postAdv(AdInfoModel adv) {

    }

}
