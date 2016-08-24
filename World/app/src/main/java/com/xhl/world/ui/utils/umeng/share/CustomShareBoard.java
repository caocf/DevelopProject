package com.xhl.world.ui.utils.umeng.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.model.user.UserInfo;


public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private Activity mActivity;
    private ShareParam mParam;
    private ShareCallBack mCallBack;

    public CustomShareBoard(Activity activity, ShareParam param) {
        super(activity);
        this.mParam = param;
        this.mActivity = activity;
        initView(activity);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.sina).setOnClickListener(this);
        rootView.findViewById(R.id.more).setOnClickListener(this);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        showPopupWindow();

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });
    }


    private void showPopupWindow() {
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;

        mActivity.getWindow().setAttributes(params);
    }

    private void closePopupWindow() {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.wechat) {
            performShare(SHARE_MEDIA.WEIXIN);

        } else if (id == R.id.wechat_circle) {
            performShare(SHARE_MEDIA.WEIXIN_CIRCLE);

        } else if (id == R.id.qq) {
            performShare(SHARE_MEDIA.QQ);

        } else if (id == R.id.qzone) {
            performShare(SHARE_MEDIA.QZONE);

        } else if (id == R.id.sina) {
//            performShare(SHARE_MEDIA.SINA);
        } else {

            if (mParam != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mParam.getContent() + " 链接:" + mParam.getTargetUrl());
                sendIntent.setType("text/plain");
                mActivity.startActivity(sendIntent);
            }
        }
    }

    private void performShare(SHARE_MEDIA share) {

        if (mParam == null) {
            return;
        }
        UMImage urlImage;
        if (TextUtils.isEmpty(mParam.getImageUrl())) {
            Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.icon_logo);
            urlImage = new UMImage(mActivity, bitmap);
        } else {
            urlImage = new UMImage(mActivity, mParam.getImageUrl());
        }

        String content = mParam.getContent();
        String targetUrl = mParam.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            if (AppApplication.appContext.isLogin()) {
                UserInfo info = AppApplication.appContext.getLoginUserInfo();
                String id = info.id;
                String type = info.type;
                if (!TextUtils.isEmpty(id) && type.equals("6")) {
                    targetUrl += "&userId=" + id;
                }
            }
        }
        String title = mParam.getTitle();
        //设置分享参数
        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(share);
        shareAction.withMedia(urlImage);
        shareAction.withTargetUrl(targetUrl);
        shareAction.withTitle(title);
        shareAction.withText(content);
        shareAction.share();

        this.dismiss();
    }


    public void setCallBack(ShareCallBack callBack) {
        this.mCallBack = callBack;
    }
}
