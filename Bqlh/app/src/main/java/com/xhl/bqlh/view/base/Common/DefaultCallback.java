package com.xhl.bqlh.view.base.Common;

import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.xhl_library.utils.log.Logger;

/**
 * Created by Sum on 16/6/6.
 */
public abstract class DefaultCallback<T> extends BaseCallback<T> {

    //成功
    public abstract void success(T result);

    //最后调用
    public abstract void finish();

    @Override
    public void onSuccess(T result) {
        if (result instanceof ResponseModel) {
            ResponseModel res = (ResponseModel) result;
            if (res.isSuccess()) {
                success(result);
            } else {
                ToastUtil.showToastLong(res.getMessage());
            }
        } else {
            Logger.e("类型错误");
        }
    }

    @Override
    public void onFinished() {
        finish();
    }
}
