package com.moge.gege.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.WriterException;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.FunctionUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class InviteFriendFragment extends BaseFragment implements
        OnClickListener
{
    public Context mContext;

    private LinearLayout mSinaWeiboLayout;
    private LinearLayout mWeixinLayout;
    private LinearLayout mWeixinCycleLayout;
    private ImageView mCodeImage;

    private final UMSocialService mShareController = UMServiceFactory
            .getUMSocialService("myshare");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_invitefriend,
                container, false);
        initView(layout);
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();
        this.setHeaderLeft(R.string.invite_friend, R.drawable.icon_back);

        mSinaWeiboLayout = (LinearLayout) v.findViewById(R.id.sinaWeiboLayout);
        mSinaWeiboLayout.setOnClickListener(this);
        mWeixinLayout = (LinearLayout) v.findViewById(R.id.weixinLayout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinCycleLayout = (LinearLayout) v
                .findViewById(R.id.weixinCycleLayout);
        mWeixinCycleLayout.setOnClickListener(this);
        mCodeImage = (ImageView) v.findViewById(R.id.codeImage);
        new Get2DCodeTask().execute();

        initSocialShare();
    }

    private void initSocialShare()
    {
        String title = getString(R.string.app_name);
        String content = getString(R.string.app_introduction);
        UMImage urlImage = new UMImage(mContext, R.drawable.icon_share_default);
        String targetUrl = NetworkConfig.appDownloadUrl;

        // sina
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(title + "-" + content + targetUrl);
        sinaContent.setShareImage(urlImage);
        // sinaContent.setTitle(title);
        // sinaContent.setTargetUrl(targetUrl);
        mShareController.setShareMedia(sinaContent);

        // sina sso
        mShareController.getConfig().setSsoHandler(new SinaSsoHandler());

        // weixin
        UMWXHandler wxHandler = new UMWXHandler(mContext, GlobalConfig.WXAppID,
                GlobalConfig.WXAppSecret);
        wxHandler.addToSocialSDK();

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setShareImage(urlImage);
        weixinContent.setTargetUrl(targetUrl);
        mShareController.setShareMedia(weixinContent);

        // wexin friend
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext,
                GlobalConfig.WXAppID, GlobalConfig.WXAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(targetUrl);
        mShareController.setShareMedia(circleMedia);

        // mShareController.openShare((Activity) mContext, false);
    }

    @Override
    protected void onHeaderLeftClick()
    {
        getActivity().finish();
    }

    private class Get2DCodeTask extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params)
        {
            try
            {
                return FunctionUtils.Create2DCode(NetworkConfig.appDownloadUrl,
                        FunctionUtil.dip2px(mContext, 240),
                        FunctionUtil.dip2px(mContext, 240));
            }
            catch (WriterException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            if (null != result)
            {
                mCodeImage.setImageBitmap(result);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sinaWeiboLayout:
                mShareController.postShare(mContext, SHARE_MEDIA.SINA,
                        mSnsPostListener);
                break;
            case R.id.weixinLayout:
                mShareController.postShare(mContext, SHARE_MEDIA.WEIXIN,
                        mSnsPostListener);
                break;
            case R.id.weixinCycleLayout:
                mShareController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
                        mSnsPostListener);
                break;
            default:
                break;
        }
    }

    private SnsPostListener mSnsPostListener = new SnsPostListener()
    {
        @Override
        public void onStart()
        {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode,
                SocializeEntity entity)
        {
            // to do list!!!
            // if (eCode == 200)
            // {
            // ToastUtil.showToastShort("分享成功~");
            // }
            // else
            // {
            // String eMsg = "";
            // if (eCode == -101)
            // {
            // eMsg = "没有授权";
            // }
            // ToastUtil.showToastShort("分享失败[" + eCode + "] " + eMsg);
            // }
        }
    };

}
