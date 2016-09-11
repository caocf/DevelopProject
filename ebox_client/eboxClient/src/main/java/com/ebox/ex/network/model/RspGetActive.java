package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.AcitveType;
import com.ebox.ex.network.model.base.type.ActivitiesType;

import java.util.List;

public class RspGetActive extends BaseRsp {

    private ActivitiesType data;

    public ActivitiesType getData() {
        return data;
    }

    public void setData(ActivitiesType data) {
        this.data = data;
    }
}
