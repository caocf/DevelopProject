package com.ebox.ex.network.model;


import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.RechargeOrder;

public class RspRechargePlayOrder extends BaseRsp {

    private RechargeOrder data;


    public RechargeOrder getData() {
        return data;
    }

    public void setData(RechargeOrder data) {
        this.data = data;
    }
}
