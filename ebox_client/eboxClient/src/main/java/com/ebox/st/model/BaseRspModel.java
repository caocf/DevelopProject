package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class BaseRspModel {
    private Boolean success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
