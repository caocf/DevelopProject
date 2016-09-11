package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.SigninRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.EmptyView;
import com.moge.gege.util.FileUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.UmengUtil;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import de.greenrobot.event.EventBus;
import butterknife.ButterKnife;

public class BaseFragment extends Fragment
{
    /**
     * log TAG.
     */
    private final String TAG = "fragment";
    /**
     * fragment name.
     */
    private final String FRAGMENT_NAME = getClass().getSimpleName();

    // general header view
    private RelativeLayout mHeaderLayout;
    private TextView mHeaderTitleText;
    private TextView mHeaderLeftText;
    private TextView mHeaderRightText;
    private LinearLayout mHeaderRightOptionLayout;
    private ImageView mHeaderRightOptionImage;

    // loading view
    private EmptyView mLoadingView;

    // signin request
    SigninRequest mSignRequest;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i(TAG, String.format("onActivityCreated %s", FRAGMENT_NAME));
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        LogUtil.i(TAG, String.format("onCreate %s", FRAGMENT_NAME));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        LogUtil.i(TAG, String.format("onDestroy %s", FRAGMENT_NAME));
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.reset(this);
        LogUtil.i(TAG, String.format("onDestroyView %s", FRAGMENT_NAME));
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        LogUtil.i(TAG, String.format("onDetach %s", FRAGMENT_NAME));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LogUtil.i(TAG, String.format("onPause %s", FRAGMENT_NAME));
        UmengUtil.statPageEnd(FRAGMENT_NAME);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LogUtil.i(TAG, String.format("onResume %s", FRAGMENT_NAME));
        UmengUtil.statPageStart(FRAGMENT_NAME);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        LogUtil.i(TAG, String.format("onStart %s", FRAGMENT_NAME));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        LogUtil.i(TAG, String.format("onStop %s", FRAGMENT_NAME));

        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request<?> request)
    {
        RequestManager.addRequest(request, this);
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

    private View mView = null;

    protected void initView(View v)
    {
        mHeaderLayout = (RelativeLayout) v
                .findViewById(R.id.generalHeaderLayout);
        if (mHeaderLayout != null)
        {
            mHeaderLayout.setOnTouchListener(new OnDoubleClickListener());
        }

        mHeaderTitleText = (TextView) v.findViewById(R.id.headerTitleText);
        mHeaderLeftText = (TextView) v.findViewById(R.id.headerLeftText);
        if (mHeaderLeftText != null)
        {
            mHeaderLeftText.setOnClickListener(mClickListener);
        }

        mHeaderRightText = (TextView) v.findViewById(R.id.headerRightText);
        if (mHeaderRightText != null)
        {
            mHeaderRightText.setOnClickListener(mClickListener);
        }

        mHeaderRightOptionImage = (ImageView) v
                .findViewById(R.id.headerRightOptionImage);
        mHeaderRightOptionLayout = (LinearLayout) v
                .findViewById(R.id.headerRightOptionLayout);
        if (mHeaderRightOptionLayout != null)
        {
            mHeaderRightOptionLayout.setOnClickListener(mClickListener);
        }

        mView = v;

        // loading view
        mLoadingView = (EmptyView) v.findViewById(R.id.loadingView);
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
                    onHeaderRightClick(v);
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

    protected void onHeaderRightClick(View v)
    {

    }

    protected void onHeaderRightOptionClick()
    {

    }

    protected void onHeaderLeftClick()
    {

    }

    public void setHeaderBackground(int color)
    {
        if(mHeaderLayout != null) {
            mHeaderLayout.setBackgroundColor(color);
        }
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
        if (mView == null)
        {
            return;
        }

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

    public void setHeaderRightTitle(String title)
    {
        if (mView == null)
        {
            return;
        }

        if (mHeaderRightText != null)
        {
            mHeaderRightText.setVisibility(View.VISIBLE);
            mHeaderRightText.setText(title);
        }
    }

    public void setHeaderLeft(int textResId, int imageResId)
    {
        if (mView == null)
        {
            return;
        }

        if (mHeaderLeftText != null)
        {
            mHeaderLeftText.setText(getString(textResId));

            Drawable drawable = getResources().getDrawable(imageResId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mHeaderLeftText.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setHeaderLeftImage(int resId)
    {
        if (mView == null)
        {
            return;
        }

        if (mHeaderLeftText != null)
        {
            Drawable drawable = getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mHeaderLeftText.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setHeaderLeftBackgroundImage(int resId)
    {
        if (mView == null)
        {
            return;
        }

        if (mHeaderLeftText != null)
        {
            mHeaderLeftText.setBackgroundResource(resId);
        }
    }

    public void setHeaderRightImage(int resId)
    {
        if (mView == null)
        {
            return;
        }

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
                AppApplication.setLoginState(true);
                PersistentData.instance().setUserInfo(
                        result.getData());

                onLoginResult(signReq.from, ErrorCode.SUCCESS);
            }
            else if (result.getStatus() == ErrorCode.USER_NOT_LOGIN
                    || result.getStatus() == ErrorCode.USER_NOT_FOUND)
            {
                // TODO list, 是否要到直接跳到首页
                UIHelper.showLoginActivity(signReq.context, signReq.from);
            }
            else
            {
                ToastUtil.showToastShort(result.getMsg());
                onLoginResult(signReq.from, ErrorCode.GENERAL_ERROR);
            }
        }

        @Override
        public void onResponseError(VolleyError error)
        {
            ToastUtil.showToastShort(error.getMessage());
            onLoginResult(mSignRequest.from, ErrorCode.GENERAL_ERROR);
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
        if(result == ErrorCode.SUCCESS)
        {
            AppApplication.setLoginState(true);
        }
        else
        {
            AppApplication.setLoginState(false);
        }
    }

    // post custom event
    public void onEvent(Object params)
    {

    }
}