package com.xhl.bqlh.view.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.data.PreferenceData;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.ViewHelper;

import org.xutils.http.cookie.DbCookieStore;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Sum on 16/7/4.
 */
@ContentView(R.layout.fragment_user_login)
public class UserLoginFragment extends BaseAppFragment {

    @ViewInject(R.id.ed_input_name)
    private EditText ed_input_name;

    @ViewInject(R.id.ed_input_pwd)
    private EditText ed_input_pwd;

    @Event(R.id.tv_find_pwd)
    private void onFrogetClick(View view) {
        getSumContext().pushFragmentToBackStack(UserForgetPwdFragment.class, null);
    }

    @Event(R.id.iv_back)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(R.id.btn_login)
    private void onLoginClick(View view) {
        String phone = getEditText(ed_input_name);
        String pwd = getEditText(ed_input_pwd);
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastShort(R.string.empty_name_hint);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToastShort(R.string.empty_pwd_hint);
            return;
        }
        //隐藏键盘
        ViewHelper.KeyBoardHide(view);

        showProgressLoading(getString(R.string.login));

        ApiControl.getApi().userLogin(phone, pwd, new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                UserInfo userInfo = result.getObj().getUserInfo();
                saveUserInfo(userInfo);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }

    //保存用户信息
    private void saveUserInfo(UserInfo userInfo) {

        String name = ed_input_name.getText().toString().trim();
        PreferenceData.getInstance().setLoginPhone(name);
        //保存到本地数据
        AppDelegate.appContext.saveLoginInfo(userInfo);
        //save cookie
        saveCookie();
        EventHelper.postCommonEvent(CommonEvent.ET_RELOAD_USER_INFO);
        getActivity().finish();
    }

    private void saveCookie() {
        DbCookieStore instance = DbCookieStore.INSTANCE;
        List<HttpCookie> cookies = instance.getCookies();
        if (cookies.size() > 0) {
            HttpCookie httpCookie = cookies.get(0);
//            httpCookie.setMaxAge(7 * 24 * 60 * 60 * 60);
//            instance.add(instance.getURIs().get(0), httpCookie);

            String cookie = httpCookie.toString();
            //保存Cookie
            AppDelegate.appContext.setCookie(cookie);
            AppDelegate.appContext.mCookie = cookie;
        }
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }


    @Override
    protected void initParams() {

    }
}
