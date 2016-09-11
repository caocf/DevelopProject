package com.moge.gege.model.enums;

public interface OrderStatusType
{

    //    ORDER_INIT = 1001 #用户下单
//    ORDER_CANCEL =1901 #订单取消
//
//    ORDER_WAIT_PAY = 2001 #等待支付
//    ORDER_PAYING = 2101 #支付中
//    ORDER_WAIT_CONFIRM_PAY = 2501 #支付等待确认
//    ORDER_PAY_CONFIRM = 2502 #支付确认
//    ORDER_PAY_SUUCED = 2801 #支付成功
//    ORDER_PAY_ERROR = 2901 #支付失败
//    ORDER_PAY_CANCEL = 2902 #支付取消
//
//    ORDER_WAIT_DELIVERY = 3001 #等待发货
//    ORDER_SORTING = 3010  #分拣
//    ORDER_IS_DELIVERY = 3101 #配送中
//    ORDER_IS_CABINET = 3102 #快递到达快递柜
//    ORDER_IS_RECEIPT = 3501 #用户已收货
//    ORDER_DELIVERY_FINISH = 3901 #配送完成

    public static final int NOT_PAYED = 2001;
    public static final int PAY_ING = 2101;
    public static final int PAY_FAILED = 2901;
    public static final int PAY_CANCEL = 2902;
    public static final int WAIT_DELIVERY = 3001;
    public static final int SORTING = 3010;

}
