package com.xhl.world.ui.event;

import com.xhl.world.model.AdvModel;
import com.xhl.world.model.user.UserInfo;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/3.
 */
public class EventBusHelper {

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

    //重新登录
    public static void postReLogin() {
        GlobalEvent event = new GlobalEvent<>();
        event.setEventType(EventType.Event_ReLogin);
        EventBus.getDefault().post(event);
    }
   //退出登录
    public static void postExitLogin() {
        GlobalEvent event = new GlobalEvent<>();
        event.setEventType(EventType.Event_Exit_Login);
        EventBus.getDefault().post(event);
    }
    //重新加载用户信息
    public static void postReloadUserInfo() {
        GlobalEvent event = new GlobalEvent<>();
        event.setEventType(EventType.Event_Reload_Info);
        EventBus.getDefault().post(event);
    }

    //检测版本
    public static void postCheckVersion() {
        GlobalEvent<String> event = new GlobalEvent<>();
        event.setEventType(EventType.Event_CheckVersion);
        EventBus.getDefault().post(event);
    }

    //通知购物车可见的时候刷新数据
    public static void posRefreshShopCarEvent() {
        ShoppingEvent event = new ShoppingEvent();
        event.action = EventType.Shopping_Event_Refresh;
        EventBus.getDefault().post(event);
    }

    public static void post(int type, String tag) {
        GlobalEvent<String> event = new GlobalEvent<>();
        event.setEventType(type);
        event.setObject(tag);
        EventBus.getDefault().post(event);
    }

    //大图片详情
    public static void postImageDetailsEvent(String imageUrl) {
        GlobalEvent<String> event = new GlobalEvent<>();
        event.setEventType(EventType.Event_SingleImageDetails);
        event.setObject(imageUrl);
        EventBus.getDefault().post(event);
    }

    //一组大图片详情
    public static void postImageUrlsEvent(ArrayList<String> urls) {
        GlobalEvent event = new GlobalEvent<>();
        event.setEventType(EventType.Event_MulitImageDetails);
        event.setObject(urls);
        EventBus.getDefault().post(event);
    }

    //扫描数据返回
    public static void postScanEvent(String scan) {
        GlobalEvent<String> event = new GlobalEvent<>();
        event.setEventType(EventType.Event_Scan);
        event.setObject(scan);
        EventBus.getDefault().post(event);
    }

    //回到首页
    public static void postGoHomeEvent() {
        GlobalEvent<String> event = new GlobalEvent<>();
        event.setEventType(EventType.Event_Go_HOME);
        event.setObject(null);
        EventBus.getDefault().post(event);
    }

    //重新加载
    public static void postReloadData() {
        ReLoadEvent event = new ReLoadEvent();
        EventBus.getDefault().post(event);
    }

    //保存登陆信息
    public static void postSaveLoginInfoEvent(UserInfo info,String hint) {
        OtherLoginEvent event = new OtherLoginEvent();
        event.info = info;
        event.tip = hint;
        EventBus.getDefault().post(event);
    }

    //搜索内容
    public static void postSearchContent(int searchType, String searchContent) {
        SearchItemEvent event = new SearchItemEvent();
        event.search_content = searchContent;
        event.search_type = searchType;
        EventBus.getDefault().post(event);
    }

    //商品详情
    public static void postProductDetails(String productId) {
        ProductDetailsEvent event = new ProductDetailsEvent();
        event.productId = productId;
        EventBus.getDefault().post(event);
    }

    //广告处理
    public static void postAdv(AdvModel adv) {
        AdvEvent event = new AdvEvent();
        event.adv = adv;
        EventBus.getDefault().post(event);
    }
}
