package com.xhl.world.config;

/**
 * Created by Sum on 15/12/3.
 */

public class Constant {
    public enum Config {
        LOCAL, TEST, DEV, RELEASE
    }

    public static final boolean isDebug = true;

    public static final Config API = Config.TEST;//服务器配置数据

    //关于空港
    public static final String URL_about = "http://www.wawscm.com/mobileWeb/appPage/about.html";
//    public static final String URL_about = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/about.html";
    //签到
    public static final String URL_sign = "http://www.wawscm.com/mobileWeb/appPage/signInApp.html";
//    public static final String URL_sign = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/signInApp.html";
    //店铺
    public static final String URL_shop = "http://www.wawscm.com/mobileWeb/appPage/shopDetail.html?shopId=";
//    public static final String URL_shop = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/shopDetail.html?shopId=";
    //空港注册协议
    public static final String URL_protocol = "http://www.wawscm.com/mobileWeb/appPage/protocol2.html";
//    public static final String URL_protocol = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/protocol2.html";
    //优惠券使用规则
    public static final String URL_coupon_rule = "http://www.wawscm.com/mobileWeb/appPage/couponsRule.html";
//    public static final String URL_coupon_rule = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/couponsRule.html";
    //优惠券领取
    public static final String URL_coupon_take = "http://www.wawscm.com/mobileWeb/appPage/coupons.html";
//    public static final String URL_coupon_take = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/coupons.html";
    //温馨提示协议
    public static final String URL_tip = "http://www.wawscm.com/mobileWeb/appPage/protocol1.html";
//    public static final String URL_tip = "http://192.168.1.131:8080/SHZC/mobileWeb/appPage/protocol1.html";


    //全局变量设置
    public static final String intent_message_type = "intent_message_type";
    public static final String intent_system_message = "intent_system_message";//系统通知
    public static final String intent_chat_message = "intent_chat_message";//聊天消息
    public static final String intent_message_data = "intent_message_data";//消息内容

    public static final String intent_change_to_main = "intent_message_data";//跳转到首页
}
