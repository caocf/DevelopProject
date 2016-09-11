package com.moge.gege.network.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.moge.gege.data.PersistentData;
import com.moge.gege.util.ImageLoaderUtil;

public class RequestManager {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static Context mContext;
    public static final String CookieTag = "cookie_tag";

    private RequestManager() {
        // no instances
    }

    public static void init(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);

        int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
                cacheSize));
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static boolean addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
        return true;
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache}
     * which effectively means that no memory caching is used. This is useful
     * for images that you know that will be show only once.
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static void loadImage(final ImageView imageView,
                                 final String imageUrl, final int loadingImageId) {
        // to do list!!!
        // ImageListener listener = ImageLoader.getImageListener(imageView,
        // loadingImageId, 0);
        // RequestManager.getImageLoader().get(
        // NetworkConfig.imageAddress + imageUrl, listener);

        //
        // ImageLoaderUtil.instance().displayImage(
        // NetworkConfig.imageAddress + imageUrl, imageView,
        // loadingImageId);

        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setImageResource(loadingImageId);
        } else {
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                ImageLoaderUtil.instance().displayImage(imageUrl, imageView, loadingImageId);
            } else {
                ImageLoaderUtil.instance().displayImage(
                        PersistentData.instance().getImageAddress() + imageUrl,
                        imageView, loadingImageId);
            }
        }

    }

    public static void loadImageByUrl(final ImageView imageView,
                                      final String imageUrl, final int loadingImageId) {
        ImageLoaderUtil.instance().displayImage(imageUrl, imageView,
                loadingImageId);
    }

    public static String getImageUrl(String imageName) {
        if (TextUtils.isEmpty(imageName)) {
            return "";
        }

        if (imageName.length() > 28) {
            return imageName.substring(0, 28);
        }

        return imageName;
    }
}
