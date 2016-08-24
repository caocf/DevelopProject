package com.xhl.world.model.Base;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Sum on 15/12/3.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ResponseModel<T> extends BaseModel {

    private T resultObj;

    public T getResultObj() {
        return resultObj;
    }

}
