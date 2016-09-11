package com.moge.gege.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.MessageModel;
import com.moge.gege.model.PushChatSendResultModel;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.request.SigninRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.InputStreamAt;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.EmptyView;
import com.moge.gege.util.*;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressLint("InlinedApi")
public abstract class BaseActivity extends FragmentActivity
{
    /**
     * log TAG.
     */
    private final String TAG = "activity";

    /**
     * current activity name.
     */
    private final String ACTIVITY_NAME = getClass().getSimpleName();
    /**
     * application handler.
     */
    protected Handler mHandler;

    // general header view
    private TextView mHeaderTitleText;
    private TextView mHeaderLeftText;
    private TextView mHeaderRightText;
    private LinearLayout mHeaderRightOptionLayout;
    private ImageView mHeaderRightOptionImage;

    // photo&album
    Uri mImageUri;
    private Dialog mProgressDialog;

    // loading view
    private EmptyView mLoadingView;

    // signin request
    SigninRequest mSignRequest;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG, String.format("onCreate %s", ACTIVITY_NAME));

        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(GlobalConfig.BROADCAST_ACTION_FINISH);
        registerReceiver(mExitReceiver, exitFilter);

    }

    private BroadcastReceiver mExitReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(GlobalConfig.BROADCAST_ACTION_FINISH))
            {
                finish();
            }
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        LogUtil.i(TAG, String.format("onStart %s", ACTIVITY_NAME));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtil.i(TAG, String.format("onResume %s", ACTIVITY_NAME));
        UmengUtil.statDurationStart(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        LogUtil.i(TAG, String.format("onPause %s", ACTIVITY_NAME));
        UmengUtil.statDurationEnd(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStop()
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        LogUtil.i(TAG, String.format("onStop %s", ACTIVITY_NAME));

        RequestManager.cancelAll(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        releaseView(getTopView());

        unregisterReceiver(mExitReceiver);

        LogUtil.i(TAG, String.format("onDestroy %s", ACTIVITY_NAME));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        LogUtil.i(TAG, String.format("onRestart %s", ACTIVITY_NAME));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent
     * )
     */
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        LogUtil.i(TAG, String.format("onNewIntent %s", ACTIVITY_NAME));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        LogUtil.i(TAG, String.format("finalize %s", ACTIVITY_NAME));
        super.finalize();
    }

    /**
     * release view resource.
     *
     * @param paramView the param view
     */
    private void releaseView(View paramView)
    {
        if (paramView != null)
        {
            paramView.setBackgroundDrawable(null);

            if (paramView instanceof ViewGroup)
            {
                ((ViewGroup) paramView).removeAllViews();
            }
        }
    }

    /**
     * handle message.
     *
     * @param msg the msg
     */
    public void handleMessage(Message msg)
    {

    }

    /**
     * be used to release view resource, proposal to return the top view of
     * child activity.
     *
     * @return the top view
     */
    public View getTopView()
    {
        return null;
    }

    protected boolean executeRequest(Request<?> request)
    {
        return RequestManager.addRequest(request, this);
    }

    public EmptyView getLoadingView()
    {
        return mLoadingView;
    }

    public void showLoadingView()
    {
        if (mLoadingView != null)
        {
            mLoadingView.showLoadingView();
        }
    }

    public void showLoadEmptyView()
    {
        if (mLoadingView != null)
        {
            mLoadingView.showEmptyResultView();
        }
    }

    public void showLoadEmptyView(String msg)
    {
        if (mLoadingView != null)
        {
            mLoadingView.showEmptyResultView(msg);
        }
    }

    public void showLoadEmptyView(int resId)
    {
        if (mLoadingView != null)
        {
            mLoadingView.showEmptyResultView(resId);
        }
    }

    public void showLoadFailView(String msg)
    {
        if (mLoadingView != null)
        {
            if (TextUtils.isEmpty(msg))
            {
                mLoadingView.showFailResultView();
            }
            else
            {
                mLoadingView.showFailResultView(msg);
            }
        }
    }

    public void showLoadFailView(int resId)
    {
        if (mLoadingView != null)
        {
            mLoadingView.showFailResultView(resId);
        }
    }

    public void hideLoadingView()
    {
        if (mLoadingView != null)
        {
            mLoadingView.hideView();
        }
    }

    protected void initView()
    {
        RelativeLayout headerLayout = (RelativeLayout) this
                .findViewById(R.id.generalHeaderLayout);
        if (headerLayout != null)
        {
            headerLayout.setOnTouchListener(new OnDoubleClickListener());
        }

        mHeaderTitleText = (TextView) this.findViewById(R.id.headerTitleText);
        mHeaderLeftText = (TextView) this.findViewById(R.id.headerLeftText);
        if (mHeaderLeftText != null)
        {
            mHeaderLeftText.setOnClickListener(mClickListener);
        }

        mHeaderRightText = (TextView) this.findViewById(R.id.headerRightText);
        if (mHeaderRightText != null)
        {
            mHeaderRightText.setOnClickListener(mClickListener);
        }

        mHeaderRightOptionImage = (ImageView) this
                .findViewById(R.id.headerRightOptionImage);
        mHeaderRightOptionLayout = (LinearLayout) this
                .findViewById(R.id.headerRightOptionLayout);
        if (mHeaderRightOptionLayout != null)
        {
            mHeaderRightOptionLayout.setOnClickListener(mClickListener);
        }

        // loading view
        mLoadingView = (EmptyView) this.findViewById(R.id.loadingView);
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.headerLeftText:
                    onHeaderLeftClick();
                    break;
                case R.id.headerRightText:
                    onHeaderRightClick();
                    break;
                case R.id.headerRightOptionLayout:
                    onHeaderRightOptionClick();
                    break;
                default:
                    break;
            }
        }
    };

    private long firstClick = 0;
    private long secondClick = 0;
    private int count = 0;

    class OnDoubleClickListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (MotionEvent.ACTION_DOWN == event.getAction())
            {
                count++;
                if (count == 1)
                {
                    firstClick = System.currentTimeMillis();

                }
                else if (count == 2)
                {
                    secondClick = System.currentTimeMillis();
                    if (secondClick - firstClick < 350)
                    {
                        // 双击事件
                        onHeaderDoubleClick();
                    }
                    count = 0;
                    firstClick = 0;
                    secondClick = 0;
                }
            }
            return true;
        }

    }

    protected void onHeaderDoubleClick()
    {

    }

    protected void onHeaderRightClick()
    {

    }

    protected void onHeaderRightOptionClick()
    {

    }

    protected void onHeaderLeftClick()
    {
        this.finish();
    }

    /**
     * set header title
     */
    public void setHeaderTitle(int resId)
    {
        setHeaderTitle(getString(resId));
    }

    public void setHeaderTitle(String title)
    {
        if (mHeaderTitleText != null)
        {
            mHeaderTitleText.setText(title);
        }
    }

    /**
     * set header right button title
     */
    public void setHeaderRightTitle(int resId)
    {
        setHeaderRightTitle(getString(resId));
    }

    public void setHeaderLeftTitle(int resId)
    {
        setHeaderLeftTitle(getString(resId));
    }

    public void setHeaderLeftTitle(String title)
    {
        if (mHeaderLeftText != null)
        {
            mHeaderLeftText.setText(title);
        }
    }

    public void setHeaderRightTitle(String title)
    {
        if (mHeaderRightText != null)
        {
            mHeaderRightText.setVisibility(View.VISIBLE);
            mHeaderRightText.setText(title);
        }
    }

    public void setHeaderLeftImage(int resId)
    {
        if (mHeaderLeftText != null)
        {
            Drawable drawable = getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mHeaderLeftText.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setHeaderRightImage(int resId)
    {
        if (mHeaderRightText != null)
        {
            mHeaderRightText.setVisibility(View.VISIBLE);

            Drawable drawable = getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mHeaderRightText.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setHeaderRightOptionImage(int resId)
    {
        if (mHeaderRightOptionImage != null)
        {
            mHeaderRightOptionLayout.setVisibility(View.VISIBLE);
            mHeaderRightOptionImage.setImageResource(resId);
        }
    }

    protected void setImage(final ImageView imageView, final String imageUrl,
            final int loadingImageId)
    {
        RequestManager.loadImage(imageView, imageUrl, loadingImageId);
    }

    public String getImageUrl(String imageName)
    {
        return RequestManager.getImageUrl(imageName);
    }

    public void onRecvChatMsg(List<MessageModel> msgList)
    {

    }

    public void onRecvChatResultMsg(PushChatSendResultModel resultModel)
    {

    }

    public void openCamera()
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(EnviromentUtil.getImageDirectory(this),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        mImageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(openCameraIntent,
                GlobalConfig.INTENT_TAKE_CAMERA);
    }

    public void openAlbum()
    {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        else
        {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, GlobalConfig.INTENT_SELECT_IMAGE);

        // to do list!!!
        // Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // openAlbumIntent.setDataAndType(
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // startActivityForResult(openAlbumIntent,
        // GlobalConfig.INTENT_SELECT_IMAGE);
    }

    public void onTakeCameraResult(Uri imageUri)
    {
        uploadPhoto();
    }

    public void onTakeAlbumResult(Uri imageUri)
    {
        uploadPhoto();
    }

    private void uploadPhoto()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = DialogUtil.createProgressDialog(this,
                    getString(R.string.uploading_photo));
        }

        mProgressDialog.show();
        doGetUploadToken();
    }

    public void onUploadPhotoSuccess(String imagename)
    {

    }

    public void onUploadPhotoFailed(String errorMsg)
    {

    }

    public Uri getImageUri()
    {
        return mImageUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_TAKE_CAMERA:
                onTakeCameraResult(mImageUri);
                break;
            case GlobalConfig.INTENT_SELECT_IMAGE:
                mImageUri = Uri.parse("file://"
                        + FileUtil.getPath(this, data.getData()));
                onTakeAlbumResult(mImageUri);
                break;
            case GlobalConfig.INTENT_LOGIN:
                if (data != null)
                {
                    int from = data
                            .getIntExtra("from", LoginFromType.FROM_GENERAL);
                    onLoginResult(from, ErrorCode.SUCCESS);
                }
                break;
            default:
                break;
        }

        /** 使用SSO授权必须添加如下代码 */
        UMSocialService controller = UMServiceFactory
                .getUMSocialService("myshare");
        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null)
        {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void doGetUploadToken()
    {
        GetUploadTokenRequest request = new GetUploadTokenRequest(
                new ResponseEventHandler<RespUploadTokenModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUploadTokenModel> request,
                            RespUploadTokenModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            String uploadToken = result.getData().getToken();
                            doUploadPhoto(getImageUri(), uploadToken);
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        mProgressDialog.dismiss();
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doUploadPhoto(Uri imageUri, String upToken)
    {
        String key = IO.UNDEFINED_KEY;
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<String, String>();
        extra.params.put("x:source", "headImage");
        extra.mimeType = "image/jpeg";

        String filepath = FileUtil.getPath(this, imageUri);

        InputStreamAt imageStream = new InputStreamAt(
                BitmapUtil.compressImage(filepath));

        IO.put(upToken, key, imageStream, extra, new JSONObjectRet()
                // IO.putFile(this, upToken, key, imageUri, extra, new JSONObjectRet()
        {
            @Override
            public void onProcess(long current, long total)
            {
            }

            @Override
            public void onSuccess(JSONObject resp)
            {
                try
                {
                    if (resp.has("success"))
                    {
                        if (resp.getBoolean("success"))
                        {
                            onUploadPhotoSuccess(resp.getString("name"));
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    onUploadPhotoFailed(e.getMessage());
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(QiniuException ex)
            {
                mProgressDialog.dismiss();
                LogUtil.e("upload avatar failed");
                onUploadPhotoFailed(ex.getMessage());
            }
        });
    }

    private ResponseEventHandler<RespUserModel> mSigninResultListener = new ResponseEventHandler<RespUserModel>()
    {
        @Override
        public void onResponseSucess(
                BaseRequest<RespUserModel> request,
                RespUserModel result)
        {
            SigninRequest signReq = (SigninRequest) request;

            if (result.getStatus() == ErrorCode.SUCCESS)
            {
                AppApplication.login(result.getData());

                onLoginResult(signReq.from, ErrorCode.SUCCESS);
            }
            else
            {
                ToastUtil.showToastShort(result.getMsg());
                onLoginResult(signReq.from, ErrorCode.GENERAL_ERROR);

                if(signReq.from != LoginFromType.FROM_APP_START)
                {
                    UIHelper.showLoginActivity(signReq.context, signReq.from);
                }
            }
        }

        @Override
        public void onResponseError(VolleyError error)
        {
            ToastUtil.showToastShort(error.getMessage());
            onLoginResult(mSignRequest.from, ErrorCode.GENERAL_ERROR);

            if(mSignRequest.from != LoginFromType.FROM_APP_START)
            {
                UIHelper.showLoginActivity(mSignRequest.context, mSignRequest.from);
            }
        }
    };

    public void doSignin(Activity context)
    {
        doSignin(context, LoginFromType.FROM_GENERAL);
    }

    public void doSignin(Activity context, int from)
    {
        if (mSignRequest == null)
        {
            mSignRequest = new SigninRequest(GlobalConfig.getLongitude(),
                    GlobalConfig.getLatitude(), mSigninResultListener
            );
        }

        mSignRequest.setParams(context, from);
        RequestManager.addRequest(mSignRequest, RequestManager.CookieTag);
    }

    protected void onLoginResult(int from, int result)
    {
        if (result == ErrorCode.SUCCESS)
        {
            AppApplication.setLoginState(true);
        }
        else
        {
            AppApplication.setLoginState(false);
        }

    }
}
