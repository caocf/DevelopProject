package com.xhl.bqlh.doman.callback;

/**
 * Created by Sum on 16/4/25.
 */
public interface ContextValue {

    Object getValue(int type);

    void showValue(int type, Object obj);
}
