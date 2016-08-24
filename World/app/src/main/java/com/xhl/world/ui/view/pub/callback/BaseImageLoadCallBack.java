package com.xhl.world.ui.view.pub.callback;

import android.view.View;
import android.widget.ImageView;

import com.xhl.xhl_library.network.ImageLoadManager;

import org.xutils.common.Callback;

/**
 * Created by Sum on 15/11/29.
 */
public class BaseImageLoadCallBack implements Callback.CommonCallback {

    private ImageView mImageView;
    private String mUrl;
    private View mParentView;

    public BaseImageLoadCallBack(ImageView imageView, String url) {
        mImageView = imageView;
        mUrl = url;
    }

    @Override
    public void onSuccess(Object result) {
        if (mParentView != null) {
            if (mParentView.getVisibility() != View.VISIBLE) {
                mParentView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageLoadManager.instance().LoadImage(mImageView, mUrl);
            }
        }, 500);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    public void setmParentView(View mParentView) {
        this.mParentView = mParentView;
    }
}
