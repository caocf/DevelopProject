package com.xhl.world.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xhl.world.R;
import com.xhl.world.ui.fragment.LoginFragment;

/**
 * Created by Sum on 15/12/7.
 */
public class BaseUserOpActivity extends BaseAppActivity {

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_op);
        pushFragmentToBackStack(LoginFragment.class, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //第三方登陆需要重写
        if (count > 0) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(0);
            (fragment).onActivityResult(requestCode, resultCode, data);
        }
    }
}
