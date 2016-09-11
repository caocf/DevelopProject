package com.ebox.ex.network.model;


import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.RechargeOrderId;

public class RspRechargeOrderId extends BaseRsp {

    private RechargeOrderId data;


    public RechargeOrderId getData() {
        return data;
    }

    public void setData(RechargeOrderId data) {
        this.data = data;
    }
}
