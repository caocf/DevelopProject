package com.xhl.xhl_library.network;

import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Sum on 15/11/24.
 */
public class ImageLoadManager {
    private ImageLoadManager() {
    }

    private static ImageLoadManager loadManager;

    public static ImageLoadManager instance() {
        if (loadManager == null) {
            synchronized (ImageLoadManager.class) {
                loadManager = new ImageLoadManager();
            }
        }
        return loadManager;
    }

    /**
     * @param imageView
     * @param url
     */
    public void LoadImage(ImageView imageView, String url) {
        //绑定图片加载
        x.image().bind(imageView, url);
    }

    /**
     * @param imageView
     * @param url
     * @param options
     */
    public void LoadImage(ImageView imageView, String url, ImageOptions options) {
        x.image().bind(imageView, url, options);
    }

    public void LoadImage(ImageView imageView, String url, ImageOptions options, Callback.CommonCallback callback) {
        x.image().bind(imageView, url, options, callback);
    }

    /**
     * @param imageView
     * @param url
     * @param callback
     */
    public void LoadImage(ImageView imageView, String url, Callback.CommonCallback callback) {
        x.image().bind(imageView, url, callback);
    }

}
