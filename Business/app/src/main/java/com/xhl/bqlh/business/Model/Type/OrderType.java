package com.xhl.bqlh.business.Model.Type;

/**
 * Created by Sum on 16/4/21.
 */
public interface OrderType {

    int order_type_all = 0;
    int order_type_car = 12;//车削
    int order_type_self = 11;//拜访
    int order_type_shop = 1;//门店

    int order_state_all = 0;//不过滤状态
    int order_state_not_send = 1;//未发货
    int order_state_have_send = 2;//已发货
    int order_state_finish = 3;//已完成

}
