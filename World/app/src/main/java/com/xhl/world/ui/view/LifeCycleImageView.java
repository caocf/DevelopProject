package com.xhl.world.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.xhl.world.R;
import com.xhl.xhl_library.network.ImageLoadManager;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Sum on 15/12/3.
 */
public class LifeCycleImageView extends ImageView {

    private String mUrl;//图片下载地址
    private boolean mIsLoadSuccess;//加载是否成功
    private boolean mIsLoading;
    private ImageLoadListener mListener;
    private ImageOptions imageOptions = new ImageOptions.Builder()
//            .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//            .setRadius(DensityUtil.dip2px(5))
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            .setLoadingDrawableId(R.drawable.icon_net_error_show3)
            .setFailureDrawableId(R.drawable.icon_net_error_show3)
            .build();
    private int mImageLoadStartSrc = -1;
    private ImageLoadCallBack imageLoadCallBack = new ImageLoadCallBack();
    private ImageView mImage;
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
            if (mImageLoadStartSrc != -1) {
                mImage.setImageResource(mImageLoadStartSrc);
            }
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
        mImage = this;
        if (mImageLoadStartSrc != -1) {
            mImage.setImageResource(mImageLoadStartSrc);
        }
        x.image().loadDrawable(mUrl, imageOptions, imageLoadCallBack);
        mIsLoading = true;

    }

    public void setListener(ImageLoadListener mListener) {
        this.mListener = mListener;
    }

    public void setImageOptions(ImageOptions imageOptions) {
        this.imageOptions = imageOptions;
    }

    public void setmImageLoadStartSrc(int mImageLoadStartSrc) {
        this.mImageLoadStartSrc = mImageLoadStartSrc;
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
//        //网络加载失败加载图片
//        this.setImageResource(mImageLoadFailedRes == -1 ? R.drawable.icon_net_error_show : mImageLoadFailedRes);
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
            if (mIsLoadImage && result != null && mImage != null) {
                mImage.setImageDrawable(result);
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
