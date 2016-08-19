package com.xhl.bqlh.view.ui.activity;

import android.os.Bundle;

import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.ui.fragment.SearchFragment;

/**
 * Created by Sum on 16/7/1.
 * SingleTask
 */
public class SearchActivity extends BaseAppActivity {

    @Override
    protected boolean isNeedEventBus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        pushFragmentToBackStack(SearchFragment.class, null);

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {

    }
}
