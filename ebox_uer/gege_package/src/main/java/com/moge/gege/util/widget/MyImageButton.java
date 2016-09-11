package com.moge.gege.util.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;

public class MyImageButton extends RelativeLayout
{
    private Context mContext;
    private TextView mTextView;
    private ImageView mImageView;

    public MyImageButton(Context context)
    {
        this(context, null);
        mContext = context;
    }

    public MyImageButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs)
    {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_myimagebutton, this);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.MyImageButton);

        RelativeLayout layout = (RelativeLayout) view
                .findViewById(R.id.contentLayout);
        mTextView = (TextView) view.findViewById(R.id.contentText);
        mImageView = (ImageView) view.findViewById(R.id.contentImage);

        mTextView
                .setText(styledAttrs.getString(R.styleable.MyImageButton_text));
        mTextView.setTextColor(styledAttrs.getColor(
                R.styleable.MyImageButton_textColor, R.color.black));
        mTextView.setTextSize(styledAttrs.getDimension(
                R.styleable.MyImageButton_textSize, 15));

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTextView
                .getLayoutParams();
        params.leftMargin = (int) styledAttrs.getDimension(
                R.styleable.MyImageButton_distance, 15);

        Drawable drawable = styledAttrs
                .getDrawable(R.styleable.MyImageButton_image);
        if (drawable != null)
        {
            mImageView.setImageDrawable(drawable);
        }
        mImageView.invalidate();

        Drawable drawable2 = styledAttrs
                .getDrawable(R.styleable.MyImageButton_background);
        if (drawable2 != null)
        {
            layout.setBackgroundDrawable(drawable2);
        }
        layout.invalidate();

        view.setClickable(true);
        view.setFocusable(true);

        styledAttrs.recycle();
    }

    public void setImageResource(int resId)
    {
        mImageView.setImageResource(resId);
    }

    public void setText(CharSequence text)
    {
        mTextView.setText(text);
    }
}
