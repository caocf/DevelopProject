package com.xhl.bqlh.business.view.event;

/**
 * Created by Sum on 16/4/25.
 */
public class ReLoginEvent {

    private int tag;

    public ReLoginEvent(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
