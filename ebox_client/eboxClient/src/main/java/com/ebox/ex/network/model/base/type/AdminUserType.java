package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class AdminUserType implements Serializable {
    private static final long serialVersionUID = 1L;
    private String admin_name;
    private String admin_password;

    public String getAdminName() {
        return admin_name;
    }

    public void setAdminName(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdminPassword() {
        return admin_password;
    }

    public void setAdminPassword(String admin_password) {
        this.admin_password = admin_password;
    }


}
