package com.xhl.bqlh.business.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.utils.TimeCount;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/6/6.
 */
@ContentView(R.layout.fragment_forget_pwd)
public class LoginForgetPwdFragment extends BaseAppFragment {


    @ViewInject(R.id.tv_get_verity)
    private TextView tv_get_verity;

    @ViewInject(R.id.ed_input_name)
    private EditText ed_input_name;

    @ViewInject(R.id.ed_input_verify)
    private EditText ed_input_verify;

    @ViewInject(R.id.ed_input_new_pwd)
    private EditText ed_input_new_pwd;

    @ViewInject(R.id.ed_input_new_pwd_again)
    private EditText ed_input_new_pwd_again;

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
        final String code = ed_input_verify.getText().toString().trim();

        final String phone = ed_input_name.getText().toString().trim();

        final String pwd1 = ed_input_new_pwd.getText().toString().trim();

        final String pwd2 = ed_input_new_pwd_again.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SnackUtil.shortShow(view, "用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            SnackUtil.shortShow(view, "验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd1)) {
            SnackUtil.shortShow(view, "新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            SnackUtil.shortShow(view, "再次确认密码不能为空");
            return;
        }
        if (!pwd1.equals(pwd2)) {
            SnackUtil.shortShow(view, "两次输入密码不一致");
            return;
        }

        ViewHelper.KeyBoardHide(view);

        ApiControl.getApi().userVerifyCode(phone, code, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                updatePwd();
            }

            @Override
            public void finish() {

            }
        });

    }

    private void updatePwd() {

        showProgressLoading("提交新密码中...");

        final String phone = ed_input_name.getText().toString().trim();

        final String pwd1 = ed_input_new_pwd.getText().toString().trim();
        ApiControl.getApi().userUpdatePwd(phone, pwd1, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                SnackUtil.shortShow(mView, result.getObj());
                mToolbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSumContext().popTopFragment(null);
                    }
                }, 1000);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }

    @Event(R.id.tv_get_verity)
    private void onGetCodeClick(View view) {
        if (!isTick) {
            getSms();
        }
    }

    private boolean isTick = false;
    private TimeCount mTimeCount;
    private String enterName;

    @Override
    protected void initParams() {
        super.initToolbar();
        mToolbar.setTitle(R.string.forget_pwd);
        if (!TextUtils.isEmpty(enterName)) {
            ed_input_name.setText(enterName);
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof String) {
            enterName = (String) data;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTick();
    }

    private void getSms() {
        String phone = ed_input_name.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            SnackUtil.shortShow(mToolbar, "用户名不能为空");
            return;
        }

        ViewHelper.KeyBoardHide(ed_input_name);

        showProgressLoading("获取验证码中...");

        ApiControl.getApi().userGetCode(phone, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    SnackUtil.longShow(mToolbar, result.getObj());
                    startTick();
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
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

    private void startTick() {
        stopTick();
        isTick = true;
        long total = 1000 * 60 * 2;
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
}
