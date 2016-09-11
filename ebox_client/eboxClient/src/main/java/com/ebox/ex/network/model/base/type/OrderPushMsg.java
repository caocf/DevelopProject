package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class OrderPushMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long order_id;
    private String oper_notice;

    public Long getOrderId() {
        return order_id;
    }

    public void setOrderId(Long order_id) {
        this.order_id = order_id;
    }

    public String getOperNotice() {
        return oper_notice;
    }

    public void setOperNotice(String oper_notice) {
        this.oper_notice = oper_notice;
    }

}
