package com.xhl.xhl_library.Base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xhl.xhl_library.utils.AppManager;

import org.xutils.x;

/**
 * Created by Sum on 15/11/21.
 */
public abstract class BaseActivity extends AppCompatActivity {

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
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
