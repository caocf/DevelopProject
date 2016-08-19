package com.xhl.bqlh.model.base;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Sum on 16/3/11.
 */
@HttpResponse(parser = JsonResParser.class)
public class ResponseModel<T> extends BaseModel {

    private T obj;

    private List<T> objList;

    private int pageSize;

    public T getObj() {
        return obj;
    }

    public List<T> getObjList() {
        return objList;
    }

    public int getPageSize() {
        return pageSize;
    }
}
