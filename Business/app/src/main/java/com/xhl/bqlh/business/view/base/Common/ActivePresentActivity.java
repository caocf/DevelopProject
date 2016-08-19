package com.xhl.bqlh.business.view.base.Common;

import android.content.Context;

/**
 * Created by Sum on 16/6/23.
 */
public class ActivePresentActivity extends ActivePresent {

    public ActivePresentActivity(Context context) {
        super(context);
        if (context instanceof RefreshLoadListener) {
            setLoadListener((RefreshLoadListener) context);
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
