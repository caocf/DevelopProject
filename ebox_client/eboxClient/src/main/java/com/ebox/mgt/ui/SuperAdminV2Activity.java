package com.ebox.mgt.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.utils.HardAnimUtils;
import com.ebox.mgt.ui.utils.My3GInfo;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.view.MaterialButton;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

/**
 * 版本二     超级管理员首页（运维首页）
 */
public class SuperAdminV2Activity extends CommonActivity implements View.OnClickListener, View.OnHoverListener {

    //界面属性
    private MaterialButton btFirstInstall;
    private MaterialButton btUpdateSet;
    private MaterialButton btPolling;
    private MaterialButton btExpLog;
    private MaterialButton btSingleTest;
    private TextView tv_version;
    private TextView tv_signal;
    private boolean isScale;
    //其他
    private List<ResolveInfo> mApps;
    public static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_v2);
        AppApplication.getInstance().is55Screen=true;
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
    }


    /*界面相关*/

    private void initViews() {
        btFirstInstall = (MaterialButton) this.findViewById(R.id.mgt_bt_first_install);
        btUpdateSet = (MaterialButton) this.findViewById(R.id.mgt_bt_update_set);
        btPolling = (MaterialButton) this.findViewById(R.id.mgt_bt_polling);
        btExpLog = (MaterialButton) this.findViewById(R.id.mgt_bt_export_log);
        btSingleTest = (MaterialButton) this.findViewById(R.id.mgt_bt_single_test);

        btFirstInstall.setOnClickListener(this);
        btUpdateSet.setOnClickListener(this);
        btPolling.setOnClickListener(this);
        btExpLog.setOnClickListener(this);
        btSingleTest.setOnClickListener(this);

        btFirstInstall.setOnHoverListener(this);
        btUpdateSet.setOnHoverListener(this);
        btPolling.setOnHoverListener(this);
        btExpLog.setOnHoverListener(this);
        btSingleTest.setOnHoverListener(this);

        tv_version = (TextView) findViewById(R.id.mgt_tv_version);
        tv_version.setText(FunctionUtil.getAllAppVersion());

        tv_signal = (TextView) this.findViewById(R.id.mgt_tv_signal);


        initTitle();

        //实现显示信号强度
        showSignalTimely();

    }

    private void showSignalTimely() {
        new My3GInfo(AppApplication.globalContext, tv_signal);
    }


    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_admin);
        data.tvVisibility = true;
        title.setData(data, this);
    }


    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ResolveInfo info = mApps.get(position);

            //该应用的包名
            String pkg = info.activityInfo.packageName;
            //应用的主activity类
            String cls = info.activityInfo.name;

            FunctionUtil.startApp(SuperAdminV2Activity.this, pkg, cls);
        }

    };


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //首次安装
            case R.id.mgt_bt_first_install:
                Intent intenFI = new Intent(SuperAdminV2Activity.this, FirstInstallActivity.class);
                startActivityForResult(intenFI, REQUEST_CODE);
                break;
            //更新配置
            case R.id.mgt_bt_update_set:
                Intent intentUS = new Intent(SuperAdminV2Activity.this, UpdateSetActivity.class);
                startActivity(intentUS);
                break;
            //巡检
            case R.id.mgt_bt_polling:
                Intent intentP = new Intent(SuperAdminV2Activity.this, PollingActivity.class);
                startActivity(intentP);
                break;
            //日志导出
            case R.id.mgt_bt_export_log:
                Intent intentEL = new Intent(SuperAdminV2Activity.this, ExportLogActivity.class);
                startActivity(intentEL);
                break;
            //单项检测
            case R.id.mgt_bt_single_test:
                Intent intentST = new Intent(SuperAdminV2Activity.this, SingleTestActivity.class);
                startActivity(intentST);
                break;
        }

    }

    /**
     * 鼠标滑动监听
     */
    @Override
    public boolean onHover(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_HOVER_ENTER:
                HardAnimUtils.menuEnter(v);
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_HOVER_EXIT:
                HardAnimUtils.menuExit(v);
                break;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();

        float x = btExpLog.getMeasuredWidth();


    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppApplication.getInstance().is55Screen=false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getBooleanExtra("isover", false)) {
                //首次安装配置成功
                btFirstInstall.setText("已完成首次安装");
            } else {
                btFirstInstall.setText("首次安装");
            }
        }
    }
    /*业务相关*/


}
