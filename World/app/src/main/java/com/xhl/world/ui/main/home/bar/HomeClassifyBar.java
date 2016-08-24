package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.config.Constant;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.Event;

/**
 * Created by Sum on 15/12/2.
 */
public class HomeClassifyBar extends BaseHomeBar {

    @Event(value = {R.id.r1, R.id.r2, R.id.r3, R.id.r4, R.id.r5, R.id.r6, R.id.r7, R.id.r8}, type = RippleView.OnRippleCompleteListener.class)
    private void classifyClick(View view) {
        switch (view.getId()) {

            case R.id.r1:
            case R.id.r2:
            case R.id.r5:
            case R.id.r6:
            case R.id.r7:
            case R.id.r8:
                Snackbar.make(view, R.string.fun_building, Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.r3:
                if (AppApplication.appContext.isLogin(getContext())) {
                    Intent intent = new Intent(mContext, WebPageActivity.class);
                    intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_sign);
                    intent.putExtra(WebPageActivity.TAG_TITLE, mContext.getString(R.string.home_classify_qd));
                    intent.putExtra(WebPageActivity.TAG_QUIT, false);
                    getContext().startActivity(intent);
                }

                break;
            case R.id.r4:

                Intent intent = new Intent(mContext, WebPageActivity.class);
                String shareUrl = NetWorkConfig.web_app_index + "?couponId=all";

                intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_coupon_take);

                intent.putExtra(WebPageActivity.TAG_TITLE, mContext.getString(R.string.home_classify_yhq));
                //分享的链接
                intent.putExtra(WebPageActivity.TAG_SHARE_URL, shareUrl);
                //按钮样式
                intent.putExtra(WebPageActivity.right_button_style, WebPageActivity.rightButtonStyleShare);

                getContext().startActivity(intent);

                break;
        }

    }

    public HomeClassifyBar(Context context) {
        super(context);
    }

    public HomeClassifyBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_classify;
    }
}
