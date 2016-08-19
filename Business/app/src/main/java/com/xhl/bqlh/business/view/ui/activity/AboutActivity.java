package com.xhl.bqlh.business.view.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.AppConfig.Constant;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/4/23.
 */
@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseAppActivity {

    @ViewInject(R.id.tv_version)
    private TextView tv_version;

    @ViewInject(R.id.tv_version_type)
    private TextView tv_version_type;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_back);

        String ver = getAppApplication().getPackageInfo().versionName;

        tv_version.setText("版本: V" + ver);

        if (Constant.API == Constant.Config.RELEASE) {
            tv_version_type.setVisibility(View.GONE);
        } else {
            if (Constant.API == Constant.Config.DEV) {
                tv_version_type.setText("开发环境");
            } else if (Constant.API == Constant.Config.TEST) {
                tv_version_type.setText("测试环境");
            }
        }
    }
}
