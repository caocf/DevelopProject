package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.TimeOutOrderType;

public class RspPickupItem extends BaseRsp {
    private TimeOutOrderType data;

    public TimeOutOrderType getItem() {
        return data;
    }

    public void setItem(TimeOutOrderType data) {
        this.data = data;
    }

}
