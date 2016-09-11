package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.ShowConfig;

public class RspGetShowConfig extends BaseRsp {

    private ShowConfig data;


    public ShowConfig getData() {
        return data;
    }

    public void setData(ShowConfig data) {
        this.data = data;
    }
}
