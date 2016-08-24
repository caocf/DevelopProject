package com.xhl.world.mvp.domain;

import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.mvp.presenters.Presenter;

/**
 * Created by Sum on 16/1/5.
 */
public interface OrderManagerUserCase extends UserCase {

    void setPresenter(Presenter presenter);

    void setViewTag(int tag);//表示当前查询的订单类型

    void orderCancel(Order order);//取消订单

    void orderConfirm(Order order);//确认收货

    void orderNotify(Order order);//通知订单发货

}
