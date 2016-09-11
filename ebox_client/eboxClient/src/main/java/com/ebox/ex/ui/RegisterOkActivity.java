package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ebox.R;
import com.ebox.pub.utils.MGViewUtil;

/**
 * 注册结果成功界面
 * 广告位的展示
 */
public class RegisterOkActivity extends CommonActivity implements View.OnClickListener {

    private RegisterOkActivity activity;
    private Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ok);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        activity = this;

        initViews();
    }

    private void initViews() {

        bt_back = (Button) this.findViewById(R.id.ex_bt_ro_back);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Intent intent = new Intent(RegisterOkActivity.this, OperatorLoginActivity.class);
                    activity.startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ex_bt_ro_back:
                finish();
                break;
        }
    }
}
