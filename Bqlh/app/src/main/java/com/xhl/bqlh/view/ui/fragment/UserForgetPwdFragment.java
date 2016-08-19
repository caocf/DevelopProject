package com.xhl.bqlh.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.xhl_library.utils.TimeCount;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Summer on 2016/7/20.
 */
@ContentView(R.layout.fragment_user_forget_pwd)
public class UserForgetPwdFragment extends BaseAppFragment {

    @ViewInject(R.id.ed_input_phone)
    private EditText ed_input_phone;

    @ViewInject(R.id.ed_input_verify)
    private EditText ed_input_verify;

    @ViewInject(R.id.tv_get_verity)
    private TextView tv_get_verity;

    @Event(R.id.tv_get_verity)
    private void onGetVerClick(View view) {
        if (!isTick) {
            getSms();
        }
    }

    @Event(R.id.btn_confirm)
    private void onNextClick(View view) {
        doNext();
    }

    private boolean isTick = false;

    private String mPhone, mUserName;

    private TimeCount mTimeCount;

    @Override
    protected void initParams() {
        super.initBackBar("找回密码", true, false);
    }

    private void doNext() {
        String code = ed_input_verify.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToastShort("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(mPhone)) {
            ToastUtil.showToastShort("请先获取验证码");
            return;
        }

        ViewHelper.KeyBoardHide(ed_input_verify);

        showLoadingDialog();

        ApiControl.getApi().userForgetPwdCheck(mPhone, code, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                getSumContext().pushFragmentToBackStack(UserForgetPwdEditFragment.class, mUserName);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });

    }


    private void getSms() {
        final String phone = ed_input_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastShort("用户名不能为空");
            return;
        }

        ViewHelper.KeyBoardHide(ed_input_phone);

        showLoadingDialog();

        ApiControl.getApi().userForgetPwdGet(phone, new DefaultCallback<ResponseModel<HashMap<String,String>>>() {
            @Override
            public void success(ResponseModel<HashMap<String,String>> result) {
                mPhone = result.getObj().get("phone");
                mUserName = phone;
                ToastUtil.showToastShort(R.string.send_sms_success);
                startTick();
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }


    private void startTick() {
        stopTick();
        isTick = true;
        long total = 1000 * 60;
        long inter = 1000;
        mTimeCount = new TimeCount(total, inter, new TimeCount.TimeOutCallback() {
            @Override
            public void onFinish() {
                isTick = false;
                tv_get_verity.setText("获取验证码");
            }

            @Override
            public void onTick(long st) {
                long t = st / 1000;
                tv_get_verity.setText(String.valueOf(t) + "s");
            }
        });
        mTimeCount.start();
    }

    private void stopTick() {
        if (mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount.onFinish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTick();
    }

}
