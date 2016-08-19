package com.xhl.bqlh.view.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.xhl_library.utils.BitmapUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Sum on 16/6/2.
 */
public class ImageDetailsFragment extends BaseAppFragment {

    private PhotoView mScaleView;
    private String mImageUrl;

    @Override
    public boolean processBackPressed() {
        if (ImageHasScale()) {
            ResetScale();
            return true;
        }
        return super.processBackPressed();
    }

    public static ImageDetailsFragment instance(String imageUrl) {
        ImageDetailsFragment fragment = new ImageDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {

    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof String) {
            mImageUrl = (String) data;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mScaleView = new PhotoView(getContext());

        mScaleView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mScaleView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        mScaleView.setBackgroundColor(Color.TRANSPARENT);

        mScaleView.setZoomable(true);

        return mScaleView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mImageUrl = bundle.getString("image");
        }

        if (URLUtil.isNetworkUrl(mImageUrl)) {
            loadNetImage();
        } else {
            Bitmap bitmap = BitmapUtil.readBitmapForFixMaxSize(mImageUrl, 720, 1080);
            if (bitmap != null) {
                mScaleView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onDestroyView() {
        mScaleView.setImageDrawable(null);
        super.onDestroyView();
    }

    private void loadNetImage() {
        x.image().loadDrawable(mImageUrl, null, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                if (result != null) {
                    mScaleView.setImageDrawable(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //表示当前View是否有缩放
    public boolean ImageHasScale() {
        if (mScaleView == null) {
            return false;
        }
        return mScaleView.getScale() != 1;
    }

    //缩放1.0
    public void ResetScale() {
        mScaleView.setScale(1.0f, true);
    }
}
