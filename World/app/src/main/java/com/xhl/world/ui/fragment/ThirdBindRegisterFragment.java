package com.xhl.world.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.config.Constant;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.user.UserInfo;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;
import com.xhl.xhl_library.utils.TimeCount;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 16/2/22.
 */
@ContentView(R.layout.fragment_third_register)
public class ThirdBindRegisterFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel> {

    private HashMap<String, String> mData;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_hint)
    private TextView tv_hint;

    @ViewInject(R.id.ed_input_phone)
    private EditText ed_input_phone;

    @ViewInject(R.id.ed_input_verify)
    private EditText ed_input_verify;

    @ViewInject(R.id.tv_get_verity)
    private TextView tv_get_verity;

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(value = R.id.ripple_register, type = RippleView.OnRippleCompleteListener.class)
    private void onRegisterClick(View view) {
        register();
    }

    @Event(value = R.id.tv_service)
    private void onServiceClick(View view) {

        Intent intent = new Intent(mContext, WebPageActivity.class);
        intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_protocol);
        intent.putExtra(WebPageActivity.TAG_TITLE, mContext.getString(R.string.register_protocol));
        getContext().startActivity(intent);
    }


    @Event(R.id.tv_get_verity)
    private void onGetVerClick(View view) {
        if (!isTick) {
            getSms();
        }
    }

    private boolean isTick = false;
    private TimeCount mTimeCount;
    private boolean mIsRegisting = false;

    @Override
    protected void initParams() {
        title_name.setText("注册绑定");
        String type = mData.get("third_login_type");

        if (type.equals("qq")) {
            tv_hint.setText(getString(R.string.bind_register_hint, "QQ"));
        } else if (type.equals("wx")) {
            tv_hint.setText(getString(R.string.bind_register_hint, "微信"));
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof HashMap) {
            mData = (HashMap<String, String>) data;
        }
    }

    private void register() {
        if (mIsRegisting) {
            return;
        }
        mIsRegisting = true;

        ViewUtils.hideKeyBoard(ed_input_verify);

        String phone = ed_input_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            SnackMaker.shortShow(title_name, R.string.empty_phone_hint);
            return;
        }
        if (!LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name, R.string.phone_error);
            return;
        }

        String verifyCode = ed_input_verify.getText().toString().trim();
        if (!LogonUtils.matcherCheckCode(verifyCode)) {
            SnackMaker.shortShow(title_name, R.string.verify_error);
            return;
        }

        String pwd = ed_input_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            SnackMaker.shortShow(title_name, R.string.empty_pwd_hint);
            return;
        }

        mData.put("telephone", phone);
        mData.put("userPassword", pwd);
        mData.put("userCode", verifyCode);

        showLoadingDialog();

        ApiControl.getApi().thirdRegisterBind(mData, new CommonCallback<ResponseModel<UserInfo>>() {
            @Override
            public void onSuccess(ResponseModel<UserInfo> result) {
                if (result.isSuccess()) {
                    EventBusHelper.postSaveLoginInfoEvent(result.getResultObj(),"注册绑定登录成功~");
                } else {
                    SnackMaker.shortShow(title_name, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.longShow(title_name, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsRegisting = false;
                hideLoadingDialog();
            }
        });
    }

    private void getSms() {
        String phone = ed_input_phone.getText().toString().trim();
        ViewUtils.hideKeyBoard(ed_input_phone);
        if (TextUtils.isEmpty(phone)) {
            SnackMaker.shortShow(title_name, R.string.empty_phone_hint);
            return;
        }
        if (!LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name, R.string.phone_error);
            return;
        }
        showLoadingDialog();
        //hide
        ViewUtils.hideKeyBoard(ed_input_verify);
        //获取验证码
        ApiControl.getApi().getRegisterVerifyCode(phone, this);
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
    public void onSuccess(ResponseModel result) {
        if (result.isSuccess()) {
            startTick();
            SnackMaker.shortShow(title_name, R.string.send_sms_success);
        } else {
            SnackMaker.shortShow(title_name, result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        SnackMaker.shortShow(title_name, ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        hideLoadingDialog();
    }
}
