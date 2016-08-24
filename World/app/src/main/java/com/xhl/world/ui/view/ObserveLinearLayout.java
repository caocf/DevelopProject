package com.xhl.world.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.xhl.world.ui.main.home.callback.HomeImageLifecycleCallBack;

import org.xutils.common.util.LogUtil;

/**
 * Created by Sum on 15/11/28.
 */
public class ObserveLinearLayout extends LinearLayout {

    private int mScreenHeight;
    private int mShowHeight;
    private int mIndex = -1;
    private SparseArray<View> mChildChild = new SparseArray<>();

    public ObserveLinearLayout(Context context) {
        super(context);
        getScreen();
    }

    public ObserveLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getScreen();
    }

    public ObserveLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getScreen();
    }

    private void getScreen() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
    }

    public void updateData() {
        int childCount = getChildCount();
        mShowHeight = 0;
        boolean isCanScroll = false;
        int index = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            mShowHeight += view.getHeight();
            if (mShowHeight > mScreenHeight) {
                mIndex = i;
                isCanScroll = true;
            }
            if (isCanScroll) {
                mChildChild.append(index++, view);
            }
        }
    }

    public void checkChildView(int scrollY) {
        int sub = mScreenHeight + scrollY - mShowHeight;
        View firstView = mChildChild.get(0);

        if (sub < 0) {
            return;
        }
        if (sub > 0 && sub < firstView.getHeight()) {
            changeType(firstView).onVisible();
        }else {

        }
    }

    private HomeImageLifecycleCallBack changeType(View view) {
        if (view instanceof HomeImageLifecycleCallBack) {
            return ((HomeImageLifecycleCallBack) view);
        }
        return new nullCallBack();
    }

    class  nullCallBack implements HomeImageLifecycleCallBack{
        @Override
        public void onVisible() {
            LogUtil.v("null callback onVisible");
        }

        @Override
        public void onInVisible() {
            LogUtil.v("null callback onInVisible");
        }
    }

}
