package com.moge.gege.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moge.gege.R;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by sam on 2014/11/19.
 */
public class NumberEditView extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private OnNumberViewChangeListener mOnNumberChangeListener;

    private ImageView mReduceImage;
    private ImageView mPlusImage;
    private EditText mNumberEdit;

    private int mNumberValue = 1;
    private int mMinValue = 1;
    private int mMaxValue = Integer.MAX_VALUE;

    public NumberEditView(Context context) {
        this(context, null);
    }

    public NumberEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout(context, attrs);
    }

    public NumberEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context)
                .inflate(R.layout.custom_number_view, this);


        this.mReduceImage = ((ImageView) findViewById(R.id.reduceImage));
        this.mPlusImage = ((ImageView) findViewById(R.id.plusImage));
        this.mNumberEdit = ((EditText) findViewById(R.id.inputNumberEdit));
        mNumberEdit.addTextChangedListener(mTextWatcher);

        this.mReduceImage.setOnClickListener(this);
        this.mPlusImage.setOnClickListener(this);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            mNumberValue = FunctionUtil.parseIntByString(charSequence.toString());
            if (mNumberValue < mMinValue) {
                mNumberEdit.setText(mMinValue + "");
                mNumberValue = mMinValue;
            } else if (mNumberValue > mMaxValue) {
                mNumberEdit.setText(mMaxValue + "");
                mNumberValue = mMaxValue;
            }

            mNumberEdit.setSelection(mNumberEdit.length());
            if (mOnNumberChangeListener != null) {
                mOnNumberChangeListener.onNumberChange(mNumberValue);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plusImage:
                if (mNumberValue >= mMaxValue) {
                    return;
                }
                mNumberValue++;
                break;
            case R.id.reduceImage:
                if (mNumberValue <= mMinValue) {
                    return;
                }
                mNumberValue--;
                break;
            default:
                break;
        }

        mNumberEdit.setText(mNumberValue + "");
    }

    public NumberEditView setNumberValue(int value) {
        this.mNumberValue = value;
        mNumberEdit.setText(value + "");
        return this;
    }

    public NumberEditView setMaxValue(int value) {

        this.mMaxValue = value;
        if(mMaxValue == 0)
        {
            mMaxValue = 1;
        }
        return this;
    }

    public NumberEditView setMinValue(int value) {
        this.mMinValue = value;
        return this;
    }

    public void setOnNumberViewChangeListener(
            OnNumberViewChangeListener numberChangeListener) {
        this.mOnNumberChangeListener = numberChangeListener;
    }

    public static abstract interface OnNumberViewChangeListener {
        public abstract void onNumberChange(int value);
    }
}
