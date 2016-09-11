package com.moge.gege.config;

import com.moge.gege.R;

public class GlobalConfig
{
    private static float mLatitude = 0;
    private static float mLongitude = 0;
    private static int mCityId = 3201; // nanjing city
    private static boolean mLogin = false;

    // image suffix config for qiniu
    public static final String IMAGE_STYLE480 = "/style480.png";
    public static final String IMAGE_STYLE300_300 = "/style300x300.png";
    public static final String IMAGE_STYLE300 = "/style300.png";
    public static final String IMAGE_STYLE150_150 = "/style150x150.png";
    public static final String IMAGE_STYLE150 = "/style150.png";
    public static final String IMAGE_STYLE90_90 = "/style90x90.png";

    // share config
    public static final String WXAppID = "wx68537e78d9031663";
    public static final String WXAppSecret = "9d3ae6a68d429e0db4f8c32ad81bc54d";
    public static final String QQAppID = "1103410498";
    public static final String QQAppSecret = "VXVL1Cpmvasxxqni";

    // delete webview padding
    public static final String DELETE_WEBVIEW_PADDING = "<style type='text/css'>html, body{padding:0px;margin:0px;}</style>";

    /**
     * local service and category datasource
     */
    public static final int mLocalServiceName[] = {
            R.string.scan_twodimension_code, R.string.activity,
            R.string.convenience_service, R.string.express_delivery,
            R.string.send_water, R.string.dry_cleaning, R.string.second_hand,
            R.string.rent_house, R.string.pet, R.string.together,
            R.string.carpool, R.string.marriage_dating };

    public static final int mLocalServiceResId[] = { R.drawable.icon_activity,
            R.drawable.icon_activity, R.drawable.icon_convenience_service,
            R.drawable.icon_express_delivery, R.drawable.icon_send_water,
            R.drawable.icon_dry_cleaning, R.drawable.icon_secondhand,
            R.drawable.icon_renthouse, R.drawable.icon_pet,
            R.drawable.icon_together, R.drawable.icon_carpool,
            R.drawable.icon_marriage_dating };

    /**
     * activity jump flag
     */
    public static final int INTENT_LOGIN = 0;
    public static final int INTENT_SELECT_COMMUNITY = 1;
    public static final int INTENT_PUBLISH_TOPIC = 2;
    public static final int INTENT_SELECT_IMAGE = 3;
    public static final int INTENT_CROP_IMAGE = 4;
    public static final int INTENT_GIFT_LIST = 5;
    public static final int INTENT_ALBUM_SECONDARY = 6;
    public static final int INTENT_SELECT_DISTRICT = 7;
    public static final int INTENT_ADD_NEW_ADDRESS = 8;
    public static final int INTENT_SELECT_ADDRESS = 9;
    public static final int INTENT_SHOPPING_CART = 10;
    public static final int INTENT_SELECT_ALBUM = 11;
    public static final int INTENT_TAKE_CAMERA = 12;
    public static final int INTENT_TOPIC_DETAIL = 13;
    public static final int INTENT_EDIT_USERINFO = 14;
    public static final int INTENT_ORDER_DETAIL = 15;
    public static final int INTENT_SELECT_COUPON = 16;
    public static final int INTENT_SELECT_PAYTYPE = 17;
    public static final int INTENT_RECOMMEND_BOARD = 18;
    public static final int INTENT_NEW_USERINFO = 19;
    public static final int INTENT_RECHARGE = 20;
    public static final int INTENT_EDIT_ADDRESS = 21;

    /**
     * broadcast
     */

    public static final String BROADCAST_ACTION_FINISH = "com.moge.gege.intent.action.FINISH";
    public static final String BROADCAST_ACTION_PUSHMSG = "com.moge.gege.intent.action.PUSHMSG";
    public static final String BROADCAST_ACTION_PUBLISH_TOPIC = "com.moge.gege.intent.action.PUBLISH_TOPIC";
    public static final String BROADCAST_ACTION_DESTROY_SERVICE = "com.moge.gege.appservice.destroy";

    public static float getLatitude()
    {
        return mLatitude;
    }

    public static void setLatitude(float latitude)
    {
        mLatitude = latitude;
    }

    public static float getLongitude()
    {
        return mLongitude;
    }

    public static void setLongitude(float longitude)
    {
        mLongitude = longitude;
    }

    public static int getmCityId()
    {
        return mCityId;
    }

    public static void setCityId(int cityId)
    {
        mCityId = cityId;
    }
}
