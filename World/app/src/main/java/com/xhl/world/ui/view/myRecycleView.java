package com.xhl.world.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sum on 15/12/1.
 */
public class myRecycleView extends RecyclerView {

    private boolean isReceiveTouchEvent = false;

    private View mParentView;

    public myRecycleView(Context context) {
        super(context);
    }

    public myRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    public void setIsReceiveTouchEvent(boolean isReceiveTouchEvent) {
        this.isReceiveTouchEvent = isReceiveTouchEvent;
    }

    public void setParentView(View mParentView) {
        this.mParentView = mParentView;
    }
}
