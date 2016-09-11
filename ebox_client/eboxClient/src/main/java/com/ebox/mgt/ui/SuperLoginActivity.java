package com.ebox.mgt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.network.model.base.type.AdminUserType;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;
import com.ebox.pub.camera.PictureType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MD5Util;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

public class SuperLoginActivity extends CommonActivity implements
        OnClickListener {
    private static final String TAG = "SuperLoginActivity";
    private EditText et_telephone;
    private EditText et_verify;
    private Button bt_ok;
    private Tip tip;
    private TextView tv_timer;
    public int clickTimes = 0;
    private boolean isTest=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgt_activity_super_login);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
        isTest=true;

        if (Constants.config == Constants.Config.DEV && Constants.DEBUG)
        {
            testData();
            isTest=true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        if (!isTest)
        {
            // 开启摄像头
            AppApplication.getInstance().getCc().start();
        }
        //
        et_telephone.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    protected void onDestroy() {
        // 关闭摄像头
        AppApplication.getInstance().getCc().release();
        super.onDestroy();
        if (tip != null) {
            tip.closeTip();
        }
    }

    private void testData() {
        et_telephone.setText("02520140214");
        et_verify.setText("198506");
        onClick(bt_ok);
    }

    private void initView() {
        et_telephone = (EditText) findViewById(R.id.et_telephone);

        et_verify = (EditText) findViewById(R.id.et_verify);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_timer.setOnClickListener(this);

        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_admin_login);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private boolean isSuperAdminAccess() {
        AdminUserType[] user = SharePreferenceHelper.getSupperAdmin();

        for (int i = 0; i < user.length; i++) {
            if (et_telephone.getText() != null
                    && et_telephone.getText().toString().equals(user[i].getAdminName())
                    && et_verify.getText() != null
                    && MD5Util.getMD5String(et_verify.getText().toString()).equals(user[i].getAdminPassword())) {
                return true;
            }
        }


        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                if (isSuperAdminAccess()) {
//                    Intent intent = new Intent(SuperLoginActivity.this,
//                            SuperAdminActivity.class);

                    Intent intent = new Intent(SuperLoginActivity.this,
                            SuperAdminV2Activity.class);
                    String telephone=et_telephone.getText().toString();
                    PollingStore.getSP();
                    PollingStore.storePoll("super_name", telephone);

                    startActivity(intent);
                    Log.i(TAG, "SuperAdminAccess");
                    // 拍照
                    if (!isTest)
                    {
                        AppApplication.getInstance().getCc().takeOtherPicture(PictureType.Admin_login);
                    }
                    this.finish();
                    return;
                } else {
                    showPrompt(R.string.pub_passwd_error);
                }

                break;
            case R.id.tv_timer: {
                /*clickTimes++;
                if(clickTimes >= 5)
                {
                    ComponentName componet = new ComponentName("com.android.settings", "com.android.settings.Settings");

                    Intent i = new Intent();
                    i.setComponent(componet);
                    startActivity(i);
                }*/
                break;
            }
        }

    }


    private void showPrompt(int resId) {
        tip = new Tip(SuperLoginActivity.this, getResources().getString(
                resId), null);
        tip.show(0);
    }
}