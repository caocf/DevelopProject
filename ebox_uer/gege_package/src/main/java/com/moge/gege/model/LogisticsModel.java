package com.moge.gege.model;

import java.io.Serializable;

public class LogisticsModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String message;
    private String time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
