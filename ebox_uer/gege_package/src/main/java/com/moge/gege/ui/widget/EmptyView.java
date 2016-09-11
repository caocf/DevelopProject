package com.moge.gege.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.moge.gege.R;
import com.moge.gege.util.LogUtil;
import pl.droidsonroids.gif.GifDrawable;

import java.io.IOException;

/**
 * Created by sam on 2014/11/19.
 */
public class EmptyView extends LinearLayout implements View.OnClickListener
{
    private int loadingRes;
    private int emptyRes;
    private int failRes;
    private ImageView mLoadingImage;
    private ViewGroup mLoadingLayout;
    private GifDrawable mGifDrawable;
    private OnEmptyViewClickListener mOnEmptyViewClickListener;
    private TextView mResultText;
    private ImageView mResultImage;
    private ViewGroup mResultLayout;

    public EmptyView(Context context)
    {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs)
    {
        LayoutInflater.from(context)
                .inflate(R.layout.custom_empty_view, this);
        this.mLoadingLayout = ((ViewGroup) findViewById(R.id.loadingLayout));
        this.mResultLayout = ((ViewGroup) findViewById(R.id.resultLayout));
        this.mLoadingImage = ((ImageView) findViewById(R.id.loadingImage));
        this.mResultImage = ((ImageView) findViewById(R.id.resultImage));
        this.mResultText = ((TextView) findViewById(R.id.resultText));
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultLayout.setOnClickListener(this);
        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.EmptyView, 0, 0);

            this.loadingRes = a
                    .getResourceId(R.styleable.EmptyView_loadingDrawable,
                            attrs.getAttributeResourceValue(null, "src", 0));
            this.emptyRes = a
                    .getResourceId(R.styleable.EmptyView_emptyDrawable,
                            attrs.getAttributeResourceValue(null, "src", 0));
            this.failRes = a
                    .getResourceId(R.styleable.EmptyView_failDrawable,
                            attrs.getAttributeResourceValue(null, "src", 0));
            a.recycle();
        }
    }

    public void onClick(View view)
    {
        if ((view.getId() == R.id.resultLayout) && (
                this.mOnEmptyViewClickListener != null))
        {
            this.mOnEmptyViewClickListener.onEmptyViewClick();
        }
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (this.mGifDrawable != null)
        {
            this.mGifDrawable.stop();
            this.mGifDrawable = null;
        }
    }

    public void showLoadingView()
    {
        this.mResultLayout.setVisibility(View.GONE);
        this.mLoadingLayout.setVisibility(View.VISIBLE);
        try
        {
            StringBuilder localStringBuilder1 = new StringBuilder();
            GifDrawable localGifDrawable = new GifDrawable(
                    getContext().getResources(), this.loadingRes);
            this.mGifDrawable = localGifDrawable;
            StringBuilder localStringBuilder2 = new StringBuilder();
            this.mLoadingImage.setImageDrawable(this.mGifDrawable);
            return;
        }
        catch (Resources.NotFoundException e)
        {
            LogUtil.logException(e);
        }
        catch (IOException e)
        {
            LogUtil.logException(e);
        }
    }

    public void showLoadingView(int resId)
    {
        this.mResultLayout.setVisibility(View.GONE);
        this.mLoadingLayout.setVisibility(View.VISIBLE);
        try
        {
            GifDrawable localGifDrawable = new GifDrawable(
                    getContext().getResources(), resId);
            this.mGifDrawable = localGifDrawable;
            this.mLoadingImage.setImageDrawable(this.mGifDrawable);
            return;
        }
        catch (Resources.NotFoundException e)
        {
            LogUtil.logException(e);
        }
        catch (IOException e)
        {
            LogUtil.logException(e);
        }
    }

    public void showEmptyResultView()
    {
        showEmptyResultView(R.string.load_empty);
    }

    public void showEmptyResultView(int resId)
    {
        this.mResultLayout.setVisibility(View.VISIBLE);
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultImage.setImageResource(this.emptyRes);
        this.mResultText.setText(resId);
    }

    public void showEmptyResultView(String result)
    {
        this.mResultLayout.setVisibility(View.VISIBLE);
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultImage.setImageResource(this.emptyRes);
        this.mResultText.setText(result);
    }

    public void showFailResultView()
    {
        this.mResultLayout.setVisibility(View.VISIBLE);
        this.mResultImage.setImageResource(this.failRes);
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultText.setText(R.string.load_failed);
    }

    public void showFailResultView(int resId)
    {
        this.mResultLayout.setVisibility(View.VISIBLE);
        this.mResultImage.setImageResource(this.failRes);
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultText.setText(resId);
    }

    public void showFailResultView(String result)
    {
        this.mResultLayout.setVisibility(View.VISIBLE);
        this.mResultImage.setImageResource(this.failRes);
        this.mLoadingLayout.setVisibility(View.GONE);
        this.mResultText.setText(result);
    }

    public void hideView()
    {
        setVisibility(View.GONE);
    }

    public void setOnEmptyViewClickListener(
            OnEmptyViewClickListener emptyViewClickListener)
    {
        this.mOnEmptyViewClickListener = emptyViewClickListener;
    }

    public static abstract interface OnEmptyViewClickListener
    {
        public abstract void onEmptyViewClick();
    }
}
