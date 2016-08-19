package com.xhl.bqlh.model;

/**
 * Created by Sum on 16/7/5.
 */
public class SearchFastModel {

    public int searchTyp;

    private String id;
    private String sid;
    private String productName;

    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }
}
