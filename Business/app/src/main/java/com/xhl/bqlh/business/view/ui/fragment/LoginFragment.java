package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.UserInfo;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.TaskAsyncHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.ui.activity.HomeActivity;

import org.xutils.common.Callback;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Sum on 16/6/6.
 */
@ContentView(R.layout.activity_login)
public class LoginFragment extends BaseAppFragment {

    @ViewInject(R.id.ed_login_name)
    private EditText ed_login_name;

    @ViewInject(R.id.ed_login_pwd)
    private EditText ed_login_pwd;

    @Event(R.id.tv_forget_pwd)
    private void onForgetClick(View view) {
        String loginName = ed_login_name.getText().toString().trim();

        getSumContext().pushFragmentToBackStack(LoginForgetPwdFragment.class, loginName);
    }

    @Event(R.id.btn_login)
    private void onLoginClick(View view) {
        String loginName = ed_login_name.getEditableText().toString();
        String password = ed_login_pwd.getEditableText().toString();

        if (TextUtils.isEmpty(loginName)) {
            SnackUtil.shortShow(view, R.string.empty_name_hint);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            SnackUtil.shortShow(view, R.string.empty_pwd_hint);
            return;
        }
        login(loginName, password);
    }

    private void login(String name, String pwd) {
        showProgressLoading(getString(R.string.login));

        ApiControl.getApi().userLogin(name, pwd, new Callback.CommonCallback<ResponseModel<UserInfo>>() {
            @Override
            public void onSuccess(ResponseModel<UserInfo> result) {
                if (result.isSuccess()) {

                    saveCookie();

                    mUserInfo = result.getObj();

                    hideLoadingDialog();

                    mTask.pullServiceTask();
                } else {
                    hideLoadingDialog();
                    SnackUtil.shortShow(ed_login_name, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(ed_login_name, ex.getMessage());
                hideLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private UserInfo mUserInfo;

    private TaskAsyncHelper mTask;

    @Override
    protected void initParams() {
        String loginPhone = PreferenceData.getInstance().getLoginPhone();
        if (!TextUtils.isEmpty(loginPhone)) {
            ed_login_name.setText(loginPhone);
            ed_login_pwd.requestFocus();
        }

        mTask = new TaskAsyncHelper(new TaskAsyncHelper.TaskListener() {
            @Override
            public void onPullStart() {
                showProgressLoading("获取数据中");
            }

            @Override
            public void onPullFinish() {
                hideLoadingDialog();
                saveUserInfo(mUserInfo);
            }

            @Override
            public void onPullFailed(String msg) {
                hideLoadingDialog();
                ToastUtil.showToastShort(msg);
            }
        });
    }


    public void saveCookie() {
        DbCookieStore instance = DbCookieStore.INSTANCE;
        List<HttpCookie> cookies = instance.getCookies();
        if (cookies.size() > 0) {
            HttpCookie httpCookie = cookies.get(0);
            String cookie = httpCookie.toString();
            //保存Cookie
            AppDelegate.appContext.setCookie(cookie);
            AppDelegate.appContext.mCookie = cookie;
        }
    }

    //保存用户信息
    private void saveUserInfo(UserInfo userInfo) {

        String name = ed_login_name.getText().toString().trim();
        String loginPhone = PreferenceData.getInstance().getLoginPhone();
        //换账号登陆重置签到状态
        if (!TextUtils.isEmpty(loginPhone) && !loginPhone.equals(name)) {
            PreferenceData.getInstance().todaySignEnd(false);
            PreferenceData.getInstance().todaySignStart(false);
        }
        PreferenceData.getInstance().setLoginPhone(name);
        //保存到本地数据
        AppDelegate.appContext.saveLoginInfo(userInfo);
        //跳转到首页
        startActivity(new Intent(getContext(), HomeActivity.class));
        getActivity().finish();
    }
}
