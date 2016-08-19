package com.xhl.bqlh.model.app;

/**
 * Created by Sum on 16/7/5.
 */
public class SearchParams {

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public String sku;

    public String shopId;

    public String productName;

    public String categoryId;

    public String brandId;

    public String myParam;//gp 设计

    private String orderBy = "";
    private String sortBy = DESC;

    public int pageSize;
    public int pageNum;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
