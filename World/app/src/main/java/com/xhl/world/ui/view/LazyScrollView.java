package com.xhl.world.ui.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Sum on 15/11/23.
 */
public class LazyScrollView extends NestedScrollView {


    private ScrollListener mListener;


    public LazyScrollView(Context context) {
        super(context);
    }

    public LazyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LazyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setmListener(ScrollListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //滚动到底部
        if (scrollY > 0 && clampedY) {
            if (mListener != null) {
                mListener.onScrollBottom();
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollY(t, oldt);
        }
    }


    public interface ScrollListener {
        void onScrollBottom();

        void onScrollY(int nowY, int oldY);
    }
}
