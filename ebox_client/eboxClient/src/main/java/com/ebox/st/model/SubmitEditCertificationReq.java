package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class SubmitEditCertificationReq {
    private Workflow data;
    private String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setData(Workflow data) {
        this.data = data;
    }
    public Workflow getData(){return data;}
}
