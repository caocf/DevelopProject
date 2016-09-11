package com.ebox.pub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class ImageLoaderUtil
{
    private static ImageLoaderUtil instance;

    public static ImageLoaderUtil instance()
    {
        if (instance == null)
        {
            instance = new ImageLoaderUtil();
        }

        return instance;
    }

    public void init(Context context)
    {
        if (!ImageLoader.getInstance().isInited())
        {
            ImageLoader.getInstance().init(
                    getDefaultImageLoaderConfiguration(context));
        }
    }

    public void displayImage(String uri, ImageView imageView)
    {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public void displayImage(String uri, ImageView imageView, int defaultResId)
    {
        ImageLoader.getInstance().displayImage(uri, imageView,
                getDefaultDisplayImageOptions(defaultResId));
    }

    public void displayImage(String uri, ImageView imageView,
            DisplayImageOptions options)
    {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView, int defaultResId,
            ImageLoadingListener listener,
            ImageLoadingProgressListener progressListener)
    {
        ImageLoader.getInstance().displayImage(uri, imageView,
                getDefaultDisplayImageOptions(defaultResId), listener,
                progressListener);
    }

    public void loadImage(String uri, ImageSize targetImageSize,
            ImageLoadingListener listener)
    {
        ImageLoader.getInstance().loadImage(uri, targetImageSize, listener);
    }

    public void loadImage(String uri, ImageSize targetImageSize,
            DisplayImageOptions options, ImageLoadingListener listener)
    {
        ImageLoader.getInstance().loadImage(uri, targetImageSize, options,
                listener);
    }

    private DisplayImageOptions getDefaultDisplayImageOptions(int defaultResId)
    {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true).cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.showImageForEmptyUri(defaultResId);
        builder.showImageOnFail(defaultResId);
        builder.showImageOnLoading(defaultResId);
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        return builder.build();
    }

    private ImageLoaderConfiguration getDefaultImageLoaderConfiguration(
            Context context)
    {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context);
//        builder.defaultDisplayImageOptions(getDefaultDisplayImageOptions(R.drawable.ex_screen_s));
        builder.denyCacheImageMultipleSizesInMemory();
        builder.diskCacheSize(100 * 1024 * 1024);
        builder.memoryCacheSizePercentage(25);
        builder.tasksProcessingOrder(QueueProcessingType.FIFO);
        return builder.build();
    }

    public void clear()
    {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    public void resume()
    {
        ImageLoader.getInstance().resume();
    }

    public void pause()
    {
        ImageLoader.getInstance().pause();
    }

    public void stop()
    {
        ImageLoader.getInstance().stop();
    }

    public static void destroy()
    {
        ImageLoader.getInstance().destroy();
    }

}
