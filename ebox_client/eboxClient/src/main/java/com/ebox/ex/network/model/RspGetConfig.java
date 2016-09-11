package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.ServiceConfig;

public class RspGetConfig extends BaseRsp {

    private ServiceConfig data;


    public ServiceConfig getData() {
        return data;
    }

    public void setData(ServiceConfig data) {
        this.data = data;
    }
}
