package com.xhl.bqlh.model.base;

import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.helper.EventHelper;

/**
 * Created by Sum on 16/3/11.
 */
public class BaseModel {

    private int resultCode;

    private String message;

    public boolean isSuccess() {
        if (resultCode == 4 || resultCode == 2) {
            EventHelper.postCommonEvent(CommonEvent.ET_RELOGIN);
            return false;
        }
        if (resultCode == 1) {
            return true;
        } else {
            return false;
        }

    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
