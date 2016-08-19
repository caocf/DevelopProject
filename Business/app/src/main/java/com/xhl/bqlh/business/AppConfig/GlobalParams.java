package com.xhl.bqlh.business.AppConfig;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Sum on 16/3/17.
 */
public class GlobalParams {

    public static BDLocation mLocation;
    public static LatLng mSelfLatLng;
    public static String mSelfLatLngStr;
    public static String mAddr = "";//位置
    public static String mNearBy = "";//附近

    public static final String BaiduMap_AK = "mU3Cw0kVm75OiSvuLIGMumnfRboobEy5";
    //LBS数据
//    public static final String BaiduMap_LBS_AK = "6bFDKjnTRTgw0Q0UQmwhM7PZyu9bcWKA";
    public static final String BaiduMap_LBS_AK = "flNNEmaToSVWMxUh5hmoewB3Hk3lGUaR";
    public static final int BaiduMap_LBS_TABLE = 119799;
    public static final int BaiduMap_LBS_DISTANCE = 1000;

    public static final String LeanCloud_id = "EOqGTo6y7v6igwKuyxdYgrFU-gzGzoHsz";
    public static final String LeanCloud_key = "8gWNeUqyfH7vynJRlWDAmkTV";


    //Intent
    public static final String Intent_shop_id = "shop_id";
    public static final String Intent_shop_name = "shop_name";
    public static final String Intent_shop_latitude = "shop_latitude";
    public static final String Intent_shop_longitude = "shop_longitude";
    public static final String Intent_shop_city = "shop_city";
    public static final String Intent_shop_street = "shop_street";
    public static final String Intent_shop_order_id = "shop_order_id";

    //店铺类型
    public static final String INTENT_VISIT_SHOP_TYPE = "visit_shop_type";
    public static final int VISIT_SHOP_TASK = 0;//拜访的客户
    public static final int VISIT_SHOP_SEARCH = 1;//搜索的客户
    public static final int VISIT_SHOP_SIGN = 2;//考勤地点


    public static final String Intent_visit_client_state = "visit_client_state";//拜访的状态
    public static final String Intent_visit_client_type = "visit_client_type";//拜访的类型
    public static final String Intent_visit_client_time = "visit_client_time";//拜访的时间


}
