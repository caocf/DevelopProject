package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqUpdateVersion extends BaseReq {
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}