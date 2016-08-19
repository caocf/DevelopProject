package com.xhl.bqlh.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xhl.bqlh.R;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;

/**
 * Created by Sum on 16/7/1.
 */
public class ContainerActivity extends BaseAppActivity {

    @Override
    protected boolean isNeedEventBus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Intent intent = getIntent();

        int tag = intent.getIntExtra(FragmentContainerHelper.F_TAG, -1);
        if (tag == -1) {
            ToastUtil.showToastLong("数据异常");
            finish();
            return;
        }

        //状态栏颜色
        int colorRes = intent.getIntExtra(FragmentContainerHelper.F_COLOR, -1);
        if (colorRes != -1) {
            setStatusBarRes(colorRes);
        }

        Class aClass = FragmentContainerHelper.getFragmentByTag(tag);
        pushFragmentToBackStack(aClass, null);

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {

    }
}
