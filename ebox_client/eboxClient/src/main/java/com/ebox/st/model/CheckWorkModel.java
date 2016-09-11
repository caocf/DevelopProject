package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/23.
 */
public class CheckWorkModel {
    private Workflow data;
    private int state;
    private String order_id;
    private String remark;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Workflow getData() {
        return data;
    }

    public void setData(Workflow data) {
        this.data = data;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
