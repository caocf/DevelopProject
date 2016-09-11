package com.ebox.ex.network.model.base.type;

import java.io.Serializable;
import java.util.List;

/**
 * Created by prin on 2015/11/2.
 */
public class NotesType implements Serializable{

    private List<RechargeNoteType> items;
    private String page;
    private String page_size;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<RechargeNoteType> getItems() {
        return items;
    }

    public void setItems(List<RechargeNoteType> items) {
        this.items = items;
    }
}
