package com.xhl.world.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhl.xhl_library.utils.BitmapUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Sum on 16/1/26.
 */
public class ScaleImageFragment extends Fragment {

    private String mImageSource;
    private int mImageType;
    private PhotoView mScaleView;

    public static ScaleImageFragment instance(String source, int type) {
        ScaleImageFragment fragment = new ScaleImageFragment();

        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        bundle.putInt("type", type);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageSource = getArguments().getString("source");
        mImageType = getArguments().getInt("type");
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
        if (mImageType == 1) {
            Bitmap bitmap = BitmapUtil.readBitmapForFixMaxSize(mImageSource);
            mScaleView.setImageBitmap(bitmap);

        } else if (mImageType == 2) {
            x.image().loadDrawable(mImageSource, null, new Callback.CommonCallback<Drawable>() {
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
    }

    //表示当前View是否有缩放
    public boolean phoneHasScale() {
        return mScaleView.getScale() != 1;
    }

    //缩放1.0
    public void resetScale() {
        mScaleView.setScale(1.0f, true);
    }
}
