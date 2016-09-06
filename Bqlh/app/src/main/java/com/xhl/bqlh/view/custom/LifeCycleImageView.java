package com.xhl.bqlh.view.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.xhl.bqlh.R;
import com.xhl.xhl_library.network.ImageLoadManager;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Sum on 15/12/3.
 */
public class LifeCycleImageView extends ImageView {

    private String mUrl;//图   片下载地址
    private boolean mIsLoadSuccess;//加载是否成功
    private boolean mIsLoading;
    private ImageLoadListener mListener;
    public static final ImageOptions imageOptions = new ImageOptions.Builder()
//            .setSize(100, 100)
//            .setRadius(DensityUtil.dip2px(1))
            .setImageScaleType(ScaleType.FIT_XY)
            .setLoadingDrawableId(R.drawable.icon_net_error_show)
            .setFailureDrawableId(R.drawable.icon_net_error_show)
            .build();

    public static  void LoadImageView(ImageView image, String url) {
        x.image().bind(image, url, imageOptions);
    }


    private ImageLoadCallBack imageLoadCallBack = new ImageLoadCallBack();
    private boolean mIsLoadImage = false;

    public LifeCycleImageView(Context context) {
        super(context);
    }

    public LifeCycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LifeCycleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mUrl != null && mUrl.equals(url)) {
            reLoadUrl();
            return;
        }
        mUrl = url;
        mIsLoadSuccess = false;
        mIsLoading = false;
        mIsLoadImage = false;
        start();
    }

    private void reLoadUrl() {
        if (!mIsLoading) {
            start();
        }
    }

    private void start() {
        if (!TextUtils.isEmpty(mUrl)) {
            ImageLoadManager.instance().LoadImage(this, mUrl, imageOptions, imageLoadCallBack);
            mIsLoading = true;
        }
    }

    public void LoadDrawable(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mUrl = url;
        mIsLoadSuccess = false;
        mIsLoading = false;
        mIsLoadImage = true;
        loadImage();
    }

    private void loadImage() {
        x.image().loadDrawable(mUrl, imageOptions, imageLoadCallBack);
        mIsLoading = true;

    }

    public void setListener(ImageLoadListener mListener) {
        this.mListener = mListener;
    }

    public interface ImageLoadListener {

        void onSuccess();

        void onFailed();
    }

    private void onErrorExecute() {
        mIsLoadSuccess = false;
        mIsLoading = false;
        if (mListener != null) {
            mListener.onFailed();
        }
        //支持长安重新加载
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mIsLoadSuccess) {
                    start();
                }
                return true;
            }
        });
    }


    class ImageLoadCallBack implements Callback.CommonCallback<Drawable> {

        public ImageLoadCallBack() {
        }

        @Override
        public void onSuccess(Drawable result) {
            mIsLoadSuccess = true;
            mIsLoading = false;
            //直接下载图片后设置显示
            if (mIsLoadImage && result != null) {
                setImageDrawable(result);
            }
            if (mListener != null) {
                mListener.onSuccess();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            onErrorExecute();
        }

        @Override
        public void onCancelled(CancelledException cex) {
            onErrorExecute();
        }

        @Override
        public void onFinished() {
        }
    }

}
