package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.AlarmInfoType;

public class ReqAlarmReport extends BaseReq {

    private String box_code;
    private String content;
    private int alarm_code;
    private int alarm_state;

    public int getAlarm_state() {
        return alarm_state;
    }

    public void setAlarm_state(int alarm_state) {
        this.alarm_state = alarm_state;
    }

    public int getAlarm_code() {
        return alarm_code;
    }

    public void setAlarm_code(int alarm_code) {
        this.alarm_code = alarm_code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }
}
