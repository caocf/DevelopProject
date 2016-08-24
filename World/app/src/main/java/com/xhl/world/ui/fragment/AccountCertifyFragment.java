package com.xhl.world.ui.fragment;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.IDCard;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/19.
 */
@ContentView(R.layout.fragment_account_certify)
public class AccountCertifyFragment extends BaseAppFragment {


    @ViewInject(R.id.title_name)
    private TextView mTitleName;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @ViewInject(R.id.tv_user_names)
    private TextView tv_user_names;

    @ViewInject(R.id.text_input_name)
    private TextInputLayout text_input_name;

    @ViewInject(R.id.text_input_id)
    private TextInputLayout text_input_id;

    @Event(value = R.id.ripple_commit, type = RippleView.OnRippleCompleteListener.class)
    private void onCommitClick(View view) {
        commitInfo();
    }

    @Override
    protected void initParams() {
        mTitleName.setText("实名认证");

        String name = AppApplication.appContext.getUserName();

        tv_user_names.setText("用户名:" + name);
    }

    private void commitInfo() {
        String name = text_input_name.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            SnackMaker.shortShow(mTitleName, "姓名不能为空");
            return;
        }
        String idCard = text_input_id.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(idCard)) {
            SnackMaker.shortShow(mTitleName, "身份证不能为空");
            return;
        }
        IDCard cc = new IDCard();
        String mesage = null;
        try {
            mesage = cc.IDCardValidate(idCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(mesage)) {
            SnackMaker.longShow(mTitleName, mesage);
            return;
        }

        showLoadingDialog();

        ApiControl.getApi().idCardVerify(name, idCard, "", new Callback.CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    Object resultObj = result.getResultObj();
                    if (resultObj instanceof Integer) {
                        if (((Integer) resultObj) == 1) {
                            SnackMaker.shortShow(mTitleName, "提交成功");
                            getSumContext().popTopFragment(null);
                        }
                    }
                } else {
                    SnackMaker.shortShow(mTitleName,result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mTitleName,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });

    }
}
