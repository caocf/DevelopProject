package com.xhl.bqlh.business.view.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.SwipeRefresh.MaterialProgressDrawable;

/**
 * Created by Sum on 16/3/14.
 */
public class LoadingViewMaker {

    public static class LoadView {

        public MaterialProgressDrawable mDrawable;

        public ViewGroup mParentView;

        public void showLoadView() {
            if (mDrawable != null) {
                mDrawable.start();
            }
        }

        public void stopLoadView() {
            if (mDrawable != null) {
                mDrawable.stop();
            }
        }

    }


    public static LoadView getSimpleLoadingView(Context context) {

        LoadView loadView = new LoadView();

        FrameLayout viewParent = new FrameLayout(context);

        ImageView imageView = new ImageView(context);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 80);

        params.gravity = Gravity.CENTER;

        imageView.setLayoutParams(params);

        viewParent.addView(imageView);

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.colorAccent);
        drawable.setColorSchemeColors(color1);
        imageView.setImageDrawable(drawable);
        drawable.setAlpha(255);

        //创建加载的View
        loadView.mDrawable = drawable;
        loadView.mParentView = viewParent;
        loadView.showLoadView();

        return loadView;
    }

}
