package com.xhl.bqlh.view.ui.fragment;

import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.UserAccount;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/7/20.
 */
@ContentView(R.layout.fragment_user_account)
public class UserAccountFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_user_account)
    private TextView tv_user_account;

    @ViewInject(R.id.tv_user_zfb)
    private TextView tv_user_zfb;

    @Override
    protected void initParams() {

        super.initBackBar("账户信息", true, false);

        onRefreshLoadData();
    }

    @Override
    public void onRefreshLoadData() {

        ApiControl.getApi().userAccount(new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                UserAccount userAccount = result.getObj().getUserAccount();
                tv_user_account.setText(userAccount.getAccountNum());
                tv_user_zfb.setText(userAccount.getZhiFBAccount());
            }

            @Override
            public void finish() {

            }
        });
    }
}
