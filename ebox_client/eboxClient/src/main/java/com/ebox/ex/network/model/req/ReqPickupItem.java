package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqPickupItem extends BaseReq {

    private String pickup_id;
    private String password;
    private String user_type;
    private String username;


    public String getPickup_id() {
        return pickup_id;
    }

    public void setPickup_id(String pickup_id) {
        this.pickup_id = pickup_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
