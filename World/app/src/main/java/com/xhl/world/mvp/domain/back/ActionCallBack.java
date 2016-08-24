package com.xhl.world.mvp.domain.back;

/**
 * Created by Sum on 15/12/3.
 */
public interface ActionCallBack<T> {

    /**
     *  用户动作成功回调
     * @param data
     */
    public void onSuccess(T data);


    /**
     * 用户动作失败回调
     * @param errorType
     * @param errorMsg
     */
    public void onFailed(String errorType, String errorMsg);
}
