package com.xhl.bqlh.model.event;

import java.util.ArrayList;

/**
 * Created by Sum on 16/7/7.
 */
public class ImageEvent {

    public static final int DEFAULT = 0;

    public static final int SINGLE_IMAGE = 1;

    public static final int MULIT_IMAGE = 2;

    public ImageEvent(int type) {
        this.type = type;
    }

    private int type;

    private ArrayList<String> urls;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
