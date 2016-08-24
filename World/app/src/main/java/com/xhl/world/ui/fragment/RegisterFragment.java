package com.xhl.world.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;
import com.xhl.xhl_library.utils.TimeCount;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 15/12/7.
 */
@ContentView(R.layout.fragment_register)
public class RegisterFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel> {

    /**
     * 注册
     */
    public static final int Type_register = 0;
    /**
     * 忘记密码
     */
    public static final int Type_forget = 1;
    /**
     * 修改密码
     */
    public static final int Type_forget_by_login = 2;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_get_verity)
    private TextView tv_get_verity;

    @ViewInject(R.id.ed_input_phone)
    private EditText ed_input_phone;

    @ViewInject(R.id.ed_input_verify)
    private EditText ed_input_verify;

    @ViewInject(R.id.ll_other_login)
    private LinearLayout ll_other_login;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        String tel = ed_input_phone.getText().toString();
        getSumContext().popTopFragment(tel);
    }

    @Event(value = R.id.ripple_next, type = RippleView.OnRippleCompleteListener.class)
    private void onRegisterNextClick(View view) {

        doNext();
    }

    private void doNext() {
        final String code = ed_input_verify.getText().toString().trim();

        final String phone = ed_input_phone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SnackMaker.shortShow(title_name, R.string.empty_phone_hint);
            return;
        }

        if (!LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name, R.string.phone_error);
            return;
        }

        if (!LogonUtils.matcherCheckCode(code)) {
            SnackMaker.shortShow(title_name, R.string.verify_error);
            return;
        }

        ViewUtils.hideKeyBoard(ed_input_verify);

        showLoadingDialog();

        ApiControl.getApi().checkVerifyCode(phone, code, new CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj().equals("success")) {
                        //操作类型
                        mRegisterData.put("type", String.valueOf(mType));
                        //验证码
                        mRegisterData.put("code", code);
                        //手机号
                        mRegisterData.put("phone", phone);

                        getSumContext().pushFragmentToBackStack(RegisterNextFragment.class, mRegisterData);
                    } else {
                        SnackMaker.shortShow(title_name, result.getMessage());
                    }
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
        });


    }

    @Event(R.id.tv_get_verity)
    private void onGetVerClick(View view) {
        if (!isTick) {
            getSms();
        }
    }

    @Event(R.id.tv_service)
    private void onLoginClick(View view) {
        if (mType != Type_forget_by_login) {
            getSumContext().popToRoot(null);
        }
    }

    private boolean isTick = false;
    private TimeCount mTimeCount;
    private int mType;
    private HashMap<String, String> mRegisterData = new HashMap<>();

    @Override

    protected void initParams() {
        if (mType == Type_register) {
            title_name.setText("手机注册");
        } else if (mType == Type_forget) {
            title_name.setText("忘记密码");
        } else {
            title_name.setText("修改密码");
        }
        if (mType == Type_forget_by_login) {
            ll_other_login.setVisibility(View.GONE);
        }
    }

    private void getSms() {
        String phone = ed_input_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            SnackMaker.shortShow(title_name, R.string.empty_phone_hint);
            return;
        }
        if (!LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name, R.string.phone_error);
            return;
        }

        ViewUtils.hideKeyBoard(ed_input_phone);

        showLoadingDialog();

        if (mType == Type_register) {
            getRegisterSms(phone);
        } else {
            getChangePwdSms(phone);
        }
    }

    private void getChangePwdSms(String phone) {
        ApiControl.getApi().getForgetPwdVerifyCode(phone, this);
    }


    private void getRegisterSms(String phone) {
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
    public void onEnter(Object data) {
        if (data != null) {
            mType = (int) data;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTick();
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
        SnackMaker.shortShow(title_name, R.string.network_error);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        hideLoadingDialog();
    }
}
