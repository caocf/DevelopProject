package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.OrderInfo;

public class RspConfirmDelivery extends BaseRsp{
    private OrderInfo data;
    public OrderInfo getData() {
        return data;
    }

    public void setData(OrderInfo data) {
        this.data = data;
    }
}
