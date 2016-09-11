package com.ebox.Anetwork;

import com.android.volley.VolleyError;

public abstract interface ResponseEventHandler<T>
{
    public abstract void onResponseSuccess(T result);

    public abstract void onResponseError(VolleyError error);
}
