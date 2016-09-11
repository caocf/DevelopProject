package com.moge.ebox.phone.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.LogonUtils;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {
    private Context mContext;
    private Head mHead;
    private Button mbtnRegister, mbtnGetCheck;
    private EditText medPhone, medPwd, medPwdAgain, medCheckCode;
    private static final int TAG_TIME = 1;
    private static final int TAG_GET = 2;
    private static final int VERIFY_OK=3;
    private static final int VERIFY_ERROR=4;
    private String verifyNumber = "";
    private Handler mHandler;
    private boolean stop = false;
    private boolean isUnRegister = true;
    private boolean isTimeOut = false;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == TAG_TIME) {
                    isTimeOut = false;
                    int time = msg.arg1;
                    mbtnGetCheck.setText(time + " s");
                    mbtnGetCheck.setEnabled(false);
                } else if (msg.what == TAG_GET) {
                    isTimeOut = true;
                    mbtnGetCheck.setEnabled(true);
                    mbtnGetCheck.setText(getResources().getString(
                            R.string.retry_get_checkcode));
                }else if (msg.what==VERIFY_OK){
                    //验证码获取成功
                    Intent intent = new Intent(mContext, RegisterPreActivity.class);
                    intent.putExtra("verify", getText(medCheckCode));
                    intent.putExtra("tel", getText(medPhone));
                    intent.putExtra("password", getText(medPwd));
                    intent.putExtra("tag", RegisterPreActivity.tag_register);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }else if (msg.what==VERIFY_ERROR){
                    //验证码获取失败
//                    ToastUtil.showToastShort("验证码不正确");
                }
            }
        };
        initView();
    }

    private void initView() {
        medPhone = findviewById_(R.id.phoneEdit);
        medPwd = findviewById_(R.id.ed_pwd);
        medCheckCode = findviewById_(R.id.ed_check_code);
        medPwdAgain = findviewById_(R.id.ed_pwd_again);
        mbtnGetCheck = findviewById_(R.id.btn_get_check);
        mbtnGetCheck.setOnClickListener(mClickListener);
        mbtnRegister = findviewById_(R.id.btn_register);
        mbtnRegister.setOnClickListener(mClickListener);
        //medCheckCode.addTextChangedListener(mTextWatcher);
        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.title);
        HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.register_create);
        mHead.setData(data, this);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    onRegisterAction();
                    break;
                case R.id.btn_get_check:
                    onGetCheckAction();
                    break;
                default:
                    break;
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence text, int start, int before,
                                  int count) {
            // text 输入框中改变后的字符串信息
            // start 输入框中改变后的字符串的起始位置
            // before 输入框中改变前的字符串的位置 默认为0
            // count 输入框中改变后的一共输入字符串的数量

            if (verifyNumber.equals(text.toString())) {
                stop = true;
                mbtnGetCheck.setEnabled(true);
                mbtnGetCheck.setText(getResources().getString(
                        R.string.retry_get_checkcode));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence text, int start, int count,
                                      int after) {
            // text 输入框中改变前的字符串信息
            // start 输入框中改变前的字符串的起始位置
            // count 输入框中改变前后的字符串改变数量一般为0
            // after 输入框中改变后的字符串与起始位置的偏移量

        }

        @Override
        public void afterTextChanged(Editable edit) {
            // edit 输入结束呈现在输入框中的信息

        }
    };

    /**
     * 下一步注册时候  需要验证码验证码是否填写正确
     */
    private void onRegisterAction() {

        String phone = getText(medPhone);
        String code = getText(medCheckCode);
        if (checkInput(phone,code)) {
            checkInputVerify(phone,code);
        }
    }

    protected void onGetCheckAction() {

        String phone = medPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !LogonUtils.isMobileNO(phone)) {
            ToastUtil.showToastShort("请输入正确手机号");
            return;
        }
        getCheckCode();
    }

    private Thread thread;
    private int time;
    private String phone;


    private void getCheckCode() {
        medCheckCode.requestFocus();
        phone = medPhone.getText().toString().trim();
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                time = 120;
                while (time != 0) {
                    if (!stop) {
                        time--;
                        Message msg = Message.obtain();
                        msg.what = TAG_TIME;
                        msg.arg1 = time;
                        mHandler.sendMessage(msg);
                        SystemClock.sleep(1000);
                    } else {
                        time = 0;
                        return;
                    }
                }
                if (time == 0) {
                    mHandler.sendEmptyMessage(TAG_GET);
                }
            }
        });
        thread.start();
        getCheckCode(phone);
    }

    /**
     * 获取验证码接口
     * @param phone
     */
    private void getCheckCode(String phone) {
        Map params = new HashMap();
        params.put("username", phone);
        params.put("type", "4");// 注册：4，忘记密码：5
//        params.put("action","register");
        ApiClient.getVerifySms(EboxApplication.getInstance(), params,
                new ClientCallback() {

                    @Override
                    public void onSuccess(JSONArray data, int code) {

                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                        time = 0;
                        isUnRegister = false;
                        ToastUtil.showToastShort("验证码获取失败");

                    }
                });
    }

    /**
     * 验证码验证接口
     *
     * @return
     */
    private void checkInputVerify(String phone,String code) {
        Map params = new HashMap();
        params.put("username", phone);
        params.put("type", 4);
        params.put("verify", code);
        ApiClient.checkVerify(EboxApplication.getInstance(), params, new ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                //验证成功
                Logger.i("----->验证码正确");
                mHandler.sendEmptyMessage(VERIFY_OK);
            }

            @Override
            public void onFailed(byte[] data, int code) {
                //验证失败
                mHandler.sendEmptyMessage(VERIFY_ERROR);
            }
        });
    }

    private boolean checkInput(String phone,String code) {

        if (TextUtils.isEmpty(phone) || !LogonUtils.isMobileNO(phone)) {
            ToastUtil.showToastShort("请输入正确手机号");
            return false;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToastShort("请输入验证码");
            return false;
        }

        if (isTimeOut) {
            ToastUtil.showToastShort("验证码已失效,请重新获取");
            return false;
        }
        if (!isUnRegister) {
            ToastUtil.showToastShort("该号码已经注册,请重新输入");
            return false;
        }

        String pwd = getText(medPwd);
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToastShort("请输入密码");
            medPwd.requestFocus();
            return false;
        }
        String pwdAgain = getText(medPwdAgain);
        if (TextUtils.isEmpty(pwdAgain)) {
            ToastUtil.showToastShort("请再次输入密码");
            medPwdAgain.requestFocus();
            return false;
        }
        if (!pwd.equals(pwdAgain)) {
            ToastUtil.showToastShort("两次密码输入不一致");
            return false;
        }
        return true;
    }


    private String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewUtils.closeInput(this, medPhone);
        ViewUtils.closeInput(this, medPwd);
        ViewUtils.closeInput(this, medPwdAgain);
        ViewUtils.closeInput(this, medCheckCode);
        return super.dispatchTouchEvent(ev);
    }
}
