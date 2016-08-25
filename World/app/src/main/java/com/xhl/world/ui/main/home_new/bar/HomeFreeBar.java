package com.xhl.world.ui.main.home_new.bar;

import android.content.Context;
import android.util.AttributeSet;

import com.xhl.world.R;
import com.xhl.world.ui.view.pub.BaseBar;

/**
 * Created by Sum on 15/11/26.
 */
public class HomeFreeBar extends BaseBar {


    public HomeFreeBar(Context context) {
        super(context);
    }

    public HomeFreeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_h_free;
    }
}
