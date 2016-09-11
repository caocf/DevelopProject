package com.ebox.ex.network.model.base.type;

import java.io.Serializable;
import java.util.List;

public class QueryItemInfoType implements Serializable {

    private String next_cursor;
    private String prev_cursor;
    private Integer page_size;

    private List<OrderInfo> items;

    public List<OrderInfo> getItems() {
        return items;
    }

    public void setItems(List<OrderInfo> items) {
        this.items = items;
    }

    public String getPrev_cursor() {
        return prev_cursor;
    }

    public void setPrev_cursor(String prev_cursor) {
        this.prev_cursor = prev_cursor;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }
}
