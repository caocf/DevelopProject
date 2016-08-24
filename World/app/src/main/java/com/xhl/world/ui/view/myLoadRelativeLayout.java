package com.xhl.world.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Sum on 15/12/11.
 */
public class myLoadRelativeLayout extends AutoRelativeLayout {

    private ViewHelper.LoadView mLoadView;
    private View mLoadingView;
    private boolean mIsShowLoadingView = false;

    private View mErrorView;
    private boolean mIsShowErrorView = false;

    private Context mContext;

    public myLoadRelativeLayout(Context context) {
        super(context);
        mContext = context;
    }

    public myLoadRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public myLoadRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void showLoadingView() {
        if (mIsShowLoadingView) {
            return;
        }
        if (mIsShowErrorView) {
            closeNetWorkErrorView();
        }
        if (mLoadView == null) {
            mLoadView = ViewHelper.getLoadingView(mContext);
            mLoadingView = mLoadView.mParent;
        }
        if (mLoadingView.getParent() != null) {
            this.removeView(mLoadingView);
        }
        this.addView(mLoadingView);

        mLoadView.mProgress.start();

        mIsShowLoadingView = true;
    }

    public void closeLoadingView() {
        if (mLoadingView != null && mIsShowLoadingView) {
            this.removeView(mLoadingView);
            mLoadView.mProgress.stop();
            mIsShowLoadingView = false;
        }
    }

    public void showNetWorkErrorView() {
        if (mIsShowLoadingView) {
            closeLoadingView();
        }
        if (mErrorView == null) {
            mErrorView = ViewHelper.getNetWorkErrorView(mContext);
        }
        this.addView(mErrorView);
        mIsShowErrorView = true;
    }

    public void closeNetWorkErrorView() {
        if (mErrorView != null && mIsShowErrorView) {
            this.removeView(mErrorView);
        }
        mErrorView = null;
        mIsShowErrorView = false;
    }
}
