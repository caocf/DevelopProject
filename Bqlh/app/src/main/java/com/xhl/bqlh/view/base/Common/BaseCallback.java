package com.xhl.bqlh.view.base.Common;

import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;

/**
 * Created by Sum on 16/6/6.
 */
public abstract class BaseCallback<T> implements Callback.CommonCallback<T> {

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToastShort(ex.getMessage());
        Logger.e("onError:" + ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Logger.e("onCancelled:" + cex.getMessage());
    }
}
