package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspGetVerifySms;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.enums.SmsVerifyType;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.ex.network.model.req.ReqRegister;
import com.ebox.ex.network.request.RegisterRequest;
import com.ebox.ex.network.request.RequestGetSms;
import com.ebox.ex.network.request.RequestGetSmsVerity;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.HashMap;

/**
 * 快递员注册
 */
public class RegisterActivity extends CommonActivity implements OnClickListener, EboxKeyboard.OnEboxKeyListener, ResponseEventHandler<BaseRsp> {
    private EditText et_tel;
    private EditText et_verify;
    private EditText et_pwd;
    private Button bt_ok;
    private Button bt_get;
    private EboxKeyboard mKeyboard;
    private TextView tv_city;
    private LinearLayout ll_voice;
    private TextView tv_voice;

    private DialogUtil dialogUtil;
    private TimeCount timeCount;
    private Tip tip;

    public final static int REQUEST_CODE = 100;

    private String telephone;
    private String password;
    private String verify;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_register);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtil.closeProgressDialog();
        if (tip != null) {
            tip.closeTip();
        }
    }

    private void initView() {
        mKeyboard = (EboxKeyboard) findViewById(R.id.keyboard);


        //输入手机号
        et_tel = (EditText) findViewById(R.id.et_operator_phone);
        et_tel.requestFocus();
        et_tel.setOnTouchListener(touchListener);
        mKeyboard.setEditText(et_tel);

        //输入验证码
        et_verify = (EditText) findViewById(R.id.et_operator_verify);
        et_verify.setOnTouchListener(touchListener);

        //输入密码
        et_pwd = (EditText) findViewById(R.id.et_operator_password);
        et_pwd.setOnTouchListener(touchListener);
        mKeyboard.setNumberAudio(false);

        //选择快递公司
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setOnClickListener(this);


        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_get = (Button) findViewById(R.id.bt_get);
        bt_ok.setOnClickListener(this);
        bt_get.setOnClickListener(this);

        ll_voice = (LinearLayout) findViewById(R.id.ex_ll_ar_voice);
        tv_voice = (TextView) findViewById(R.id.ex_tv_ar_voice);
        tv_voice.setOnClickListener(this);


        // 密码最多32位
        EditTextUtil.limitCount(et_tel, 11, null);
        EditTextUtil.limitCount(et_verify, SysValueDefine.MAX_SMS_VERIFY_LEN, null);
        EditTextUtil.limitCount(et_pwd, SysValueDefine.MAX_PASSWD_LEN, null);


        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this);

        timeCount = new TimeCount(60000, 1000);
        initTitle();
    }

    View.OnTouchListener touchListener=new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view instanceof EditText){
                mKeyboard.setEditText((EditText) view);
                mKeyboard.setMkeyListener(RegisterActivity.this);
                mKeyboard.setNumberAudio(true);
            }
            return false;
        }
    };


    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.ex_register);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                if (checkPara()) {
                    //先执行验证验证码接口
                    verifySms();
                }
                break;
            case R.id.bt_get: {
                String telStr = et_tel.getText().toString().trim();
                if (et_tel.getText() == null || !FunctionUtil.matcherTel(telStr)) {
                    showPrompt(R.string.pub_telephone_error);
                    return;
                } else {
                    getVerify(RequestGetSms.TYPE_SMS);
                    timeCount.start();
                }
                break;
            }

            case R.id.ex_tv_ar_voice:
                String telStr = et_tel.getText().toString().trim();
                if (et_tel.getText() == null || !FunctionUtil.matcherTel(telStr)) {
                    showPrompt(R.string.pub_telephone_error);
                    return;
                } else {
//                    showPrompt(R.string.pub_voice_send_ok);
                    getVerify(RequestGetSms.TYPE_VOICE);
                    timeCount.start();
                }
                break;

            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_city:
                Intent intent = new Intent(RegisterActivity.this, RegisteNextActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    /**
     * 提交注册信息
     *
     * @return
     */
    private boolean verifySms() {
        HashMap params = new HashMap();
        params.put("username", telephone);
        params.put("type", 4);
        params.put("verify", verify);
        dialogUtil.showProgressDialog();
        RequestGetSmsVerity re = new RequestGetSmsVerity(params, new ResponseEventHandler<BaseRsp>() {
            @Override
            public void onResponseSuccess(BaseRsp result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    LogUtil.i("验证成功，执行注册接口");
                    //验证成功，执行注册接口
                    TSRegister();
                } else {
                    showPrompt(result.getMsg());
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                showPrompt(R.string.pub_connect_failed);
                LogUtil.e(error.getMessage());
            }
        });

        executeRequest(re);


        return false;
    }

    private long orgnizationId;
    private String org_name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //保存快递公司信息
            orgnizationId = data.getLongExtra("id", -1);
            String org_name = data.getStringExtra("name");
            tv_city.setText(org_name);


        }
    }

    public void TSRegister() {
        ReqRegister req = new ReqRegister();

        req.setUsername(telephone);
        req.setPassword(password);
        req.setVerify(verify);
        if (orgnizationId != -1) {
            //设置快递公司的ID
            req.setOrg_id(orgnizationId);
            dialogUtil.showProgressDialog();
            register(req);
        } else {
            showPrompt(R.string.ex_i_orgselect);
        }
    }

    private void register(ReqRegister req) {

        RegisterRequest registerRequest = new RegisterRequest(req, new ResponseEventHandler<BaseRsp>() {
            @Override
            public void onResponseSuccess(BaseRsp result) {
                dialogUtil.closeProgressDialog();

                if (result.isSuccess()) {
                    //注册成功则跳转到注册成功界面
                    Intent intent = new Intent(RegisterActivity.this, RegisterOkActivity.class);
                    startActivity(intent);
                } else {
                    showPrompt(result.getMsg());
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                tip = new Tip(RegisterActivity.this,
                        getResources().getString(R.string.pub_connect_failed), null);
                tip.show(0);
            }
        });

        dialogUtil.showProgressDialog();
        executeRequest(registerRequest);

    }


    private void showPrompt(String msg) {
        tip = new Tip(RegisterActivity.this,
                msg,
                null);
        tip.show(0);
    }


    private void showPrompt(int resId) {
        tip = new Tip(RegisterActivity.this,
                getResources().getString(resId),
                null);
        tip.show(0);
    }

    /**
     * 参数测试
     *
     * @return
     */

    private boolean checkPara() {
        telephone = et_tel.getText().toString().trim();
        if (et_tel.getText() == null || !FunctionUtil.matcherTel(telephone)) {
            showPrompt(R.string.pub_telephone_error);
            return false;
        }


        verify = et_verify.getText().toString();
        if (et_verify.getText() == null ||
                verify.length() == 0 ||
                verify.length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.ex_verify_error);
            return false;
        }

        password = et_pwd.getText().toString();
        if (et_pwd.getText() == null ||
                password.length() == 0 ||
                password.length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.ex_input_new_pwd);
            return false;
        }


        name = et_tel.getText().toString();

        return true;
    }

    /**
     * 获得短信验证码
     */
    private void getVerify(int type) {
        RspGetVerifySms req = new RspGetVerifySms();
        req.setTelephone(et_tel.getText().toString());
        req.setType(SmsVerifyType.op_regedit);

        RequestGetSms request = new RequestGetSms(req, this, type);
        executeRequest(request);
        dialogUtil.showProgressDialog();
    }


    @Override
    public void onResponseSuccess(BaseRsp result) {
        dialogUtil.closeProgressDialog();
        if (result.isSuccess()) {
            new Tip(RegisterActivity.this,
                    getResources().getString(R.string.ex_get_verify_success),
                    null).show(0);
        } else {
            timeCount.cancel();
            timeCount.onFinish();
            showPrompt(result.getMsg());
        }

    }

    @Override
    public void onResponseError(VolleyError error) {
        dialogUtil.closeProgressDialog();
        timeCount.cancel();
        timeCount.onFinish();
        new Tip(this, getResources().getString(R.string.pub_connect_failed), null).show(0);
    }

    @Override
    public void onKey(int keyCode) {

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            bt_get.setText(getResources().getString(R.string.ex_get_verify));
            bt_get.setClickable(true);
//            bt_get.setBackgroundResource(R.drawable.pub_code_btn_white);
            ll_voice.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            bt_get.setClickable(false);
//            bt_get.setBackgroundResource(R.drawable.pub_code_btn_white);
            bt_get.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
