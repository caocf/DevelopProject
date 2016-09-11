package com.ebox.Anetwork;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class RequestManager
{
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private RequestManager()
    {
        // no instances
    }

    public static void init(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);

        int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/16th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 16;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
                cacheSize));
    }

    public static RequestQueue getRequestQueue()
    {
        if (mRequestQueue != null)
        {
            return mRequestQueue;
        }
        else
        {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void addRequest(Request<?> request, Object tag)
    {
        if (tag != null)
        {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag)
    {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache}
     * which effectively means that no memory caching is used. This is useful
     * for images that you know that will be show only once.
     * 
     * @return
     */
    public static ImageLoader getImageLoader()
    {
        if (mImageLoader != null)
        {
            return mImageLoader;
        }
        else
        {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static void loadImage(final ImageView imageView,
            final String imageUrl, final int loadingImageId)
    {
        ImageListener listener = ImageLoader.getImageListener(imageView,
                loadingImageId, 0);
        RequestManager.getImageLoader().get(imageUrl, listener);
    }
    
    

    public static String getImageUrl(String imageName)
    {
        if (TextUtils.isEmpty(imageName))
        {
            return "";
        }

        if (imageName.length() > 28)
        {
            return imageName.substring(0, 28);
        }

        return imageName;
    }
}
