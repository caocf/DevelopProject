package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.ItemRackBoxType;

import java.util.List;

/**
 * Created by Android on 2015/10/10.
 */
public class ReqCheckLocalConfig extends BaseReq {

    private List<ItemRackBoxType> racks;

    public List<ItemRackBoxType> getRack() {
        return racks;
    }

    public void setRack(List<ItemRackBoxType> rack) {
        this.racks = rack;
    }
}
