package com.xhl.xhl_library.Base;

import android.content.Intent;
import android.os.Bundle;

import com.xhl.xhl_library.R;
import com.xhl.xhl_library.utils.AppManager;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;

/**
 * Created by Sum on 15/11/21.
 */
public abstract class BaseActivity extends AutoLayoutActivity {

    protected abstract void initParams();

    public boolean isNeedInject() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedInject()) {
            x.view().inject(this);
            initParams();
        }
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }
}
