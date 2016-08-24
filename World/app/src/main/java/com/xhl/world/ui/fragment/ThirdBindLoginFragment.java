package com.xhl.world.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.user.UserInfo;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Sum on 16/2/22.
 */
@ContentView(R.layout.fragment_third_login)
public class ThirdBindLoginFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.tv_hint)
    private TextView tv_hint;

    @ViewInject(R.id.ed_input_phone)
    private EditText ed_input_phone;

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(value = R.id.ripple_login, type = RippleView.OnRippleCompleteListener.class)
    private void onLoginClick(View view) {
        bindLogin();
    }


    private HashMap<String, String> mData;

    @Override
    protected void initParams() {
        title_name.setText("登陆绑定");

        String type = mData.get("third_login_type");
        if (type.equals("qq")) {
            tv_hint.setText(getString(R.string.bind_login_hint, "QQ"));
        } else if (type.equals("wx")) {
            tv_hint.setText(getString(R.string.bind_login_hint, "微信"));
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof HashMap) {
            mData = (HashMap<String, String>) data;
        }
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void bindLogin() {
        String phone = getEditText(ed_input_phone);
        String pwd = getEditText(ed_input_pwd);

        ViewUtils.hideKeyBoard(ed_input_phone);

        if (TextUtils.isEmpty(phone)) {
            SnackMaker.shortShow(title_name, R.string.empty_phone_hint);
            return;
        }
        if (!LogonUtils.matcherLogonPhone(phone)) {
            SnackMaker.shortShow(title_name, R.string.phone_error);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            SnackMaker.shortShow(title_name, R.string.empty_pwd_hint);
            return;
        }

        showProgressLoading("正在为您绑定账号信息");


        String openId = mData.get("openid");
        String openType = mData.get("third_login_type");

        ApiControl.getApi().thirdLoginBind(openId, openType, phone, pwd, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj().equals("success")) {
                        thirdLogin();
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

    private void thirdLogin() {
        String openId = mData.get("openid");
        String openType = mData.get("third_login_type");

        ApiControl.getApi().thirdLogin(openType, openId, new Callback.CommonCallback<ResponseModel<UserInfo>>() {
            @Override
            public void onSuccess(ResponseModel<UserInfo> result) {
                if (result.isSuccess()) {

                    EventBusHelper.postSaveLoginInfoEvent(result.getResultObj(), "登录绑定成功~");

                } else {
                    SnackMaker.shortShow(title_name, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
