package com.xhl.bqlh.doman;


import com.xhl.bqlh.doman.callback.ContextValue;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by Sum on 16/4/25.
 */
public abstract class BaseValue implements Serializable{

    protected ContextValue mValue;

    protected int mPageIndex = 1;
    protected final int mPageSize= 14;
    protected int mTotalSize = 0;


    //Snack或Toast提示
    public static final int TYPE_TOAST = -1;
    //进度条提示
    public static final int TYPE_DIALOG_PROGRESS_SHOW = -2;
    //关闭对话框
    public static final int TYPE_DIALOG_HIDE = -3;
    //加载中
    public static final int TYPE_DIALOG_LOADING = -4;

    public static final int TYPE_NO_MORE = -5;

    public BaseValue(ContextValue value) {
        mValue = new WeakReference<>(value).get();
    }

    public void showValue(int type, Object obj) {
        if (mValue != null) {
            mValue.showValue(type, obj);
        }
    }

    public void toastShow(String msg) {
        if (mValue != null) {
            mValue.showValue(TYPE_TOAST, msg);
        }
    }

    /**
     * 显示进度条
     *
     * @param msg
     */
    public void progressShow(String msg) {
        if (mValue != null) {
            mValue.showValue(TYPE_DIALOG_PROGRESS_SHOW, msg);
        }
    }

    public void loadingShow(){
        if (mValue != null) {
            mValue.showValue(TYPE_DIALOG_LOADING, null);
        }
    }

    /**
     * 隐藏进度条
     */
    public void dialogHide() {
        if (mValue != null) {
            mValue.showValue(TYPE_DIALOG_HIDE, null);
        }
    }

    public void onCreate() {

    }

    public void onResume() {

    }

    public void onDestroy() {
    }


}
