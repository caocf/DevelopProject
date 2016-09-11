package com.moge.gege.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.LocalServiceModel;
import com.moge.gege.model.ShareParam;
import com.moge.gege.model.enums.ShareType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TopicDeleteRequest;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.request.TopicReportRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ShareAdapter;
import com.moge.gege.ui.adapter.ShareAdapter.ShareListener;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareDialog extends Dialog implements ShareListener,
        OnClickListener
{
    private Context mContext;
    private HListView mShareListView;
    private ShareAdapter mShareAdapter;
    private HListView mOtherListView;
    private ShareAdapter mOtherAdapter;
    private TextView mCancelText;

    private ShareDialogListener mListener;
    private ShareParam mParam;
    final UMSocialService mShareController = UMServiceFactory
            .getUMSocialService("myshare");

    private String mCopyUrl = "";

    public interface ShareDialogListener
    {
        public void onDeleteTopicResult(int result);
    }

    public ShareDialog(Context context, ShareParam param)
    {
        super(context, R.style.CustomDialog_Share);

        this.mContext = context;
        this.mParam = param;

        initSocialShare();
    }

    public void setListener(ShareDialogListener listener)
    {
        mListener = listener;
    }

    public void setParam(ShareParam param)
    {
        this.mParam = param;
        initSocialShare();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout menuLayout = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.custom_share_dialog, null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(menuLayout);
        setCanceledOnTouchOutside(true);

        initView();
        initData();

        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow
                .getAttributes();
        localLayoutParams.x = 0;
        localLayoutParams.y = -1000;
        localLayoutParams.gravity = Gravity.BOTTOM;
        localWindow.setAttributes(localLayoutParams);

        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    private void initView()
    {
        mShareListView = (HListView) findViewById(R.id.shareListView);
        mShareAdapter = new ShareAdapter(mContext);
        mShareAdapter.setListener(this);
        mShareListView.setAdapter(mShareAdapter);
        mShareListView.setOnItemClickListener(mShareAdapter);

        mOtherListView = (HListView) findViewById(R.id.otherListView);
        mOtherAdapter = new ShareAdapter(mContext);
        mOtherAdapter.setListener(this);
        mOtherListView.setAdapter(mOtherAdapter);
        mOtherListView.setOnItemClickListener(mOtherAdapter);

        mCancelText = (TextView) findViewById(R.id.cancelText);
        mCancelText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    private void initData()
    {
        final String nameList[] = mContext.getResources().getStringArray(
                R.array.share_name);
        final String resNameList[] = mContext.getResources().getStringArray(
                R.array.share_res_name);

        List<LocalServiceModel> shareList = new ArrayList<LocalServiceModel>();

        for (int i = 0; i < nameList.length; i++)
        {
            LocalServiceModel model = new LocalServiceModel();
            model.setName(nameList[i]);
            model.setResId(mContext.getResources().getIdentifier(
                    resNameList[i], "drawable", mContext.getPackageName()));
            shareList.add(model);
        }

        mShareAdapter.clear();
        mShareAdapter.addAll(shareList);
        mShareAdapter.notifyDataSetChanged();

        List<LocalServiceModel> otherList = new ArrayList<LocalServiceModel>();

        LocalServiceModel favorite = new LocalServiceModel();
        favorite.setName(mContext.getResources().getString(R.string.favorite));
        favorite.setResId(R.drawable.icon_share_favorite);
        favorite.setValue(ShareType.FAVORITE);
        otherList.add(favorite);

        LocalServiceModel copyUrl = new LocalServiceModel();
        copyUrl.setName(mContext.getResources().getString(R.string.copy_url));
        copyUrl.setResId(R.drawable.icon_share_copyurl);
        copyUrl.setValue(ShareType.COPY_URL);
        otherList.add(copyUrl);

        LocalServiceModel report = new LocalServiceModel();
        report.setName(mContext.getResources().getString(R.string.report));
        report.setResId(R.drawable.icon_share_report);
        report.setValue(ShareType.REPORT);
        otherList.add(report);

        if (mParam != null && mParam.isShowDelete())
        {
            LocalServiceModel deleteTopic = new LocalServiceModel();
            if (mParam.getTopicType() == TopicType.BUSINESS_TOPIC)
            {
                deleteTopic.setName(mContext.getResources().getString(
                        R.string.delete_goods));
            }
            else
            {
                deleteTopic.setName(mContext.getResources().getString(
                        R.string.delete_topic2));
            }
            deleteTopic.setResId(R.drawable.icon_delete_topic);
            deleteTopic.setValue(ShareType.DELETE);
            otherList.add(deleteTopic);
        }

        mOtherAdapter.clear();
        mOtherAdapter.addAll(otherList);
        mOtherAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShareItemClick(int position, LocalServiceModel model)
    {
        switch (model.getValue())
        {
            case ShareType.FAVORITE:
                if (mParam != null)
                {
                    if(!AppApplication.checkLoginState((Activity) mContext))
                    {
                        return;
                    }

                    doTopicFavorite(mParam.getTopicType(), mParam.getTopicId(),
                            mParam.getBoardId());
                }
                break;
            case ShareType.COPY_URL:
                copyToClipboard(mContext, mCopyUrl);
                ToastUtil.showToastShort(R.string.copy_to_clipboard_success);
                break;
            case ShareType.REPORT:
                if (mParam != null)
                {
                    if(!AppApplication.checkLoginState((Activity) mContext))
                    {
                        return;
                    }

                    doTopicReport(mParam.getTopicType(), mParam.getTopicId(),
                            mParam.getBoardId());
                }
                break;
            case ShareType.DELETE:
                if (mParam != null)
                {
                    if(!AppApplication.checkLoginState((Activity) mContext))
                    {
                        return;
                    }

                    onTopicDelete();
                }
                break;
            default:
                shareTopicContent(position);
                break;
        }

        if (model.getValue() != ShareType.DELETE)
        {
            this.dismiss();
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        this.dismiss();
    }

    private void initSocialShare()
    {
        if (mParam == null)
        {
            return;
        }

        // to do list!!!
        com.umeng.socialize.utils.Log.LOG = true;
        SocializeConstants.SHOW_ERROR_CODE = true;

        UMImage urlImage = null;
        if (!TextUtils.isEmpty(mParam.getImageUrl()))
        {
            urlImage = new UMImage(mContext, PersistentData.instance()
                    .getImageAddress()
                    + RequestManager.getImageUrl(mParam.getImageUrl())
                    + GlobalConfig.IMAGE_STYLE90_90);
        }
        else
        {
            urlImage = new UMImage(mContext, R.drawable.icon_share_default);
        }

        String targetUrl = "";

        if (mParam.getTopicType() == TopicType.GENERAL_TOPIC
                || mParam.getTopicType() == TopicType.ACTIVITY_TOPIC)
        {
            targetUrl = NetworkConfig.mobileAddress + "/topic/"
                    + mParam.getTopicId();
        }
        else if (mParam.getTopicType() == TopicType.BUSINESS_TOPIC)
        {
            targetUrl = NetworkConfig.mobileAddress + "/trading/"
                    + mParam.getTopicId() + "?promotion_id=" + mParam.getPromotionId();
        }
        else
        {
            targetUrl = NetworkConfig.mobileAddress + "/living/service/"
                    + mParam.getTopicId();
        }

        mCopyUrl = targetUrl;

        // sina
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(mParam.getContent() + targetUrl);
        // sinaContent.setTitle(mParam.getTitle());
        // sinaContent.setTargetUrl(targetUrl);
        sinaContent.setShareImage(urlImage);
        mShareController.setShareMedia(sinaContent);

        // sina sso
        mShareController.getConfig().setSsoHandler(new SinaSsoHandler());

        // weixin
        UMWXHandler wxHandler = new UMWXHandler(mContext, GlobalConfig.WXAppID,
                GlobalConfig.WXAppSecret);
        wxHandler.addToSocialSDK();

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(mParam.getContent());
        weixinContent.setTitle(mParam.getTitle());
        weixinContent.setShareImage(urlImage);
        weixinContent.setTargetUrl(targetUrl);
        mShareController.setShareMedia(weixinContent);

        // wexin friend
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext,
                GlobalConfig.WXAppID, GlobalConfig.WXAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(mParam.getContent());
        circleMedia.setTitle(mParam.getTitle());
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(targetUrl);
        mShareController.setShareMedia(circleMedia);

        // qzone
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                (Activity) mContext, GlobalConfig.QQAppID,
                GlobalConfig.QQAppSecret);
        qZoneSsoHandler.addToSocialSDK();

        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(mParam.getContent());
        qzone.setTitle(mParam.getTitle());
        qzone.setShareImage(urlImage);
        qzone.setTargetUrl(targetUrl);
        mShareController.setShareMedia(qzone);

        // qq
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext,
                GlobalConfig.QQAppID, GlobalConfig.QQAppSecret);
        qqSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(mParam.getContent());
        qqShareContent.setTitle(mParam.getTitle());
        qqShareContent.setShareImage(urlImage);
        qqShareContent.setTargetUrl(targetUrl);
        mShareController.setShareMedia(qqShareContent);

        // mShareController.openShare((Activity) mContext, false);
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

    private void shareTopicContent(int type)
    {
        if (mParam == null)
        {
            return;
        }

        switch (type)
        {
            case ShareType.SINA_WEIBO:
                mShareController.directShare(mContext, SHARE_MEDIA.SINA,
                        mSnsPostListener);
                break;
            case ShareType.WEICHAT:
                mShareController.directShare(mContext, SHARE_MEDIA.WEIXIN,
                        mSnsPostListener);
                break;
            case ShareType.WEICHAT_FRIEND_CIRCLE:
                mShareController.directShare(mContext,
                        SHARE_MEDIA.WEIXIN_CIRCLE, mSnsPostListener);
                break;
            case ShareType.QQ_QZONE:
                mShareController.directShare(mContext, SHARE_MEDIA.QZONE,
                        mSnsPostListener);
                break;
            case ShareType.QQ:
                mShareController.directShare(mContext, SHARE_MEDIA.QQ,
                        mSnsPostListener);
                break;
            default:
                break;
        }
    }

    private void copyToClipboard(Context context, String content)
    {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    private void onTopicDelete()
    {
        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.delete_topic_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                dialog.dismiss();

                                doTopicDeleteRequest(mParam.getTopicType(),
                                        mParam.getTopicId(),
                                        mParam.getBoardId());
                            }

                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    private void doTopicFavorite(int topicType, String topicId, String boardId)
    {
        TopicFavoriteRequest request = new TopicFavoriteRequest(topicType,
                topicId, boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(mContext.getResources()
                                    .getString(R.string.favorite_success));
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doTopicReport(int topicType, String topicId, String boardId)
    {
        TopicReportRequest request = new TopicReportRequest(topicType, topicId,
                boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(mContext.getResources()
                                    .getString(R.string.report_success));
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doTopicDeleteRequest(int topicType, String topicId,
            String boardId)
    {
        TopicDeleteRequest request = new TopicDeleteRequest(topicType, topicId,
                boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(mContext.getResources()
                                    .getString(R.string.delete_success));
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                        if (mListener != null)
                        {
                            mListener.onDeleteTopicResult(result.getStatus());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

}
