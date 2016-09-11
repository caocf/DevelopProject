package com.moge.gege.util.widget.wheel.time;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.moge.gege.R;
import com.moge.gege.util.widget.wheel.WheelView;

public abstract class TimeCtrl extends LinearLayout
{
    protected static final String NUMBER_FORMAT = "%02d";
    protected static final int[] TRANSPARENT_COLORS = { 0, 0, 0 };

    public TimeCtrl(Context paramContext)
    {
        super(paramContext);
    }

    public TimeCtrl(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public View[] getWheelViews()
    {
        ViewGroup localViewGroup = (ViewGroup) getChildAt(0);
        View[] arrayOfView = new View[localViewGroup.getChildCount()];
        int i = localViewGroup.getChildCount();
        for (int j = 0; j < i; j++)
            arrayOfView[j] = localViewGroup.getChildAt(j);
        return arrayOfView;
    }

    public final void setWheelLeftBackground(int paramInt)
    {
        View localView = findViewById(R.id.wheel_left);
        if (localView != null)
            localView.setBackgroundResource(paramInt);
    }

    public void setWheelMiddleBackground(int paramInt)
    {
        View localView = findViewById(R.id.wheel_middle);
        if (localView != null)
            localView.setBackgroundResource(paramInt);
    }

    public final void setWheelRightBackground(int paramInt)
    {
        View localView = findViewById(R.id.wheel_right);
        if (localView != null)
            localView.setBackgroundResource(paramInt);
    }

    protected void setWheelViewGlobal(WheelView paramWheelView,
            boolean paramBoolean)
    {
        paramWheelView.setBackgroundResource(R.drawable.transparent_background);
        paramWheelView
                .setCenterDrawableResource(R.drawable.transparent_background);
        paramWheelView.setTopShadowColors(TRANSPARENT_COLORS);
        paramWheelView.setBottomShadowColors(TRANSPARENT_COLORS);
        paramWheelView.setCyclic(paramBoolean);
    }
}