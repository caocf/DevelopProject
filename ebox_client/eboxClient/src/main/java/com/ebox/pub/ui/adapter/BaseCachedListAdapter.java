package com.ebox.pub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.ebox.Anetwork.RequestManager;


// TODO: Auto-generated Javadoc
/**
 * The Class BaseCachedListAdapter.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class BaseCachedListAdapter<T> extends BaseListAdapter<T>
{

    /** The m inflater. */
    protected LayoutInflater mInflater;

    /** The m context. */
    protected Context mContext;

    /**
     * Instantiates a new base cached list adapter.
     * 
     * @param context
     *            the context
     */
    public BaseCachedListAdapter(Context context)
    {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Sets the image.
     * 
     * @param imageView
     *            the image view
     * @param imageUrl
     *            the image url
     * @param loadingImageId
     *            the loading image id
     */
    protected void setImage(final ImageView imageView, final String imageUrl,
            final int loadingImageId)
    {
        RequestManager.loadImage(imageView, imageUrl, loadingImageId);
    }

    /**
     * this should be called when Activity is destoried.
     * 
     */
    public void destory()
    {
        super.clear();
        mInflater = null;
    }

    public String getString(int resId)
    {
        return mContext.getResources().getString(resId);
    }

    public String getString(int id, Object... formatArgs)
    {
        return mContext.getResources().getString(id, formatArgs);
    }

    public static String getImageUrl(String imageName)
    {
        return RequestManager.getImageUrl(imageName);
    }
}
