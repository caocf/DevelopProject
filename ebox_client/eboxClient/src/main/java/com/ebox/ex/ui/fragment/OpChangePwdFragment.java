package com.ebox.ex.ui.fragment;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspGetVerifySms;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.enums.SmsVerifyType;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.ex.network.model.req.ReqResetPassword;
import com.ebox.ex.network.request.PwdResetRequest;
import com.ebox.ex.network.request.RequestGetSms;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.Tip;

/**
 * Created by Android on 2015/10/22.
 */
public class OpChangePwdFragment extends BaseOpFragment implements View.OnClickListener, EboxKeyboard.OnEboxKeyListener, ResponseEventHandler<BaseRsp> {

    private EditText et_tel;
    private EditText et_verify;
    private EditText et_pwd;
    private EditText et_pwd_again;
    private Button bt_ok;
    private Button bt_get;
    private EboxKeyboard mKeyboard;
    private TextView tv_voice;
    private LinearLayout ll_voice;

    private DialogUtil dialogUtil;
    private TimeCount timeCount;
    private Tip tip;


    private View view;


    public static OpChangePwdFragment newInstance() {
        OpChangePwdFragment fragment = new OpChangePwdFragment();
        return fragment;
    }


    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_change_pwd;
    }


    @Override
    public void onResume() {
        super.onResume();
        et_tel.requestFocus();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogUtil.closeProgressDialog();

        if (tip != null) {
            tip.closeTip();
        }
    }

    @Override
    protected void initView(View view) {
        mKeyboard = (EboxKeyboard) view.findViewById(R.id.keyboard);

        //输入手机号
        et_tel = (EditText) view.findViewById(R.id.et_tel);
        et_tel.setOnTouchListener(touchListener);
        et_tel.requestFocus();
        mKeyboard.setEditText(et_tel);

        //输入验证码
        et_verify = (EditText) view.findViewById(R.id.et_verify);
        et_verify.setOnTouchListener(touchListener);

        //输入密码
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        et_pwd.setOnTouchListener(touchListener);

        //再次输入密码
        et_pwd_again = (EditText) view.findViewById(R.id.et_pwd_again);
        et_pwd_again.setOnTouchListener(touchListener);
        mKeyboard.setNumberAudio(false);


        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_get = (Button) view.findViewById(R.id.bt_get);
        tv_voice = (TextView) view.findViewById(R.id.tv_voice);
        tv_voice.setOnClickListener(this);
        ll_voice= (LinearLayout) view.findViewById(R.id.ex_ll_foc_voice);
        // 密码最多32位
        EditTextUtil.limitCount(et_tel, 11, null);
        EditTextUtil.limitCount(et_verify, SysValueDefine.MAX_SMS_VERIFY_LEN, null);
        EditTextUtil.limitCount(et_pwd, SysValueDefine.MAX_PASSWD_LEN, null);
        EditTextUtil.limitCount(et_pwd_again, SysValueDefine.MAX_PASSWD_LEN, null);
        bt_ok.setOnClickListener(this);
        bt_get.setOnClickListener(this);

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(getActivity());

        timeCount = new TimeCount(60000, 1000);
    }


    View.OnTouchListener touchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view instanceof EditText){
                mKeyboard.setEditText((EditText) view);
                mKeyboard.setMkeyListener(OpChangePwdFragment.this);
                mKeyboard.setNumberAudio(true);
            }
            return false;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确认提交
            case R.id.bt_ok:
                if (checkPara()) {
                    ResetPwd();
                }
                break;

            //获得验证码
            case R.id.bt_get: {
                if (et_tel.getText() == null || !FunctionUtil.matcherTel(et_tel.getText().toString().trim())) {
                    showPrompt(R.string.pub_telephone_error);
                    return;
                }
                timeCount.start();
                getVerify(RequestGetSms.TYPE_SMS);
                break;
            }

            //获得语音验证码
            case R.id.tv_voice:
                if (et_tel.getText() == null || !FunctionUtil.matcherTel(et_tel.getText().toString().trim())) {
                    showPrompt(R.string.pub_telephone_error);
                    return;
                }
                getVerify(RequestGetSms.TYPE_VOICE);
                timeCount.start();
                break;


        }
    }

    private void showPrompt(int resId) {
        tip = new Tip(getActivity(),
                getResources().getString(resId),
                null);
        tip.show(0);
    }

    private void showPrompt(String msg) {
        tip = new Tip(getActivity(),
                msg,
                null);
        tip.show(0);
    }

    private boolean checkPara() {
        if (et_tel.getText() == null ||
                et_tel.getText().toString().length() == 0 ||
                et_tel.getText().toString().length() != 11) {
            showPrompt(R.string.pub_telephone_error);
            return false;
        }

        if (et_verify.getText() == null ||
                et_verify.getText().toString().length() == 0 ||
                et_verify.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.ex_verify_error);
            return false;
        }

        if (et_pwd.getText() == null ||
                et_pwd.getText().toString().length() == 0 ||
                et_pwd.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.ex_input_new_pwd);
            return false;
        }

        if (et_pwd_again.getText() == null ||
                et_pwd_again.getText().toString().length() == 0 ||
                et_pwd_again.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.ex_input_new_pwd_again);
            return false;
        }

        if (!et_pwd.getText().toString().equals(et_pwd_again.getText().toString())) {
            showPrompt(R.string.ex_pwd_not_same);
            return false;
        }

        return true;
    }

    //确认重新设置密码
    private void ResetPwd() {
        ReqResetPassword req = new ReqResetPassword();

        req.setusername(et_tel.getText().toString());
        req.setPassword(et_pwd.getText().toString());
        req.setVerify(et_verify.getText().toString());

        PwdResetRequest reset = new PwdResetRequest(req, new ResponseEventHandler<BaseRsp>() {
            @Override
            public void onResponseSuccess(BaseRsp result) {
                dialogUtil.closeProgressDialog();

                if (result.isSuccess()) {

                    tip = new Tip(getActivity(),
                            getResources().getString(R.string.ex_change_success),
                            new Tip.onDismissListener() {
                                @Override
                                public void onDismiss() {
//                                    finish();
                                }
                            });
                    tip.show(0);
                } else {
                    showPrompt(result.getMsg());
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                timeCount.cancel();
                dialogUtil.closeProgressDialog();
                showPrompt(R.string.pub_connect_failed);
            }
        });
        RequestManager.addRequest(reset, null);
        dialogUtil.showProgressDialog();
    }

    //获得验证码
    private void getVerify(int type) {

        RspGetVerifySms req = new RspGetVerifySms();
        req.setTelephone(et_tel.getText().toString());
        req.setType(SmsVerifyType.op_forget_pwd);

        RequestGetSms request = new RequestGetSms(req, this,type);
        RequestManager.addRequest(request, null);
        dialogUtil.showProgressDialog();

    }

    @Override
    public void onResponseSuccess(BaseRsp result) {
        dialogUtil.closeProgressDialog();
        if (result.isSuccess()) {
            new Tip(getActivity(),
                    getResources().getString(R.string.ex_get_verify_success),
                    null).show(0);
        } else {

            timeCount.cancel();
            timeCount.onFinish();
            new Tip(getActivity(), result.getMsg(), null).show(0);
        }
    }

    @Override
    public void onResponseError(VolleyError error) {
        dialogUtil.closeProgressDialog();
        timeCount.cancel();
        timeCount.onFinish();
        new Tip(getActivity(), getResources().getString(R.string.pub_connect_failed), null).show(0);
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
//            bt_get.setBackgroundResource(R.drawable.ex_code_blue);
            ll_voice.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            bt_get.setClickable(false);
//            bt_get.setBackgroundResource(R.drawable.ex_code_blue);
            bt_get.setText(millisUntilFinished / 1000 + "秒");
        }
    }


}
