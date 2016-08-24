package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.ui.activity.SearchActivity;
import com.xhl.world.ui.utils.barcode.ui.CaptureActivity;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.Event;

/**
 * Created by Sum on 15/11/26.
 */
public class HomeTopBar extends BaseBar {

    @Event(value = R.id.ripple_bar_home_top_scan, type = RippleView.OnRippleCompleteListener.class)
    private void onScanClick(View view) {
        startActivity(CaptureActivity.class);
    }

    @Event(value = R.id.ripple_bar_home_top_search, type = RippleView.OnRippleCompleteListener.class)
    private void onSearchClick(View view) {
        startActivity(SearchActivity.class);
    }

    public HomeTopBar(Context context) {
        super(context);
    }

    public HomeTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_top;
    }
}
