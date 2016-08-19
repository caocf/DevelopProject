package com.xhl.bqlh.business.view.ui.callback;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Sum on 16/4/15.
 */
public interface ProductAnimListener {
    /**
     * 商品加载完成动画
     */
    void loadFinishAnim();

    /**
     * 上滚动画
     */
    void scrollUpAnim();

    /**
     * 下滚动画
     */
    void scrollDownAnim();

    /**
     * 添加商品动画
     */
    void startAnim(View view, Drawable drawable);
}
