package com.xhl.bqlh.business.Model.Base;

import com.xhl.bqlh.business.view.event.ReLoginEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/3/11.
 */
public class BaseModel {

    private int resultCode;

    private String message;

    public boolean isSuccess() {
        if (resultCode == 4 || resultCode == 2) {
            EventBus.getDefault().post(new ReLoginEvent(resultCode));
            return false;
        }
        return resultCode == 1;

    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
