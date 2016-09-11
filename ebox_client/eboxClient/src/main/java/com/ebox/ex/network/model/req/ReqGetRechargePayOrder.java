package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.DeviceType;

import java.util.List;

/**
 * Created by Android on 2015/9/22.
 */
public class ReqGetRechargePayOrder extends BaseReq {

    public float total_fee;
    public int total_num;
    public int pay_type;
    public List<String>order_ids;
    public String service;
    public String return_url;
    public DeviceType device_info;

}
