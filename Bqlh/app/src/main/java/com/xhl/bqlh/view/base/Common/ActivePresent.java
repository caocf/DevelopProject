package com.xhl.bqlh.view.base.Common;

import android.content.Context;

import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.BaseValue;
import com.xhl.bqlh.doman.callback.ContextValue;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;

/**
 * Created by Sum on 16/6/23.
 * 实现常用数据代理处理
 */
public abstract class ActivePresent implements ContextValue {

    public LoadingView loadingView;

    private RefreshLoadListener loadListener;

    //统一管理分页数据
    public int mPageIndex = 1;
    public int mPageSize = 14;
    public int mTotalSize = 0;

    private SwipeRefreshLayout mRefresh;
    //是否使用默认是判断逻辑处理
    private boolean pageDefaultDo = true;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
    }

    @Override
    public Object getValue(int type) {
        return null;
    }

    @Override
    public void showValue(int type, Object obj) {
        if (type == BaseValue.TYPE_TOAST) {
            ToastUtil.showToastShort((String) obj);
        } else if (type == BaseValue.TYPE_DIALOG_PROGRESS_SHOW) {
            loadingView.showLoading((String) obj);
        } else if (type == BaseValue.TYPE_DIALOG_LOADING) {
            loadingView.showLoading();
        } else if (type == BaseValue.TYPE_DIALOG_HIDE) {
            loadingView.hideLoading();
        } else if (type == BaseValue.TYPE_NO_MORE) {
            if (loadListener != null) {
                loadListener.onRefreshNoMore();
            }
        }
    }

    public void initRefreshStyle(SwipeRefreshLayout refreshLayout, SwipeRefreshLayoutDirection direction) {
        if (refreshLayout == null) {
            return;
        }
        mRefresh = refreshLayout;
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setDirection(direction);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeRefreshLayoutDirection direction) {
                if (direction == SwipeRefreshLayoutDirection.TOP) {
                    if (pageDefaultDo) {
                        onRefreshTop();
                    }
                    if (loadListener != null) {
                        loadListener.onRefreshTop();
                    }
                } else if (direction == SwipeRefreshLayoutDirection.BOTTOM) {
                    if (pageDefaultDo) {
                        onRefreshMore();
                    }
                    if (loadListener != null) {
                        loadListener.onRefreshMore();
                    }
                }
            }
        });
    }

    protected void onRefreshTop() {
        mPageIndex = 1;
        if (mRefresh != null && !mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(true);
        }
        if (loadListener != null) {
            loadListener.onRefreshLoadData();
        }
    }

    protected void onRefreshMore() {
        if (mPageIndex * mPageSize >= mTotalSize) {
            if (mRefresh != null) {
                mRefresh.setRefreshing(false);
            }
            if (loadListener != null) {
                loadListener.onRefreshNoMore();
            }
        } else {
            mPageIndex++;
            if (loadListener != null) {
                loadListener.onRefreshLoadData();
            }
        }
    }

    public void setLoadListener(RefreshLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setPageDefaultDo(boolean pageDefaultDo) {
        this.pageDefaultDo = pageDefaultDo;
    }
}
