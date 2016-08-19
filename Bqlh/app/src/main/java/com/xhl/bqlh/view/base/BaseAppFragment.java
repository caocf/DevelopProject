package com.xhl.bqlh.view.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.callback.ContextValue;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.Common.ActivePresent;
import com.xhl.bqlh.view.base.Common.ActivePresentFragment;
import com.xhl.bqlh.view.base.Common.RefreshLoadListener;
import com.xhl.bqlh.view.helper.PhotoHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.xhl_library.Base.Sum.SumFragment;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/3/10.
 */
public abstract class BaseAppFragment extends SumFragment implements ContextValue, RefreshLoadListener {

    private final static String EXTRA_RESTORE_PHOTO = "extra_photo";

    private boolean mIsNeedLoadData = false;//view创建完成后，状态是当前可见的

    private boolean mIsPrepared = false;//view是否已经创建完成，可以加载数据

    protected PhotoHelper mPhotoHelper;

    protected boolean isNeedRegisterEventBus() {
        return false;
    }

    private ActivePresent mPresent;//代理公用任务

    protected ActivePresent getPresent() {
        if (mPresent == null) {
            mPresent = new ActivePresentFragment(this);
        }
        return mPresent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        mPhotoHelper = new PhotoHelper(this);
    }

    public <T> T _findViewById(int id) {
        return (T) mView.findViewById(id);
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
                    getSumContext().popTopFragment(null);
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
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd(this.getClass().getSimpleName());
    }

    /**
     * app的上下文
     */
    public AppDelegate getAppApplication() {
        return (AppDelegate) getActivity().getApplication();
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

    //刷新控件
    protected void initRefreshStyle(SwipeRefreshLayout refreshLayout, SwipeRefreshLayoutDirection direction) {
        getPresent().initRefreshStyle(refreshLayout, direction);
    }

    //刷新回调
    @Override
    public void onRefreshLoadData() {
        networkErrorHide();
    }

    @Override
    public void onRefreshTop() {
    }

    @Override
    public void onRefreshMore() {

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


    @Override
    public Object getValue(int type) {
        return getPresent().getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        getPresent().showValue(type, obj);
    }

    @Override
    public void onDestroy() {
        try {
            if (isNeedRegisterEventBus()) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        getAppApplication().watcher.watch(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepared = true;
        loadData();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        File photo = mPhotoHelper.getPhoto();
        if (photo != null) {
            outState.putSerializable(EXTRA_RESTORE_PHOTO, photo);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            File photo = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
            if (photo != null) {
                mPhotoHelper.setPhoto(photo);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//获取fragment的可见状态
            mIsNeedLoadData = true;
            loadData();
        } else {
            mIsNeedLoadData = false;
        }
    }

    //首次加载和多次加载
    private void loadData() {
        if (mIsNeedLoadData && mIsPrepared) {
            onVisibleLoadData();
        }
    }

    /**
     * 方法在Fragment可见的时候加载数据
     * <p/>
     * 子类可根据需要判断使用已经加载完成过一次
     */
    protected void onVisibleLoadData() {
    }

    public void onEvent(CommonEvent event) {
    }
}
