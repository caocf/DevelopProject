package com.xhl.bqlh.business.view.helper.pub;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecyclerViewScrollBottomListener;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecyclerViewScrollStateListener;
import com.xhl.xhl_library.ui.recyclerview.OnPageScrollListener;

/**
 * Created by Sum on 15/12/12.
 */
public class PageScrollListener extends OnPageScrollListener {

    private View mTopView;

    private RecyclerViewScrollStateListener mStateListener;

    private RecyclerViewScrollBottomListener mBottomListener;

    private int topViewShowLocation = 6;

    private boolean isScrollTag = false;

    public PageScrollListener(LinearLayoutManager manager) {
        super(manager);
    }

    @Override
    public void onItemCount(int firstVisible, int visibleCount, int totalCount, int dx, int dy) {
        super.onItemCount(firstVisible, visibleCount, totalCount, dx, dy);
        if (firstVisible > topViewShowLocation) {
            ViewHelper.setViewGone(mTopView, true);
        } else {
            ViewHelper.setViewGone(mTopView, false);
        }

//        Logger.v(dx + ";dy:" + dy);

        if (dy > 10) {//向上
            if (!isScrollTag) {
                if (mStateListener != null) {
                    mStateListener.onScrollingUp();
                }
                isScrollTag = true;
            }
        } else if (dy < -10) {//向下
            if (isScrollTag) {
                if (mStateListener != null) {
                    mStateListener.onScrollingDown();
                }
                isScrollTag = false;
            }
        }
    }

    @Override
    public void onPageScrolled(RecyclerView recyclerView) {
        if (mStateListener != null) {
            mStateListener.onScrollBottom();
        } else if (mBottomListener != null) {
            mBottomListener.onScrollBottom();
        }
    }

    public PageScrollListener setTopView(View topView) {
        this.mTopView = topView;
        return this;
    }

    public PageScrollListener addStateListener(RecyclerViewScrollStateListener listener) {
        this.mStateListener = listener;
        return this;
    }

    public PageScrollListener addBottomListener(RecyclerViewScrollBottomListener listener) {
        this.mBottomListener = listener;
        return this;
    }

    public PageScrollListener setTopViewShowLocation(int topViewShowLocation) {
        this.topViewShowLocation = topViewShowLocation;
        return this;
    }
}
