package com.moge.gege.model;

import java.util.List;

public class CourierListModel
{
    private List<CourierModel> deliverys;
    private String previous_cursor;
    private String next_cursor;

    public List<CourierModel> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(List<CourierModel> deliverys) {
        this.deliverys = deliverys;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }
}
