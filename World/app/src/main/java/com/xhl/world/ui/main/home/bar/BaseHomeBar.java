package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.world.ui.main.home.callback.HomeImageLifecycleCallBack;
import com.xhl.world.ui.view.LifeCycleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */
public abstract class BaseHomeBar extends BaseBar implements HomeImageLifecycleCallBack {
    public BaseHomeBar(Context context) {
        super(context);
    }

    public BaseHomeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onVisible() {
    }

    @Override
    public void onInVisible() {
    }

    protected List<String> getNullUrl(int count) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add("null");
        }
        return list;
    }

    /**
     *  绑定ImageView显示内容
     *
     * @param iv
     * @param url
     */
    protected void bindImage(final LifeCycleImageView iv, final String url) {
        if (!TextUtils.isEmpty(url)) {
            iv.setTag(url);
            iv.bindImageUrl(url);
        } else {
            iv.setImageDrawable(null);
        }
    }
}
