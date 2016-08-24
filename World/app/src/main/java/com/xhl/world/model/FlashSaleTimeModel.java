package com.xhl.world.model;

import com.xhl.world.model.Type.FlashSaleType;

/**
 * Created by Sum on 15/12/11.
 */
public class FlashSaleTimeModel {

    private String time;
    private int state;

    public String content_tag;//标示该时间段内请求的一个标示

    public void setTime(String time) {
        this.time = time;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    public String getShowTime() {
        //2015-12-14 20:00:00
        if (time.length() > 5) {
            int startIndex = time.indexOf(" ");
            int endIndex = time.lastIndexOf(":");

            return time.substring(startIndex+1, endIndex);
        }

        return time;
    }

    public String getStateDes() {
        String des = "";
        if (state == FlashSaleType.Flash_sale_finish) {
            des = "已开抢";
        } else if (state == FlashSaleType.Flash_sale_start) {
            des = "抢购中";
        } else if (state == FlashSaleType.Flash_sale_unstart) {
            des = "即将开抢";
        }

        return des;
    }
}
