package com.xhl.bqlh.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;

/**
 * Created by Sum on 15/12/4.
 * 拦截左右滑动导致刷新操作
 */
public class LazySwipeRefreshLayout extends SwipeRefreshLayout {

    private int downX;
    private int downY;
    private boolean isNeedReceive;

    public LazySwipeRefreshLayout(Context context) {
        super(context);
    }

    public LazySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //下拉有左右滑动操作，不用拦截事件
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                isNeedReceive = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isNeedReceive) {
                    float curX = ev.getX();
                    int subX = (int) (curX - downX);
                    downX = (int) curX;
                    float curY = ev.getY();
                    int subY = (int) (curY - downY);
                    downY = (int) curY;
                    //  LogUtil.i("sub:" + subX + " subY:" + subY);
                    if (subY < Math.abs(subX)) {
                        isNeedReceive = false;
                        return false;
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        if (!isNeedReceive) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
