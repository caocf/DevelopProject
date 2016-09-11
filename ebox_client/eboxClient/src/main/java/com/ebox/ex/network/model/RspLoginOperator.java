package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.Operators;

public class RspLoginOperator extends BaseRsp {

    private Operators data;

    public Operators getData() {
        return data;
    }

    public void setData(Operators data) {
        this.data = data;
    }
}
