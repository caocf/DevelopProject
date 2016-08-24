package com.xhl.world.model.Entities;

import java.io.Serializable;

/**
 * Created by Sum on 16/1/12.
 */
public class QueryConditionEntity implements Serializable {

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private String type;//查询类型
    private String queryParmas;//查询参数

    private String orderBy = "";//排序类型
    private String sortBy = DESC;//升序降序排序
    private String pageSize;
    private String pageNo;


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getQueryParmas() {
        return queryParmas;
    }

    public void setQueryParmas(String queryParmas) {
        this.queryParmas = queryParmas;
    }
}
