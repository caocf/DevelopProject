package com.xhl.bqlh.business.view.ui.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.PermissionHelper;
import com.xhl.bqlh.business.view.ui.fragment.LoginFragment;

/**
 * Created by Sum on 16/4/18.
 */
public class LoginActivity extends BaseAppActivity {

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionHelper.hasPermissionLocation(this)) {
                PermissionHelper.reqPermissionLocation(this, 1);
            }
        }
        pushFragmentToBackStack(LoginFragment.class, null);

        setStatusBarRes(R.color.colorPrimary);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Permission Denied
            DialogMaker.refuseShowSetLocation(this);
        }
    }

    @Override
    protected void initParams() {
    }

}
