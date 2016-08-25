package com.xhl.world.ui.main.home_new.bar;

import android.content.Context;

import com.xhl.world.R;
import com.xhl.world.ui.view.pub.BaseBar;

/**
 * Created by Summer on 2016/8/25.
 */
public class HomeSeasonBar extends BaseBar {

    public HomeSeasonBar(Context context) {
        super(context);
    }

    @Override
    protected void initParams() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_h_season_buy;
    }
}
