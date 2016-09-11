package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.OperatorBalance;

public class RspUpdateOperatorBalance extends BaseRsp {

    private OperatorBalance data;

    public OperatorBalance getData() {
        return data;
    }

    public void setData(OperatorBalance data) {
        this.data = data;
    }
}
