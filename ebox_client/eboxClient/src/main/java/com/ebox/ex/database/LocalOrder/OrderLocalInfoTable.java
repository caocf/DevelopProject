package com.ebox.ex.database.LocalOrder;

import android.provider.BaseColumns;


public final class OrderLocalInfoTable implements BaseColumns {
    private OrderLocalInfoTable() {

    }

    public static final String TABLE_NAME = "LocalOrderInfo";
    public static final String operator_telephone = "operator_telephone";
    public static final String customer_telephone = "customer_telephone";
    public static final String order_id = "order_id";
    public static final String box_code = "box_code";
    public static final String item_id = "item_id";
    public static final String password = "password";
    public static final String pick_id = "pick_id";//授权码
    public static final String user_type = "user_type";
    public static final String time_out = "time_out";
    public static final String delivery_at = "delivery_at";
    public static final String order_state = "order_state";//订单状态

    //扩展列
    public static final String data1 = "data1";//取件时间
    public static final String data2 = "data2";
    public static final String data3 = "data3";


    /**
     * STATE_0 :订单生成
     */
    public static final Integer STATE_CREATE = 0;
    /**
     * STATE_1 :订单已取走,待同步
     */
    public static final Integer STATE_WAIT_UPLOAD = 1;

    public static String getCreateStr() {

        String sqlStr = "CREATE TABLE " + OrderLocalInfoTable.TABLE_NAME + " ("
                + OrderLocalInfoTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OrderLocalInfoTable.order_id + " TEXT,"
                + OrderLocalInfoTable.item_id + " TEXT,"
                + OrderLocalInfoTable.box_code + " TEXT,"
                + OrderLocalInfoTable.operator_telephone + " TEXT,"
                + OrderLocalInfoTable.customer_telephone + " TEXT,"
                + OrderLocalInfoTable.password + " TEXT,"
                + OrderLocalInfoTable.pick_id + " TEXT,"
                + OrderLocalInfoTable.user_type + " TEXT,"
                + OrderLocalInfoTable.time_out + " TEXT DEFAULT 0,"
                + OrderLocalInfoTable.delivery_at + " TEXT,"
                + OrderLocalInfoTable.order_state + " INTEGER,"
                + OrderLocalInfoTable.data1 + " TEXT,"
                + OrderLocalInfoTable.data2 + " TEXT,"
                + OrderLocalInfoTable.data3 + " TEXT"
                + ");";
        return sqlStr;
    }


}
