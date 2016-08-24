package com.xhl.world.model;

/**
 * Created by Sum on 16/3/9.
 */
public class LogisticsDelivery {
    private String context;
    private String time;
    private String ftime;
    private boolean isLeast;

    public String getContext() {
        return context;
    }

    public String getTime() {
        return time;
    }

    public String getFtime() {
        return ftime;
    }

    public boolean isLeast() {
        return isLeast;
    }

    public void setLeast(boolean least) {
        isLeast = least;
    }
}
