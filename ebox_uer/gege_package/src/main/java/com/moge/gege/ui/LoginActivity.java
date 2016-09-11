package com.moge.gege.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespAppInitModel;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetCheckCodeRequest;
import com.moge.gege.network.request.GetPayVoiceCodeRequest;
import com.moge.gege.network.request.GetVoiceCodeRequest;
import com.moge.gege.network.request.LoginRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LocationUtil;
import com.moge.gege.util.LogonUtils;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

public class LoginActivity extends BaseActivity
{
    private Context mContext;
    private String mPhoneNumber;
    private String mCheckCode;
    private int mRetry = 0;
    private int mRetryVoice = 0;

    private float mLatitude;
    private float mLongitude;

    private Button mCheckcodeBtn;
    private Button mLoginBtn;
    private EditText mPhoneEditText;
    private EditText mCheckCodeEditText;
    private CheckBox mAgreementCheckBox;
    private TextView mProtocolText;

    private static final int mTimeCount = 61;
    private static final int mVoiceTimeCount = 21;
    private int mCurrentTimeCount = mTimeCount;

    /**
     * external flag
     */
    private int mFrom = 0;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                if (mCurrentTimeCount == 0)
                {
                    mCheckcodeBtn.setEnabled(true);
                    mCheckcodeBtn.setText(R.string.retry_get_checkcode);
                    showVoiceCodeChoiceDialog();
                }
                else
                {
                    mCurrentTimeCount--;
                    mCheckcodeBtn.setText(String.valueOf(mCurrentTimeCount)
                            + "S");
                    postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mHandler.sendEmptyMessage(0);
                        }
                    }, 1000);

                }
            } else {

                if (mCurrentTimeCount == 0) {
                    mCheckcodeBtn.setEnabled(true);
                    mCheckcodeBtn.setText(R.string.retry_get_checkcode);
                } else {
                    mCurrentTimeCount--;
                    mCheckcodeBtn.setText(String.valueOf(mCurrentTimeCount)
                            + "S");
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }, 1000);
                }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;
        initView();

        mFrom = this.getIntent()
                .getIntExtra("from", LoginFromType.FROM_GENERAL);

        mLatitude = (float) LocationUtil.instance().getLatitude();
        mLongitude = (float) LocationUtil.instance().getLongitude();
    }

    @Override
    protected void initView()
    {
        mCheckcodeBtn = (Button) this.findViewById(R.id.checkcodeBtn);
        mCheckcodeBtn.setOnClickListener(mClickListener);

        mLoginBtn = (Button) this.findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(mClickListener);

        mPhoneEditText = (EditText) this.findViewById(R.id.phoneEdit);
        mCheckCodeEditText = (EditText) this.findViewById(R.id.checkcodeEdit);
        mAgreementCheckBox = (CheckBox) this
                .findViewById(R.id.agreementCheckBox);
        mAgreementCheckBox.setText(Html
                .fromHtml(getString(R.string.agree_protocol)));

        mProtocolText = (TextView) this.findViewById(R.id.protocolText);
        mProtocolText.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.checkcodeBtn:
                    onCheckCodeAction();
                    break;
                case R.id.loginBtn:
                    onLoginAction();
                    break;
                case R.id.protocolText:
                    UIHelper.showWebPageActivity(mContext,
                            NetworkConfig.protocolUrl);
                    break;
                default:
                    break;
            }
        }
    };

    private boolean checkInputValid(boolean onlyCheckMobile)
    {
        String phone = mPhoneEditText.getText().toString().trim();
        String checkcode = mCheckCodeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phone))
        {
            ToastUtil.showToastShort(R.string.mobile_empty);
            mPhoneEditText.setFocusable(true);
            mPhoneEditText.requestFocus();
            return false;
        }

        if (!LogonUtils.matcherLogonId(phone))
        {
            ToastUtil.showToastShort(R.string.mobile_format_error);
            mPhoneEditText.setFocusable(true);
            mPhoneEditText.requestFocus();
            return false;
        }

        mPhoneNumber = phone;
        if (onlyCheckMobile)
        {
            return true;
        }

        if (TextUtils.isEmpty(checkcode))
        {
            ToastUtil.showToastShort(R.string.checkcode_empty);
            mCheckCodeEditText.setFocusable(true);
            mCheckCodeEditText.requestFocus();
            return false;
        }

        mCheckCode = checkcode;
        return true;
    }

    private void onCheckCodeAction()
    {
        if (!checkInputValid(true))
        {
            return;
        }

        mCheckcodeBtn.setEnabled(false);

        GetCheckCodeRequest request = new GetCheckCodeRequest(mPhoneNumber,
                mRetry, new ResponseEventHandler<RespAppInitModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespAppInitModel> request,
                            RespAppInitModel result)
                    {
                        mRetry++;

                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil
                                    .showToastShort(R.string.send_checkcode_success);

                            // update button
                            mCurrentTimeCount = mTimeCount;
                            mCheckcodeBtn.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mHandler.sendEmptyMessage(0);
                                }
                            }, 1000);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            mCheckcodeBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRetry++;
                        mCheckcodeBtn.setEnabled(true);
                    }

                });
        executeRequest(request);
    }

    private void showVoiceCodeChoiceDialog() {
        CustomDialog dialog = new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.try_voice_code_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                doVoiceCodeRequest(mPhoneNumber);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void doVoiceCodeRequest(String phoneNumber) {

        mCheckcodeBtn.setEnabled(false);

        GetVoiceCodeRequest request = new GetVoiceCodeRequest(phoneNumber, mRetryVoice,
                new ResponseEventHandler<BaseModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request,
                            BaseModel result) {

                        mRetryVoice++;

                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS) {

                            ToastUtil
                                    .showToastShort(R.string.send_voice_code_success);

                            // update button
                            mCurrentTimeCount = mVoiceTimeCount;
                            mCheckcodeBtn.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                }
                            }, 1000);
                        } else {
                            ToastUtil.showToastShort(result.getMsg());
                            mCheckcodeBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                        mRetryVoice++;
                        mCheckcodeBtn.setEnabled(true);
                    }

                });

        RequestManager.addRequest(request, mContext);
    }

    private void onLoginAction()
    {
        if (!checkInputValid(false))
        {
            return;
        }

        LoginRequest request = new LoginRequest(mPhoneNumber, mCheckCode,
                mLatitude, mLongitude,
                new ResponseEventHandler<RespUserModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserModel> request,
                            RespUserModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            // save user information
                            AppApplication.login(result.getData());

                            exitActivity();
                        }
                        else if (result.getStatus() == ErrorCode.USER_NOT_REGISTER)
                        {
                            Intent intent = new Intent(mContext,
                                    RegisterActivity.class);
                            intent.putExtra("username", mPhoneNumber);
                            startActivityForResult(intent,
                                    GlobalConfig.INTENT_NEW_USERINFO);

                            setLoginButtonState(true);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            setLoginButtonState(true);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        setLoginButtonState(true);
                    }

                });
        RequestManager.addRequest(request, RequestManager.CookieTag);

        setLoginButtonState(false);
    }

    private void exitActivity()
    {
        Intent intent = new Intent();
        intent.putExtra("from", mFrom);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setLoginButtonState(boolean isEnable)
    {
        mLoginBtn.setEnabled(isEnable);
        mLoginBtn.setText(isEnable ? getString(R.string.login)
                : getString(R.string.login_ing));
    }

    private long firstClickBackTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (mFrom == LoginFromType.FROM_APP_START
                    || mFrom == LoginFromType.FROM_QUIT_LOGIN)
            {
                long secondClickBackTime = System.currentTimeMillis();
                if (secondClickBackTime - firstClickBackTime > 2000)
                {
                    ToastUtil.showToastShort(R.string.repeat_quit_confirm);
                    firstClickBackTime = secondClickBackTime;
                }
                else
                {
                    AppApplication.instance().exit();
                }
                return true;
            }

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            case GlobalConfig.INTENT_NEW_USERINFO:
                exitActivity();
                break;
            default:
                break;
        }
    }

    @Override
    protected  void onDestroy()
    {
        super.onDestroy();

        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
