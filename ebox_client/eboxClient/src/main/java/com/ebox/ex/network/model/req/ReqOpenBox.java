package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.TimeOutOrderType;

public class ReqOpenBox extends BaseReq {
    private TimeOutOrderType item;

    public TimeOutOrderType getItem() {
        return item;
    }

    public void setItem(TimeOutOrderType item) {
        this.item = item;
    }

}
