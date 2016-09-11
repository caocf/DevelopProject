package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.OrderInfo;

import java.util.List;

public class RspFirstUpdateLocalOrder extends BaseRsp {
    private List<OrderInfo> data;


    public List<OrderInfo> getData() {
        return data;
    }

    public void setData(List<OrderInfo> data) {
        this.data = data;
    }
}
