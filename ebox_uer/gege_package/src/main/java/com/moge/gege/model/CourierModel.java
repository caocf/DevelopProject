package com.moge.gege.model;

import java.io.Serializable;


public class CourierModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String delivery_type;
    private String name;
    private String mobile;
    private String trading_id;
    private String delivery_logo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTrading_id() {
        return trading_id;
    }

    public void setTrading_id(String trading_id) {
        this.trading_id = trading_id;
    }

    public String getDelivery_logo() {
        return delivery_logo;
    }

    public void setDelivery_logo(String delivery_logo) {
        this.delivery_logo = delivery_logo;
    }
}
