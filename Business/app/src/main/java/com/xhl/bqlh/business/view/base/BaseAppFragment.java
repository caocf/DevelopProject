package com.xhl.bqlh.business.view.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.Common.ActivePresent;
import com.xhl.bqlh.business.view.base.Common.ActivePresentFragment;
import com.xhl.bqlh.business.view.base.Common.RefreshLoadListener;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.PhotoHelper;
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
    //其他，返回
    public static final int TYPE_child_other_back = -1;
    //其他，清理
    public static final int TYPE_child_other_clear = -2;

    private boolean mIsNeedLoadData = false;//view创建完成后，状态是当前可见的
    private boolean mIsPrepared = false;//view是否已经创建完成，可以加载数据

    protected boolean isNeedRegisterEventBus() {
        return false;
    }

    protected Toolbar mToolbar;//工具栏
    protected AppBarLayout mBarLayout;

    private ActionBarDrawerToggle mToggle;//侧滑菜单和工具栏关联控制

    protected PhotoHelper mPhotoHelper;

    protected ActivePresent mPresent;

    private ActivePresent getPresent() {
        if (mPresent == null) {
            mPresent = new ActivePresentFragment(this);
            mPresent.setSnackBarView(mView);
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

    protected void initHomeToolbar() {
        mToolbar = _findViewById(R.id.toolbar);
        mBarLayout = _findViewById(R.id.app_toolbar_layout);
        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_nav_drawer));
        mToolbar.setNavigationIcon(wrap);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHelper.postCommonEvent(CommonEvent.EVENT_OPEN_DRAWER);
            }
        });

        //drawer监听
//        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.addDrawerListener(mToggle);
//        mToggle.syncState();
    }

    protected void initToolbar() {
        initToolbar(TYPE_child_other_back);
    }

    protected void initToolbar(int type) {
        mToolbar = _findViewById(R.id.toolbar);
        mBarLayout = _findViewById(R.id.app_toolbar_layout);
        Drawable wrap;
        if (type == TYPE_child_other_back) {
            wrap = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_back));
        } else {
            wrap = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear));
        }
        mToolbar.setNavigationIcon(wrap);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSumContext().popTopFragment(getBackData());
            }
        });
    }

    //子类重新返回上个fragment传递的参数对象
    protected Object getBackData() {
        return null;
    }

    public <T> T _findViewById(int id) {
        return (T) mView.findViewById(id);
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

    }

    @Override
    public void onRefreshTop() {

    }

    @Override
    public void onRefreshMore() {

    }

    @Override
    public void onRefreshNoMore() {
        SnackUtil.shortShow(mView,R.string.load_null);
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
        AppDelegate.getRefWatcher(getActivity()).watch(this);
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
}
