package com.xhl.world.model.Base;

import android.text.TextUtils;

import com.xhl.world.ui.event.EventBusHelper;

/**
 * Created by Sum on 15/11/27.
 */
public class BaseModel {

    private int resultCode;//1：成功 0：失败
    private String message;//返回描述


    public boolean isSuccess() {
        if (!TextUtils.isEmpty(message) && message.contains("登录")) {
            EventBusHelper.postReLogin();
        }
        return resultCode == 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
