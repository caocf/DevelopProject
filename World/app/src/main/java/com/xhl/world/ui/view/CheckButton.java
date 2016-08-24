package com.xhl.world.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Sum on 15/12/4.
 */
public class CheckButton extends AutoRelativeLayout implements Checkable{
    protected Context mContext;
    private boolean isChecked;
    private int[] mImageResIdArray = new int[2];
    private int[] mTextColorResIdArray = new int[2];
    private ImageView mCheckIcon;
    private TextView mCheckText;

    public CheckButton(Context context) {
        this(context, null);
    }

    public CheckButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.check_button, this, true);
        mCheckIcon = (ImageView) findViewById(R.id.check_item_image);
        mCheckText = (TextView) findViewById(R.id.check_item_text);
    }

    public void setCheckedImageRes(int resId) {
        mImageResIdArray[0] = resId;
    }

    public void setUnCheckedImageRes(int resId) {
        mImageResIdArray[1] = resId;
    }

    public void setCheckedTextColorRes(int resId) {
        mTextColorResIdArray[0] = resId;
    }

    public void setUnCheckedTextColorRes(int resId) {
        mTextColorResIdArray[1] = resId;
    }

    public void setTextRes(int resId){
        mCheckText.setText(resId);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        toggle();
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if (isChecked) {
            mCheckIcon.setImageResource(mImageResIdArray[0]);
            mCheckText.setTextColor((mTextColorResIdArray[0]));
        } else {
            mCheckIcon.setImageResource(mImageResIdArray[1]);
            mCheckText.setTextColor(mTextColorResIdArray[1]);
        }
    }

}
