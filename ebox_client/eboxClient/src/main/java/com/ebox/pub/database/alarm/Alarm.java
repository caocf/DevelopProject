package com.ebox.pub.database.alarm;


public class Alarm {
    private Long _id;
    private String boxId;
    private Integer alarmCode;
    private int alarm_type;//0:系统告警  1：业务告警
    private String content;
    private int state;// 0 告警中;1:手工处理完告警  2 告警自动恢复

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public Integer getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(Integer alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(int alarm_type) {
        this.alarm_type = alarm_type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}