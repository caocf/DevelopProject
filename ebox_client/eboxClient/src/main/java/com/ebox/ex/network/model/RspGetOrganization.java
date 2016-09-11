package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.OrganizationType;
import com.ebox.ex.network.model.base.type.Organizations;

public class RspGetOrganization extends BaseRsp {
    private Organizations data;


    public Organizations getData() {
        return data;
    }

    public void setData(Organizations data) {
        this.data = data;
    }
}