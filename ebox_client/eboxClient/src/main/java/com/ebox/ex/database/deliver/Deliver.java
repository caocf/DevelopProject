package com.ebox.ex.database.deliver;

public class Deliver {
    private Long _id;
    private String itemId;
    private String box_code;
    private String operator;
    private String msisdn;
    private Integer state;
    private Integer fee;

    private String order_id;
    private String time;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getOperatorId() {
        return operator;
    }

    public void setOperatorId(String operator) {
        this.operator = operator;
    }

    public String getTelephone() {
        return msisdn;
    }

    public void setTelephone(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
