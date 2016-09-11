package com.moge.gege.data;

public interface DBConstants
{
    String NAME = "gege.db";
    int VERSION = 22;

    interface Table_Names
    {
        String SHOPCART = "shopcart";
        String PUSHMSG = "pushmsg";
    }

    interface SHOPCART
    {
        String ID = "_id";
        String ATTACHMENTS = "attachments";
        String DISCOUNT_PRICE = "discount_price";
        String NUM = "num";
        String TITLE = "title";
        String PRICE = "price";
        String IS_SELECTED = "is_selected";
        String BUY_NUM = "buy_num";
        String USER_ACCOUNT_ID = "usercount_id";
        String MERCHANT_ID = "merchant_id";
        String PROMOTION_ID = "promotion_id";
    }

    interface PushMsg
    {
        String UID = "uid";
        String TAG = "tag";
        String MSGTYPE = "msg_type";
        String HAVEREAD = "have_read";
        String AVATAR = "avatar";
        String COUNT = "count";
        String TITLE = "title";
        String CONTENT = "content";
        String TIME = "time";
        String IS_NOTIFY = "is_notify";
    }

}
