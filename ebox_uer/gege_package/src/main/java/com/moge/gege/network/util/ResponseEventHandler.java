package com.moge.gege.network.util;

import com.android.volley.VolleyError;

public abstract interface ResponseEventHandler<T>
{

    public abstract void onResponseSucess(BaseRequest<T> request, T result);

    public abstract void onResponseError(VolleyError error);
}
