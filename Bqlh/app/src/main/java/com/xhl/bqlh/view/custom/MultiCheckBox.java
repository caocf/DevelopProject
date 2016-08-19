package com.xhl.bqlh.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.R;

/**
 * Created by Sum on 15/12/21.
 */
public class MultiCheckBox extends FrameLayout implements Checkable {
    protected Context mContext;

    private boolean mChecked = false;
    private ImageView mCheckIcon;
    private TextView mCheckText;

    private int mImageDrawableRes;
    private int mCheckColor;//选择色
    private int mDefaultColor;//默认色

    private String mHintText;

    public MultiCheckBox(Context context) {
        this(context, null);
    }

    public MultiCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        initAttrs(context, attrs, 0, 0);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiCheckBox, defStyleAttr, defStyleRes);

        //主题强调色
        int color = ContextCompat.getColor(context, R.color.main_check_color_select);
        mDefaultColor = ContextCompat.getColor(context, R.color.main_check_color_nor);

        mCheckColor = typedArray.getColor(R.styleable.MultiCheckBox_checkColor, color);

        mImageDrawableRes = typedArray.getResourceId(R.styleable.MultiCheckBox_drawableRes, 0);

        mHintText = typedArray.getString(R.styleable.MultiCheckBox_textRes);

        typedArray.recycle();

        applyTint();
    }

    private void initLayout(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.bar_check_button, this, true);
        mCheckIcon = (ImageView) findViewById(R.id.check_item_image);
        mCheckText = (TextView) findViewById(R.id.check_item_text);
    }

    public void setText(String text) {
        mCheckText.setText(text);
    }

    public void setImageRes(int imageRes) {
        mCheckIcon.setImageResource(imageRes);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            mCheckText.setEnabled(mChecked);
            mCheckIcon.setEnabled(mChecked);
            mCheckIcon.refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    private void applyTint() {
        if (mImageDrawableRes != 0 && mCheckColor != 0) {
            Drawable drawable = ContextCompat.getDrawable(mContext, mImageDrawableRes);

            setTint(mCheckIcon, mCheckColor, drawable);
            //更新选择状态
            mCheckIcon.setEnabled(mChecked);
            mCheckIcon.refreshDrawableState();
        }
        if (!TextUtils.isEmpty(mHintText)) {
            mCheckText.setText(mHintText);
            applyTextTint();
        }
    }

    private void applyTextTint() {
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled},
        }, new int[]{
                mDefaultColor,
                mCheckColor,
        });
        mCheckText.setEnabled(mChecked);
        mCheckText.setTextColor(sl);
    }

    private void setTint(ImageView imageView, @ColorInt int color, Drawable bg) {
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed},
        }, new int[]{
                mDefaultColor,
                color,
                color,
        });
        Drawable drawable = DrawableCompat.wrap(bg);
        DrawableCompat.setTintList(drawable, sl);
        imageView.setImageDrawable(drawable);
    }
}
