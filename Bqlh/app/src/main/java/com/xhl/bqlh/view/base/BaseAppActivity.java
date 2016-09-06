package com.xhl.bqlh.view.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.callback.ContextValue;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.SystemBarTintManager;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.Common.ActivePresent;
import com.xhl.bqlh.view.base.Common.ActivePresentActivity;
import com.xhl.bqlh.view.base.Common.RefreshLoadListener;
import com.xhl.bqlh.view.custom.BadgeView;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.bqlh.view.helper.PhotoHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.xhl_library.Base.Sum.SumFragmentActivity;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/3/10.
 */
public abstract class BaseAppActivity extends SumFragmentActivity implements ContextValue, RefreshLoadListener {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String EXTRA_RESTORE_PHOTO = "extra_photo";
    protected static final int DELAY_TIME = 300;
    //统一拍照帮助类
    protected PhotoHelper mPhotoHelper;

    private ActivePresent mPresent;

    private SystemBarTintManager mTintManager;

    protected BadgeView mBadgeView;

    //初始化数字
    protected void initBadgeView(View view) {
        mBadgeView = new BadgeView(this);
        mBadgeView.setTargetView(view);
        mBadgeView.setTextSize(8);
        mBadgeView.setBadgeMargin(0, 3, 16, 0);

        GlobalCarInfo.instance().addBadgeView(mBadgeView);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    public ActivePresent getPresent() {
        if (mPresent == null) {
            mPresent = new ActivePresentActivity(this);
        }
        return mPresent;
    }

    /**
     * app状态栏设置颜色
     */
    protected boolean isNeedCompatStatusBar() {
        return true;
    }

    protected boolean isNeedEventBus() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedCompatStatusBar()) {
            statusInit();
        }
        if (isNeedEventBus()) {
            EventBus.getDefault().register(this);
        }

        mPhotoHelper = new PhotoHelper(this);
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

    //子类标题初始化
    protected void initBackBar(String title, boolean showBack) {
        initBackBar(title, showBack, true);
    }

    protected void initBackBar(String title, boolean showBack, boolean showRight) {
        TextView tv_title = _findViewById(R.id.title_name);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        //返回键处理
        View back = _findViewById(R.id.fl_back);
        if (showBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            ViewHelper.setViewVisible(back, false);
            back.setBackgroundResource(R.color.transparent);
        }
        //右键处理
        View right = _findViewById(R.id.fl_btn_right);
        if (!showRight) {
            ViewHelper.setViewVisible(right, false);
            right.setBackgroundResource(R.color.transparent);
        }
    }

    protected void setTitle(String title) {
        TextView tv_title = _findViewById(R.id.title_name);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    //网络异常显示的View
    private View mNetworkErrorView;

    private void initNetwork() {
        ViewStub viewStub = _findViewById(R.id.vb_network_error);
        viewStub.inflate();
        mNetworkErrorView = _findViewById(R.id.rl_null_show);
        mNetworkErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshLoadData();
            }
        });
    }

    //异常显示
    protected void networkErrorShow() {
        if (mNetworkErrorView == null) {
            initNetwork();
        }
        ViewHelper.setViewGone(mNetworkErrorView, false);
    }

    //异常关闭
    protected void networkErrorHide() {
        ViewHelper.setViewGone(mNetworkErrorView, true);
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

    protected Drawable setDrawableTint(Drawable drawable, @ColorRes int colorId) {
        Drawable wrap = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrap, ContextCompat.getColor(this, colorId));
        return wrap;
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
    protected void onDestroy() {
        if (isNeedEventBus()) {
            try {
                EventBus.getDefault().unregister(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GlobalCarInfo.instance().removeBadgeView(mBadgeView);

        super.onDestroy();
        //监控回收
        getAppApplication().watcher.watch(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
        overridePendingTransition(R.anim.right_open_enter, R.anim.right_open_exist);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
        overridePendingTransition(R.anim.right_open_enter, R.anim.right_open_exist);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
        overridePendingTransition(R.anim.right_close_enter, R.anim.right_close_exist);
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

    /**
     * 初始化数据
     */
    protected void initRefreshStyle(SwipeRefreshLayout refreshLayout, SwipeRefreshLayoutDirection direction) {
        getPresent().initRefreshStyle(refreshLayout, direction);
    }

    @Override
    public void onRefreshLoadData() {
        //子类重写加载数据
        networkErrorHide();//关闭网络异常ui
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
        ToastUtil.showToastShort(R.string.load_null);
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

    public void onEvent(CommonEvent event) {
        if (event.getEventTag() == CommonEvent.ET_RELOGIN) {
            AppDelegate.appContext.setUserLogout();
            //清理缓存
            GlobalCarInfo.instance().clear();
            //登陆的Fragment
//            FragmentContainerHelper.startFragment(this, FragmentContainerHelper.fragment_login);
        }
    }
}
