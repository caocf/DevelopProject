package com.xhl.world.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.chat.event.ChatLoginFinish;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.data.PreferenceData;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.user.UserInfo;
import com.xhl.world.ui.event.OtherLoginEvent;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.utils.umeng.login.LoginCallBack;
import com.xhl.world.ui.utils.umeng.login.LoginHelper;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.LogonUtils;

import org.xutils.common.Callback;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/7.
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().finish();
    }

    @Event(value = R.id.ripple_login, type = RippleView.OnRippleCompleteListener.class)
    private void onLoginClick(View view) {
        chatLogin();
    }

    @Event(R.id.tv_register)
    private void onRegisterClick(View view) {
        getSumContext().pushFragmentToBackStack(RegisterFragment.class, RegisterFragment.Type_register);
    }

    @Event(R.id.tv_forget_pwd)
    private void onForgetPwdClick(View view) {
        getSumContext().pushFragmentToBackStack(RegisterFragment.class, RegisterFragment.Type_forget);
    }

    @Event(value = {R.id.iv_login_by_qq, R.id.iv_login_by_wx, R.id.iv_login_by_wb})
    private void onOtherLoginClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_by_qq:
                login(0);
                break;
            case R.id.iv_login_by_wx:
                login(1);
                break;
            case R.id.iv_login_by_wb:
                login(2);
                break;
        }
    }

    @ViewInject(R.id.ed_input_phone)
    private EditText ed_input_phone;

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;

    private LoginHelper mLoginHelper;

    private String mThirdLoginHint;
    private String mLoginType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initParams() {
        title_name.setText("登陆");

        String phone = PreferenceData.getInstance().getLoginPhone();
        if (!TextUtils.isEmpty(phone)) {
            ed_input_phone.setText(phone);
            ed_input_pwd.requestFocus();
        }
    }

    @Override
    protected boolean needEventBusRegister() {
        return false;
    }

    @Override
    public void onBackWithData(Object data) {
        super.onBackWithData(data);
        if (data != null && data instanceof String) {
            String phone = (String) data;
            if (!TextUtils.isEmpty(phone) && LogonUtils.matcherLogonPhone(phone)) {
                ed_input_phone.setText(phone);
            }
        }

    }

    public void chatLogin() {
        String phone = getEditText(ed_input_phone);
        String pwd = getEditText(ed_input_pwd);
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

        //隐藏键盘

        ViewUtils.hideKeyBoard(ed_input_pwd);

        showProgressLoading("账号登录中...");

        ApiControl.getApi().userLogin(phone, pwd, new Callback.CommonCallback<ResponseModel<UserInfo>>() {
            @Override
            public void onSuccess(ResponseModel<UserInfo> result) {
                if (result.isSuccess()) {
                    saveUserInfo(result.getResultObj());
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


    public void setCookie() {
        DbCookieStore instance = DbCookieStore.INSTANCE;
        List<HttpCookie> cookies = instance.getCookies();
        if (cookies.size() > 0) {
            HttpCookie httpCookie = cookies.get(0);
            String cookie = httpCookie.toString();
            //保存Cookie
            AppApplication.appContext.setCookie(cookie);
            AppApplication.appContext.mCookie = cookie;

            //保存到浏览器中
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(NetWorkConfig.generalHost, cookie);

        }
    }


    //保存用户信息
    private void saveUserInfo(UserInfo userInfo) {

        setCookie();

        //保存到本地数据
        AppApplication.appContext.saveLoginInfo(userInfo);

        PreferenceData.getInstance().setLoginPhone(userInfo.userName);

        //登陆聊天
        lastDo(userInfo.id);
    }

    private void lastDo(String id) {
        //订阅个人消息推送
//        PushManager.getInstance().subscribeCurrentUserChannel();

        ChatLoginFinish login = new ChatLoginFinish();
        //用户的唯一标示
        login.chatUserId = id;
        EventBus.getDefault().post(login);
        getSumContext().finish();
    }

    //保存登陆信息
    public void onEvent(OtherLoginEvent event) {
        ToastUtil.showToastLong(event.tip);
        saveUserInfo(event.info);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLoadingDialog();
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean isLogining = false;

    private void login(int type) {
        if (isLogining) {
            return;
        }
        isLogining = true;

        mLoginHelper = new LoginHelper(getActivity());
        if (type == 0) {
            mLoginHelper.loginByQQ();
            mThirdLoginHint = "亲爱的QQ用户：";
            mLoginType = "qq";
        } else if (type == 1) {
            mLoginHelper.loginByWeiXin();
            mThirdLoginHint = "亲爱的微信用户：";
            mLoginType = "wx";
        } else if (type == 2) {
//            helper.loginBySina();
        }
        mLoginHelper.setLoginCallBack(new LoginCallBack() {

            @Override
            public void onGetInfoSuccess(Map<String, String> info) {
                isLogining = false;
                //{is_yellow_year_vip=0, vip=0, level=0, province=江苏, yellow_vip_level=0, is_yellow_vip=0,
                // gender=男, screen_name=test, msg=, profile_image_url=http://q.qlogo.cn/qqapp/100424468/04A6EE6B66D2F9E392DBFF6356677007/100, city=南京}

                info.put("third_login_hint", mThirdLoginHint);
                info.put("third_login_type", mLoginType);
                //验证
                thirdVerify(info);
            }

            @Override
            public void onGetInfoFailed(int status) {
                isLogining = false;
                ToastUtil.showToastShort("获取用户信息失败");
            }

            @Override
            public void onGetInfoCancel(int status) {
                isLogining = false;
                ToastUtil.showToastShort("取消授权");
            }
        });
    }

    //第三方登陆验证
    private void thirdVerify(final Map<String, String> info) {

        showProgressLoading("第三方平台登录中...");

        String openId = info.get("openid");

        ApiControl.getApi().thirdLogin(mLoginType, openId, new Callback.CommonCallback<ResponseModel<UserInfo>>() {
            @Override
            public void onSuccess(ResponseModel<UserInfo> result) {
                if (result.isSuccess()) {
                    saveUserInfo(result.getResultObj());
                } else {
                    String message = result.getMessage();
                    if (message.equals("100")) {
                        getSumContext().pushFragmentToBackStack(ThirdBindFragment.class, info);
                    } else {
                        SnackMaker.shortShow(title_name, result.getMessage());
                    }
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
                hideLoadingDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginHelper != null) {
            mLoginHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
