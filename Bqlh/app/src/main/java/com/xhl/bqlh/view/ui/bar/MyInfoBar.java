package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.custom.RoundedImageView;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.activity.CollectionActivity;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/4.
 */
public class MyInfoBar extends BaseBar implements Callback.CommonCallback<ResponseModel<GarbageModel>> {

    @ViewInject(R.id.iv_login)
    private ImageView iv_login;

    @ViewInject(R.id.rl_login_res)
    private View rl_login_res;

    //基本信息
    @ViewInject(R.id.iv_user_avatar)
    private RoundedImageView iv_user_avatar;

    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;

    @ViewInject(R.id.tv_user_phone)
    private TextView tv_user_phone;

    //收藏信息
    @ViewInject(R.id.tv_num_pro)
    private TextView tv_num_pro;

    @ViewInject(R.id.tv_num_shop)
    private TextView tv_num_shop;

    @Event(R.id.fl_c_pro)
    private void onProClick(View view) {
        collection(CollectionActivity.TAG_PRODUCT);
    }

    @Event(R.id.fl_c_shop)
    private void onShopClick(View view) {
        collection(CollectionActivity.TAG_SHOP);
    }

    private void collection(int type) {
        if (AppDelegate.appContext.isLogin(mContext)) {
            Intent intent = new Intent(mContext, CollectionActivity.class);
            intent.putExtra("data", type);
            mContext.startActivity(intent);
        }
    }

    @Event(R.id.iv_login)
    private void onLoginClick(View view) {
        //登陆的Fragment
        FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_login);
    }

    @Event(R.id.iv_user_avatar)
    private void onInfoClick(View view) {
        //用户信息的Fragment
        FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_user);
    }

    public MyInfoBar(Context context) {
        super(context);
    }

    public MyInfoBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        refreshUserInfo();
        refreshUserCollection();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_my_info;
    }

    public void refreshUserInfo() {

        boolean login = AppDelegate.appContext.isLogin();
        if (login) {
            ViewHelper.setViewGone(iv_login, true);
            ViewHelper.setViewGone(rl_login_res, false);
            //头像
            String faceImage = AppDelegate.appContext.getUserFaceImage();
            if (!TextUtils.isEmpty(faceImage)) {
                iv_user_avatar.LoadDrawable(faceImage);
            }
            //信息
            UserInfo userInfo = AppDelegate.appContext.getUserInfo();
            if (!TextUtils.isEmpty(userInfo.liableName)) {
                tv_user_name.setText(userInfo.liableName);
            }
            if (!TextUtils.isEmpty(userInfo.userId)) {
                tv_user_phone.setText(userInfo.userId);
            }

        } else {
            ViewHelper.setViewGone(iv_login, false);
            ViewHelper.setViewGone(rl_login_res, true);
        }
    }

    public void refreshUserCollection() {
        boolean login = AppDelegate.appContext.isLogin();
        if (login) {
            ViewHelper.setViewGone(tv_num_shop, false);
            ViewHelper.setViewGone(tv_num_pro, false);
            ApiControl.getApi().collectQueryNum(this);
        } else {
            ViewHelper.setViewGone(tv_num_shop, true);
            ViewHelper.setViewGone(tv_num_pro, true);
        }
    }

    @Override
    public void onSuccess(ResponseModel<GarbageModel> result) {
        if (result.isSuccess()) {
            GarbageModel obj = result.getObj();
            tv_num_shop.setText(obj.getUserCollectShopCount());
            tv_num_pro.setText(obj.getUserCollectProductCount());
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
}
