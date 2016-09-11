package com.moge.gege.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewUtil
{
    private static int mWindowWidth;
    private static int mWindowHeight;
    private static float mDensity;
    private static float mFontDensity;
    private static boolean mNeedScale;

    private static float mReferencedDensity = 1.0f;
    private static float mReferencedFontDensity = 1.0f;

    private static float mScaleRate;

    private static final String LOG_TAG = "ViewUtil";
    private static int TARGET_TV_WIDTH = 1920;
    private static int TARGET_TV_HEIGHT = 1080;
    public static final int INVALID = Integer.MIN_VALUE;

    public static void init(Context context)
    {
        // DisplayMetrics dm = new DisplayMetrics();
        // ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        mWindowWidth = dm.widthPixels;
        mWindowHeight = dm.heightPixels;

        LogUtil.i(LOG_TAG, "mWindowWidth=" + mWindowWidth + ",mWindowHeight"
                + mWindowHeight);

        mFontDensity = dm.scaledDensity;
        LogUtil.i(LOG_TAG, "mFontDensity=" + mFontDensity);

        mDensity = dm.density;
        LogUtil.i(LOG_TAG, "mDensity=" + mDensity);

        LogUtil.i(LOG_TAG, "densityDpi=" + dm.densityDpi);

        mNeedScale = false;
        if (mWindowWidth != TARGET_TV_WIDTH
                || mWindowHeight != TARGET_TV_HEIGHT
                || Math.abs(mDensity - mReferencedDensity) >= 0.001
                || Math.abs(mFontDensity - mReferencedFontDensity) >= 0.001)
        {
            mNeedScale = true;
        }

        // double widthScaleRate = 1.0 * mWindowWidth / (TARGET_TV_WIDTH *
        // mDensity);
        double widthScaleRate = 1.0 * mWindowWidth / (TARGET_TV_WIDTH);
        LogUtil.i(LOG_TAG, "widthScaleRate=" + widthScaleRate);

        // double heightScaleRate = 1.0 * mWindowHeight / (TARGET_TV_HEIGHT *
        // mDensity);
        double heightScaleRate = 1.0 * mWindowHeight / (TARGET_TV_HEIGHT);
        LogUtil.i(LOG_TAG, "heightScaleRate=" + heightScaleRate);

        mScaleRate = (float) (widthScaleRate < heightScaleRate ? widthScaleRate
                : heightScaleRate);

        LogUtil.i(LOG_TAG, "mScaleRate=" + mScaleRate);

        if (Math.abs(mScaleRate - 1.0) <= 0.001)
        {
            mNeedScale = false;
        }

        LogUtil.i(LOG_TAG, "mNeedScale=" + mNeedScale);
    }

    public static int getWidth()
    {
        return mWindowWidth;
    }

    public static int getHeight()
    {
        return mWindowHeight;
    }

    public static float getFontDensity()
    {
        return mFontDensity;
    }

    public static float getDensity()
    {
        return mDensity;
    }

    public static void scaleView(View v)
    {
        if (null != v)
        {
            scaleView(v, mScaleRate);
        }
        else
        {
            LogUtil.w(LOG_TAG, "scaleView : view is null");
        }
    }

    public static void scaleView(View v, float fRate)
    {
        if (!mNeedScale || v == null || fRate <= 0.001)
        {
            return;
        }

        if (v instanceof TextView)
        {
            TextView txtView = (TextView) v;
            float f = (float) (txtView.getTextSize() * fRate / mFontDensity);
            txtView.setTextSize(f);
        }

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) v
                .getLayoutParams();
        if (null != params)
        {
            // Width & Height
            if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.width != ViewGroup.LayoutParams.FILL_PARENT)
            {
                params.width = (int) (params.width * fRate);
            }

            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.height != ViewGroup.LayoutParams.FILL_PARENT)
            {
                params.height = (int) (params.height * fRate);
            }

            // Padding
            v.setPadding((int) (v.getPaddingLeft() * fRate),
                    (int) (v.getPaddingTop() * fRate),
                    (int) (v.getPaddingRight() * fRate),
                    (int) (v.getPaddingBottom() * fRate));
        }

        // Margin
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) v
                    .getLayoutParams();
            if (params2 != null)
            {
                params2.leftMargin *= fRate;
                params2.rightMargin *= fRate;
                params2.topMargin *= fRate;
                params2.bottomMargin *= fRate;
            }
        }

    }

    public static void setViewSize(View v, int width, int height)
    {
        int paramWidth = (int) (width * mScaleRate);
        int paramHeight = (int) (height * mScaleRate);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params == null)
        {
            return;
        }
        if (width != INVALID)
        {
            params.width = paramWidth;
        }
        if (height != INVALID)
        {
            params.height = paramHeight;
        }
        v.setLayoutParams(params);
    }

    public static void setViewSizeByHeight(View v, int width, int height)
    {
        double heightScaleRate = 1.0 * mWindowHeight / (TARGET_TV_HEIGHT);
        int paramWidth = (int) (width * heightScaleRate);
        int paramHeight = (int) (height * heightScaleRate);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v
                .getLayoutParams();
        if (width != INVALID)
        {
            params.width = paramWidth;
        }
        if (height != INVALID)
        {
            params.height = paramHeight;
        }
        v.setLayoutParams(params);
    }

    public static void setViewPadding(View v, int left, int top, int right,
            int bottom)
    {
        left = (int) (left * mScaleRate);
        top = (int) (top * mScaleRate);
        right = (int) (right * mScaleRate);
        bottom = (int) (bottom * mScaleRate);
        v.setPadding(left, top, right, bottom);
    }

    public static void setViewMargin(View v, int left, int right, int top,
            int bottom)
    {
        int paramLeft = (int) (left * mScaleRate);
        int paramRight = (int) (right * mScaleRate);
        int paramTop = (int) (top * mScaleRate);
        int paramBottom = (int) (bottom * mScaleRate);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v
                .getLayoutParams();
        if (left != INVALID)
        {
            params.leftMargin = paramLeft;
        }
        if (right != INVALID)
        {
            params.rightMargin = paramRight;
        }
        if (top != INVALID)
        {
            params.topMargin = paramTop;
        }
        if (bottom != INVALID)
        {
            params.bottomMargin = paramBottom;
        }
        v.setLayoutParams(params);
    }

    public static float formateTextSize(float txtSize)
    {
        return txtSize * mScaleRate / mFontDensity;
    }

    public static int getWidthSize(int size)
    {
        return (int) (size * mScaleRate);
    }

    public static void requestFocus(View v)
    {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.requestFocusFromTouch();
    }

    public static float getScaleRate()
    {
        return mScaleRate;
    }
}
