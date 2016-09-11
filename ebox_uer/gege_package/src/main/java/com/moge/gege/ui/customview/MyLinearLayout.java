package com.moge.gege.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moge.gege.network.util.RequestManager;

public class MyLinearLayout extends LinearLayout
{

    public MyLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyLinearLayout(Context context)
    {
        super(context);
    }

    // load imageview
    protected void setImage(final ImageView imageView, final String imageUrl,
            final int loadingImageId)
    {
        RequestManager.loadImage(imageView, imageUrl, loadingImageId);
    }
}
