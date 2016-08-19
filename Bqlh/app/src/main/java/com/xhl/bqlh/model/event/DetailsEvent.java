package com.xhl.bqlh.model.event;

/**
 * Created by Sum on 16/7/7.
 */
public class DetailsEvent {

    public static final int TAG_PRODUCT = 1;
    public static final int TAG_SHOP = 2;
    private int tag;
    private String infoId;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
