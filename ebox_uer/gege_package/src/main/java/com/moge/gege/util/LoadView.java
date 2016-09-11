package com.moge.gege.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;

public class LoadView
{
    private View mLoadView;
    private View mErrorView;
    private ViewGroup mFragmentView;
    private RelativeLayout mLayout;
    private boolean mHideRetry;

    private Context mContext;

    public LoadView(Context act, int fragmentId)
    {
        mLoadView = ((Activity) act).getLayoutInflater().inflate(
                R.layout.loading, null);
        mErrorView = ((Activity) act).getLayoutInflater().inflate(
                R.layout.loaderror, null);
        mFragmentView = (ViewGroup) ((Activity) act).getWindow().findViewById(
                fragmentId);
        mLayout = new RelativeLayout(act);
        mContext = act;
        // mLayout.setPadding(0, FunctionUtil.dip2px(act, 30), 0, 0);
        init();
    }

    public LoadView(Context act, ViewGroup viewGroup)
    {
        mLoadView = ((Activity) act).getLayoutInflater().inflate(
                R.layout.loading, null);
        mErrorView = ((Activity) act).getLayoutInflater().inflate(
                R.layout.loaderror, null);
        mFragmentView = viewGroup;
        mLayout = new RelativeLayout(act);
        // mLayout.setPadding(0, FunctionUtil.dip2px(act, 30), 0, 0);
        init();
    }

    private void init()
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayout.addView(mLoadView, params);
        mLayout.addView(mErrorView, params);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        mLayout.setLayoutParams(layoutParams);
        addLoadView();
    }

    private void addLoadView()
    {

        mFragmentView.addView(mLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        mLoadView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    public void displayLoadView()
    {
        hideErrorView();
        displayLoadView(null);
    }

    public void displayLoadView(String msg)
    {
        if (msg != null)
        {
            ((TextView) mLoadView.findViewById(R.id.loading_txt)).setText(msg);
        }
        mLayout.setBackgroundColor(mLayout.getResources().getColor(
                R.color.translucent_background));
        mLoadView.setVisibility(View.VISIBLE);
        mLoadView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            }
        });
    }

    public void displayLoadView(int resId)
    {
        displayLoadView(mContext.getResources().getString(resId));
    }

    public void setHideRetry(boolean hideRetry)
    {
        mHideRetry = hideRetry;
    }

    public void hideLoadView()
    {
        mLoadView.setVisibility(View.GONE);
        mLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    public void displayErrorView(String message,
            final AsyncTask<Void, Void, ?> asyncTask)
    {
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        mLayout.setBackgroundColor(mLayout.getResources().getColor(
                R.color.translucent_background));
        if (message != null)
        {
            ((TextView) mErrorView.findViewById(R.id.errorMsg))
                    .setText(message);
        }
        TextView textView = (TextView) mErrorView.findViewById(R.id.errorTx);
        if (mHideRetry)
        {
            textView.setVisibility(View.GONE);
            mHideRetry = false;
        }
        else
        {
            SpannableString content = new SpannableString(mContext
                    .getResources().getString(R.string.retry));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            if (asyncTask != null)
            {
                textView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hideErrorView();
                        asyncTask.execute();
                    }
                });
            }

        }
    }

    public void displayErrorView(final AsyncTask<Void, Void, ?> asyncTask)
    {
        displayErrorView(null, asyncTask);
    }

    public void hideErrorView()
    {
        mErrorView.setVisibility(View.GONE);
        mLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    public void remove()
    {
        mFragmentView.removeView(mLayout);
    }

}
