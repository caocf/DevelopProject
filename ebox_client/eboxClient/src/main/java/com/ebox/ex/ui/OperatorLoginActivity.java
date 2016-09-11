package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.database.operator.OperatorTable;
import com.ebox.ex.network.model.RspLoginOperator;
import com.ebox.ex.network.model.base.type.OperatorType;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.OperatorStatus;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.mgt.ui.SuperLoginActivity;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.TimeUtil;
import com.ebox.pub.utils.Tip;

/**
 * 快递员登录界面
 */
public class OperatorLoginActivity extends CommonActivity implements
        OnClickListener,EboxKeyboard.OnEboxKeyListener,ResponseEventHandler<RspLoginOperator> {
    private static final String TAG = "OperatorLoginActivity";
    private EditText ed_operator_phone;
    private EditText ed_operator_pwd;
    private Button bt_ok;
    private Button bt_forget;
    private Button bt_register;
    private DialogUtil dialogUtil;
    private EboxKeyboard mKeyboard;
    private TextView tv_big,tv_medium,tv_samll,tv_tiny;
    private Tip tip;
    private int loginType;// 0:离线登陆 1:网络登陆
    public TextView tv_timer;
    public int clickTimes = 0;
    private TextView tv_error_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_operator_login);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();

        loginType=1;
//
//        if (Constants.DEBUG) {
//            ed_operator_phone.setText("15656263709");
//            ed_operator_pwd.setText("123456");
//        }
        updateBoxCount();

    }

    private void updateBoxCount()
    {
        int large = BoxInfoHelper.getBoxCountBySize(BoxSizeType.large);
        tv_big.setText(getString(R.string.ex_login_box_big,large));
        if (large==0) {
            tv_big.setTextColor(getResources().getColor(R.color.ex_text_disable));
        }

        int medium = BoxInfoHelper.getBoxCountBySize(BoxSizeType.medium);
        tv_medium.setText(getString(R.string.ex_login_box_medium, medium));
        if (medium==0) {
            tv_medium.setTextColor(getResources().getColor(R.color.ex_text_disable));
        }
        int small = BoxInfoHelper.getBoxCountBySize(BoxSizeType.small);
        tv_samll.setText(getString(R.string.ex_login_box_small,small));
        if (small==0) {
            tv_samll.setTextColor(getResources().getColor(R.color.ex_text_disable));
        }
        int tiny = BoxInfoHelper.getBoxCountBySize(BoxSizeType.tiny);
        tv_tiny.setText(getString(R.string.ex_login_box_tiny,tiny));
        if (tiny==0) {
            tv_tiny.setTextColor(getResources().getColor(R.color.ex_text_disable));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();

        ed_operator_phone.requestFocus();

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
        tv_big = (TextView) findViewById(R.id.tv_big);
        tv_medium = (TextView) findViewById(R.id.tv_medium);
        tv_samll = (TextView) findViewById(R.id.tv_small);
        tv_tiny = (TextView) findViewById(R.id.tv_tiny);
        tv_error_tip = (TextView) findViewById(R.id.tv_error_tip);
        tv_error_tip.setVisibility(View.INVISIBLE);

        mKeyboard = (EboxKeyboard) findViewById(R.id.keyboard);

        ed_operator_phone = (EditText) findViewById(R.id.ed_operator_phone);
        KeyboardUtil.hideInput(this, ed_operator_phone);

        ed_operator_phone.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKeyboard.setEditText((EditText) v);
                mKeyboard.setMkeyListener(OperatorLoginActivity.this);
                return false;
            }
        });

        ed_operator_pwd = (EditText) findViewById(R.id.ed_operator_pwd);
        KeyboardUtil.hideInput(this, ed_operator_pwd);

        ed_operator_pwd.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKeyboard.setEditText(ed_operator_pwd);
                mKeyboard.setMkeyListener(null);
                return false;
            }
        });
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_forget = (Button) findViewById(R.id.bt_forget);
        bt_register = (Button) findViewById(R.id.bt_register);

        // 用户名最多11位
        EditTextUtil.limitCount(ed_operator_phone, 11, null);
        // 密码最多32位
        EditTextUtil.limitCount(ed_operator_pwd, SysValueDefine.MAX_PASSWD_LEN, null);

        mKeyboard.setEditText(ed_operator_phone);
        mKeyboard.setNumberAudio(false);

        bt_ok.setOnClickListener(this);
        bt_forget.setOnClickListener(this);
        bt_register.setOnClickListener(this);

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_timer.setOnClickListener(this);

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this);

        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.ex_operator_login);
        data.tvVisibility = true;
        title.setData(data, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确认登录
            case R.id.bt_ok:
//                String systemOk = AppApplication.getInstance().isSystemOk();
//                if (!systemOk.equals("ok"))
//                {
//                    tip = new Tip(OperatorLoginActivity.this, systemOk, null);
//                    tip.show(0);
//                    return;
//                }

                if (checkPara())
                {
                    onLineOperator();
                }
                break;
            //忘记密码
            case R.id.bt_forget:
            {
                Intent intent = new Intent(OperatorLoginActivity.this,
                        ForgetPwdActivity.class);
                startActivity(intent);
                break;
            }

            //注册
            case R.id.bt_register:
            {
                Intent intent = new Intent(OperatorLoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_timer:
            {
                clickTimes++;
                if (clickTimes >= 5) {
                    Intent intent = new Intent(OperatorLoginActivity.this, SuperLoginActivity.class);
                    startActivity(intent);
                    clickTimes=0;
                }
                break;
            }
        }
    }

    private void showPrompt(int resId) {
        tip = new Tip(OperatorLoginActivity.this, getResources().getString(
                resId), null);
        tip.show(0);
    }

    private boolean checkPara() {
        if (ed_operator_phone.getText() == null
                || ed_operator_phone.getText().toString().length() == 0
                || ed_operator_phone.getText().toString().length() > SysValueDefine.MAX_OPERATOR_ID_LEN) {
            showPrompt(R.string.pub_username_error);
            return false;
        }

        if (ed_operator_pwd.getText() == null
                || ed_operator_pwd.getText().toString().length() == 0


                || ed_operator_pwd.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN) {
            showPrompt(R.string.pub_passwd_error);
            return false;
        }

        return true;
    }

    // 离线操作
    private void OperatorAfterOnline()
    {
        String operatorID = ed_operator_phone.getText().toString().trim();
        String operatorPwd = FunctionUtils.getMD5Str(ed_operator_pwd.getText().toString().trim());

        dialogUtil.closeProgressDialog();
        // 缓存用户信息
        OperatorInfo info = OperatorOp.getOperatorById(operatorID);

        if (info == null) {
            showPrompt(R.string.pub_connect_failed);
            return;
        }

        //删除锁定和未激活的数据
        if (info.getOperatorStatus() == OperatorStatus.Blocked
                || info.getOperatorStatus() == OperatorStatus.UnActivate) {
            OperatorOp.deleteOperatorById(operatorID);
            showPrompt(R.string.pub_connect_failed);
            return;
        }
        //密码错误
        if (!operatorPwd.equals(info.getPassword())) {
            LogUtil.i(TAG, operatorID + ",密码错误," + TimeUtil.currentTime());
            showPrompt(R.string.pub_passwd_error);
            return;
        }

        // 计费
        int isAccount = GlobalField.serverConfig.getIsAccount();

        if (info.getBalance() <= 0
                && isAccount == AcountType.is_acount)
        {
            LogUtil.i(TAG, operatorID + ",余额不足," + info.getBalance() + "," + TimeUtil.currentTime());
            showPrompt(R.string.pub_connect_failed);
            return;
        }
        // 账号密码正确，登陆成功
        Log.i(TAG, "offline login success");
        loginType = 0;
        startMainOperatorActivity(operatorID);
    }


    private void onLineOperator() {

        String phone = ed_operator_phone.getText().toString().trim();

        String pwd = ed_operator_pwd.getText().toString().trim();

        OperatorHelper.Login(phone, pwd, this);

        loginType = 1;
        dialogUtil.showProgressDialog();
    }

    public void resetMyself() {
        ed_operator_phone.setText("");
        ed_operator_pwd.setText("");
        ed_operator_phone.requestFocus();

        mKeyboard.setEditText(ed_operator_phone);
        mKeyboard.setMkeyListener(this);
    }

    private void startMainOperatorActivity(String operatorId) {
        resetMyself();
        Toast.makeText(AppApplication.globalContext, R.string.ex_login_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OperatorLoginActivity.this,OperatorHomeActivity.class);
        intent.putExtra("loginType", loginType);
        intent.putExtra("operatorId", operatorId);
        startActivity(intent);
        // 读取最新门状态
        BoxCtrlTask.readBoxStatus();
        this.finish();
    }

    /**
     * 保存一个本地缓存用户
     */
    private void saveOperatorInfo(OperatorType operator) {
        OperatorInfo info = new OperatorInfo();
        info.setOperatorId(operator.getUsername());
        info.setOperatorName(operator.getOperator_name());
        //modify by mf 20150316  改成密文MD5加密（用于快递员本地缓存密码更新）
        info.setPassword(FunctionUtils.getMD5Str(ed_operator_pwd.getText().toString().trim()));
        //end by mf
        info.setReserveStatus(operator.getReserve_status());//预留功能是否可用
        info.setOperatorStatus(operator.getStatus());// 用户的状态
        info.setBalance(operator.getBalance());
        info.setTelephone(operator.getTelephone());
        info.setState(OperatorTable.STATE_1);

        info.setCardId("");
        // 添加至本地表中
        OperatorOp.addOperator(info);
    }

    @Override
    public void onKey(int primaryCode) {

        if (ed_operator_phone.getText() != null
                && ed_operator_phone.getText().toString().length() >= 11)
        {
            ed_operator_pwd.requestFocus();
            mKeyboard.setEditText(ed_operator_pwd);
            mKeyboard.setMkeyListener(null);
            tv_error_tip.setVisibility(View.INVISIBLE);
        }else {

            tv_error_tip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseSuccess(RspLoginOperator result) {
        dialogUtil.closeProgressDialog();
        if (result!=null && result.isSuccess())
        {
            LogUtil.i("online login success");
            OperatorType operatorRsp = result.getData().getUser();
            int operatorState = operatorRsp.getStatus();

            String operator_username = operatorRsp.getUsername();
            //快递员账户状态
            switch (operatorState)
            {
                case OperatorStatus.Activate:
                    saveOperatorInfo(operatorRsp);
                    //检测快递员激活
                    startMainOperatorActivity(operator_username);
                    break;
                case OperatorStatus.UnActivate:
                    showPrompt(R.string.ex_account_not_active);
                    //删除本地未激活的快递员信息
                    OperatorOp.deleteOperatorById(operator_username);
                    break;
                case OperatorStatus.Blocked:
                    showPrompt(R.string.ex_account_locked);
                    //删除本地锁定的快递员信息
                    OperatorOp.deleteOperatorById(operator_username);
                    break;
                default:
                    break;
            }
        } else {
            tip = new Tip(OperatorLoginActivity.this, result.getMsg()+"", null);
            tip.show(0);
        }
    }

    @Override
    public void onResponseError(VolleyError error) {
        OperatorAfterOnline();
        LogUtil.e(error.getMessage());
    }
}
