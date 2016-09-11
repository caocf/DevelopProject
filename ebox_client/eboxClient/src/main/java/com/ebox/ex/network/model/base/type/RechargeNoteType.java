package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

/**
 * Created by prin on 2015/11/2.
 * 充值记录的数据模型
 */
public class RechargeNoteType implements Serializable {

    private String remark;
    private Long old_value;
    private Long charge_id;
    private Long value;
    private String charge_channel_zh;
    private Long act_amount;
    private Integer charge_channel;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOld_value() {
        return old_value;
    }

    public void setOld_value(Long old_value) {
        this.old_value = old_value;
    }

    public Long getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(Long charge_id) {
        this.charge_id = charge_id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getCharge_channel_zh() {
        return charge_channel_zh;
    }

    public void setCharge_channel_zh(String charge_channel_zh) {
        this.charge_channel_zh = charge_channel_zh;
    }

    public Long getAct_amount() {
        return act_amount;
    }

    public void setAct_amount(Long act_amount) {
        this.act_amount = act_amount;
    }

    public Integer getCharge_channel() {
        return charge_channel;
    }

    public void setCharge_channel(Integer charge_channel) {
        this.charge_channel = charge_channel;
    }
}
