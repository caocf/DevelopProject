package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/4/21.
 */
@ContentView(R.layout.fragment_pay)
public class PayFragment extends BaseAppFragment {

    public static PayFragment newInstance(int type) {
        PayFragment payFragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        payFragment.setArguments(bundle);
        return payFragment;
    }

    @ViewInject(R.id.iv_pay)
    private LifeCycleImageView iv_pay;

    @Override
    protected void initParams() {

        int type = getArguments().getInt("type", 0);

        ApiControl.getApi().payImage(type, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {

                    String url = "http://bqlhtest.xhl.cn.com:81/" + result.getObj();

                    iv_pay.bindImageUrl(url);

                } else {
                    SnackUtil.shortShow(mView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
