package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class QueryWorkInfoRspModel {
    private String creat_at;
    private String idcard;
    private Long order_id;
    private int state;
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreat_at() {
        return creat_at;
    }

    public void setCreat_at(String creat_at) {
        this.creat_at = creat_at;
    }



    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }



    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }
}
