package com.xhl.bqlh.business.view.base;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.SystemBarTintManager;
import com.xhl.bqlh.business.view.base.Common.ActivePresent;
import com.xhl.bqlh.business.view.base.Common.ActivePresentActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.base.Common.RefreshLoadListener;
import com.xhl.bqlh.business.view.event.ReLoginEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.PhotoHelper;
import com.xhl.bqlh.business.view.ui.activity.LoginActivity;
import com.xhl.xhl_library.Base.Sum.SumFragmentActivity;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/3/10.
 */
public abstract class BaseAppActivity extends SumFragmentActivity implements ContextValue, RefreshLoadListener {

    private final static String EXTRA_RESTORE_PHOTO = "extra_photo";
    //其他，返回
    public static final int TYPE_child_other_back = -1;
    //其他，清理
    public static final int TYPE_child_other_clear = -2;

    private SystemBarTintManager mTintManager;

    protected Toolbar mToolbar;//工具栏
    private AppBarLayout mBarLayout;

    //统一拍照帮助类
    protected PhotoHelper mPhotoHelper;

    protected ActivePresent mPresent;

    /**
     * app状态栏设置颜色
     */
    protected boolean isNeedCompatStatusBar() {
        return true;
    }

    protected boolean registerEventBus() {
        return true;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    public ActivePresent getPresent() {
        if (mPresent == null) {
            mPresent = new ActivePresentActivity(this);
        }
        mPresent.setSnackBarView(mToolbar);
        return mPresent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedCompatStatusBar()) {
            statusInit();
        }
        mPhotoHelper = new PhotoHelper(this);
        if (registerEventBus()) {
            //注册监听
            EventBus.getDefault().register(this);
        }
    }

    /**
     * app的上下文
     */
    public AppDelegate getAppApplication() {
        return (AppDelegate) this.getApplication();
    }

    public <T> T _findViewById(int id) {

        return (T) findViewById(id);
    }

    protected void initToolbar(int type) {
        mToolbar = _findViewById(R.id.toolbar);
        mBarLayout = _findViewById(R.id.app_toolbar_layout);
        setSupportActionBar(mToolbar);
        Drawable wrap = null;
        if (type == TYPE_child_other_clear) {
            wrap = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_clear));
        } else if (type == TYPE_child_other_back) {
            wrap = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_back));
        }
        if (wrap == null) {
            return;
        }
        mToolbar.setNavigationIcon(wrap);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDo();
            }
        });
    }

    protected void initToolbar() {
        initToolbar(TYPE_child_other_back);
    }

    private void backDo() {
        BaseAppActivity.this.finish();
    }

    protected ColorStateList getIconList() {
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        }, new int[]{
                ContextCompat.getColor(this, R.color.app_nav_text_color),
                ContextCompat.getColor(this, R.color.colorPrimary),
        });

        return sl;
    }

    @Override
    protected void onResume() {
        AVAnalytics.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        AVAnalytics.onPause(this);
        super.onPause();
    }

    protected Drawable getTintDrawable(Drawable drawable, @ColorRes int colorId) {
        Drawable wrap = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrap, ContextCompat.getColor(this, colorId));
        return wrap;
    }

    protected Drawable getTintDrawable(@DrawableRes int drawableId, @ColorRes int colorId) {
        return getTintDrawable(ContextCompat.getDrawable(this, drawableId), colorId);
    }

    private void statusInit() {
        //为状态栏着色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        setStatusBarRes(R.color.colorPrimaryDark);
    }

    protected void setStatusBarRes(int resId) {
        mTintManager.setStatusBarTintResource(resId);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {

        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void showLoadingDialog() {
        getPresent().loadingView.showLoading();
    }

    public void hideLoadingDialog() {
        getPresent().loadingView.hideLoading();
    }

    public void showProgressLoading(String message) {
        getPresent().loadingView.showLoading(message);
    }

    public void showProgressLoading(String message, boolean cancelable) {
        getPresent().loadingView.showLoading(message, cancelable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        try {
            if (registerEventBus()) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        //监控回收
        AppDelegate.getRefWatcher(this).watch(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        File photo = mPhotoHelper.getPhoto();
        if (photo != null) {
            outState.putSerializable(EXTRA_RESTORE_PHOTO, photo);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        File photo = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
        if (photo != null) {
            mPhotoHelper.setPhoto(photo);
        }
    }

    @Override
    public Object getValue(int type) {
        return getPresent().getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        getPresent().showValue(type, obj);
    }


    protected void userQuitDialog() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.dialog_exit_title);
        dialog.setMessage(R.string.dialog_exit_msg);
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                quitLogin();
                reLogin();
            }
        });
        dialog.show();
    }

    private void quitLogin() {
        ApiControl.getApi().userQuit(new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                reLogin();
            }

            @Override
            public void finish() {

            }
        });
    }

    //重新登录
    public void onEvent(ReLoginEvent loginEvent) {
        if (loginEvent.getTag() == 4) {
            AlertDialog.Builder dialog = DialogMaker.getDialog(this);
            dialog.setTitle("异地登陆");
            dialog.setMessage("您的账号在另一个设备上登陆");
            dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAppApplication().setUserLogout();
                    getAppApplication().exitApp();
                }
            });
            dialog.setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reLogin();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            reLogin();
        }
    }

    private void reLogin() {
        getAppApplication().setUserLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getAppApplication().exitApp();
    }

    /**
     * 初始化数据
     */
    protected void initRefreshStyle(SwipeRefreshLayout refreshLayout, SwipeRefreshLayoutDirection direction) {
        getPresent().initRefreshStyle(refreshLayout, direction);
    }

    @Override
    public void onRefreshLoadData() {
        //子类重写加载数据
    }

    @Override
    public void onRefreshTop() {
        //下拉回调
    }

    @Override
    public void onRefreshMore() {
        //上拉回调
    }

    @Override
    public void onRefreshNoMore() {
        SnackUtil.shortShow(mToolbar, R.string.load_null);
    }

    public int getPageSize() {
        return mPresent.mPageSize;
    }

    public int getPageIndex() {
        return mPresent.mPageIndex;
    }

    public void setTotalSize(int totalSize) {
        mPresent.mTotalSize = totalSize;
    }

}
