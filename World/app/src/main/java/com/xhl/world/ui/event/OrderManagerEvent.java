package com.xhl.world.ui.event;

import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerEvent {

    public static final int ORDER_ACTION_PAY = 0;//支付
    public static final int ORDER_ACTION_CANCEL = 1;//取消订单
    public static final int ORDER_ACTION_sh = 2;//确认收货
    public static final int ORDER_ACTION_wl = 3;//查询物流
    public static final int ORDER_ACTION_tx = 4;//提醒发货
    public static final int ORDER_ACTION_pj = 5;//去评价
    public static final int ORDER_ACTION_th = 6;//申请退货
    public static final int ORDER_ACTION_DETAILS = 7;//订单详情

    public static final int ORDER_ACTION_SINGLE_DETAILS = 8;//单个订单详情针对收货完成后操作

    private Order order;

    private OrderDetail orderDetail;//单个商品

    private int view_tag;//当前操作的ViewTag，避免多个Fragment同时注册事件时做相同操作

    private int order_action;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getOrder_action() {
        return order_action;
    }

    public void setOrder_action(int order_action) {
        this.order_action = order_action;
    }

    public int getView_tag() {
        return view_tag;
    }

    public void setView_tag(int view_tag) {
        this.view_tag = view_tag;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }
}
