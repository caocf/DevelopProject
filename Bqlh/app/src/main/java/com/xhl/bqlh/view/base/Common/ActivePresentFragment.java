package com.xhl.bqlh.view.base.Common;

import android.support.v4.app.Fragment;

/**
 * Created by Sum on 16/6/23.
 */
public class ActivePresentFragment extends ActivePresent {

    private Fragment fragment;

    public ActivePresentFragment(Fragment fragment) {
        super(fragment.getContext());
        this.fragment = fragment;
        if (fragment instanceof RefreshLoadListener) {
            setLoadListener((RefreshLoadListener) fragment);
        }
    }

    @Override
    protected void onRefreshTop() {
        super.onRefreshTop();
    }

    @Override
    protected void onRefreshMore() {
        super.onRefreshMore();
    }
}
