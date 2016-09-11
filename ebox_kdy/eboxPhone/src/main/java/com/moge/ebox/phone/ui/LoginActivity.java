package com.moge.ebox.phone.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.model.UserInfo;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.utils.EditTextUtil;
import com.moge.ebox.phone.utils.EditTextUtil.inputFinishListener;
import com.moge.ebox.phone.utils.LogonUtils;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tools.AppException;

public class LoginActivity extends BaseActivity {
    private Context mContext;
    private EboxApplication mAppContext;
    private String mPhoneNumber;
    private String mPwd;

    private Button mBtnForget, mBtnRegister, mBtnLogin;
    private EditText mPhoneEditText, mPwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAppContext = (EboxApplication) getApplication();
        mContext = LoginActivity.this;
        initView();
    }

    private void initView() {
        mBtnForget = (Button) findViewById(R.id.btn_foget_pwd);
        mBtnForget.setOnClickListener(mClickListener);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(mClickListener);
        mBtnLogin = (Button) findViewById(R.id.loginBtn);
        mBtnLogin.setOnClickListener(mClickListener);
        mPhoneEditText = (EditText) findViewById(R.id.phoneEdit);
        EditTextUtil.limitCount(mPhoneEditText, 11, new inputFinishListener() {
            @Override
            public void onInputFinish() {
                mPwdEditText.requestFocus();
            }
        });
        mPwdEditText = (EditText) findViewById(R.id.checkcodeEdit);
        EditTextUtil.limitCount(mPwdEditText, 32, null);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginBtn:
                    onLoginAction();
                    break;
                case R.id.btn_foget_pwd:
                    onFogetAction();
                    break;
                case R.id.btn_register:
                    onRegisterAction();
                    break;
                default:
                    break;
            }
        }
    };

    private void onRegisterAction() {
        Intent intent = new Intent(mContext, RegisterActivity.class);
        startActivityForResult(intent, GlobalConfig.INTENT_REGISTER);
//        Intent intent=new Intent(mContext,RegisterPreActivity.class);
//        startActivity(intent);
    }

    private void onFogetAction() {
        Intent intent = new Intent(mContext, FrogetPwdActivity.class);
        startActivityForResult(intent, GlobalConfig.INTENT_FORGET_PWD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GlobalConfig.INTENT_REGISTER:
                String id = data.getStringExtra(GlobalConfig.FLAG_TEL);
                if (id != "" && id != null) {
                    mPhoneEditText.setText(id);
                }
                break;
            case GlobalConfig.INTENT_FORGET_PWD:
                String phone = data.getStringExtra(GlobalConfig.FLAG_TEL);
                if (phone != "" && phone != null) {
                    mPhoneEditText.setText(phone);
                }
                break;
        }

    }

    private boolean checkInputValid() {
        try {
            String phone = mPhoneEditText.getText().toString().trim();
            String checkcode = mPwdEditText.getText().toString().trim();

            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToastShort(R.string.mobile_empty);
                mPhoneEditText.setFocusable(true);
                mPhoneEditText.requestFocus();
                return false;
            }
            if (!LogonUtils.isMobileNO(phone)) {
                ToastUtil.showToastShort(R.string.mobile_format_error);
                mPhoneEditText.setFocusable(true);
                mPhoneEditText.requestFocus();
                return false;
            }

            mPhoneNumber = phone;

            if (TextUtils.isEmpty(checkcode)) {
                ToastUtil.showToastShort(R.string.pwd_empty);
                mPwdEditText.setFocusable(true);
                mPwdEditText.requestFocus();
                return false;
            }

//			mPwd = MD5Util.getMD5String(checkcode);
            mPwd = checkcode;
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewUtils.closeInput(this, mPhoneEditText);
        ViewUtils.closeInput(this, mPwdEditText);
        return super.dispatchTouchEvent(ev);
    }


    private ProgressDialog progress;
    private void onLoginAction() {
        if (checkInputValid()) {
            progress = UIHelper.showProgress(mContext, "",
                    "登陆中...");
            Map requestParams = new HashMap();
            requestParams.put("username", mPhoneNumber);
            requestParams.put("password", mPwd);
//			requestParams.put("cid", PushManager.getInstance().getClientid(mAppContext));
            /**
             * 用户登录
             */
            ApiClient.login(mAppContext, requestParams, new ClientCallback() {

                @Override
                public void onSuccess(JSONArray data, int code) {
                    UIHelper.dismissProgress(progress);
                    Logger.i("LoginActivity:  success" + data.toString());
                    save(data);
                }

                @Override
                public void onFailed(byte[] data, int code) {
                    UIHelper.dismissProgress(progress);
                    Logger.i("LoginActivity:  failed" + data.toString());

                    //服务端返回代码编号为101表示需要完善资料
                    if (code ==30112) {
                        UserInfo userInfo = new UserInfo();
                        String phone = mPhoneEditText.getText().toString().trim();
                        userInfo.telephone = phone;
                        getoPrePage(userInfo);
                    } /*else {
                        String msg = "";
                        try {
                            JSONObject json = new JSONObject(data.toString());
                            msg = json.getString("message");

                        } catch (Exception e){

                        }
                        if (msg !=null && msg.equals("")){
                            Tip tip = new Tip(LoginActivity.this,msg ,null);
                            tip.show();
                        }


                    }*/
                }
            });
            return;
        }
    }

    protected void save(JSONArray data) {
        if (data.length() > 0) {
            try {
                JSONObject jsonObject = data.getJSONObject(0).getJSONObject("user");
                UserInfo userInfo = new UserInfo();
                userInfo = userInfo.parse(jsonObject.toString());
                Logger.i("LoginActivity: UserInfo:" + userInfo.create_at + "  " + userInfo.app_status);

                if (userInfo != null) {
                    Logger.i("CheckInfo  save  ok");
                    checkUserInfo(userInfo);
                }else {
                    Logger.i("CheckInfo  save");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (AppException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private void checkUserInfo(UserInfo userInfo) {
            Logger.i("CheckInfo:  ok");
            EboxApplication.getInstance().saveLoginInfo(userInfo);
            gotoHomePage();

    }

    private void doAimogeLogin(UserInfo userInfo) {
        HashMap params=new HashMap();
        try {
            /**
             * 获得登录aimoge支付登录状态
             */
            ApiClient.getAimogeLoginState(EboxApplication.getInstance(), params, new ClientCallback() {
                @Override
                public void onSuccess(JSONArray data, int code) {
                    Logger.i("LoginActivity: aimoge登录成功" + data.toString());
                    //登录成功  再去获得支付单
                    UIHelper.dismissProgress(progress);
                    gotoHomePage();
                }

                @Override
                public void onFailed(byte[] data, int code) {

                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void getoPrePage(UserInfo userInfo) {
        Intent intent = new Intent(mContext, RegisterPreActivity.class);
        intent.putExtra("tag", RegisterPreActivity.tag_perfect);
        intent.putExtra("tel", userInfo.telephone);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    private void gotoHomePage() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
    }

    // 处理按钮点击事件派发
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();// 记录点击时间
                ToastUtil.showToastShort("再次点击退出程序");
            } else {
                EboxApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
