package com.xhl.world.ui.view.pub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.x;

/**
 * Created by Sum on 15/11/27.
 */
public abstract class BaseBar extends AutoRelativeLayout {

    protected abstract void initParams();

    protected abstract int getLayoutId();

    protected Context mContext;
    protected View mView;
    private boolean mIsFirstInit = true;

    public BaseBar(Context context) {
        super(context);
        if (mIsFirstInit) {
            initLayout(context);
            mIsFirstInit = false;
        }
    }

    public BaseBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mIsFirstInit) {
            initLayout(context);
            mIsFirstInit = false;
        }
    }

    protected boolean autoInject() {
        return true;
    }

    private void initLayout(Context context) {
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(getLayoutId(), this, true);
        if (autoInject()) {
            x.view().inject(this, mView);
        }

        initParams();
    }

    public <T> T _findViewById(int id) {

        return (T) findViewById(id);
    }

    public void startActivity(Class activity) {
        getContext().startActivity(new Intent(getContext(), activity));
    }

    public void startActivity(Class activity, Bundle bundle) {
        Intent data = new Intent(getContext(), activity);
        data.putExtras(bundle);
        getContext().startActivity(data);
    }

    protected void setTextViewText(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(text);
        }
    }
}
