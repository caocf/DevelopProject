package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Sum on 15/12/1.
 *  首页懒加载加载更多提示
 */
public class HomeLoadMoreBar extends BaseHomeBar {
    public HomeLoadMoreBar(Context context) {
        super(context);
    }

    public HomeLoadMoreBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void initParams() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
