package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.Constants;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.update.VersionUpdate;
import com.moge.gege.ui.widget.update.VersionUpdate.UpdateResultListener;
import com.moge.gege.util.LocationUtil;
import com.moge.gege.util.PackageUtil;
import com.moge.gege.util.UmengUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;


/**
进入首界面
**/

public class IndexActivity extends BaseActivity
{
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mContext = IndexActivity.this;

        UmengUtil.initConfig(getApplicationContext());
        LocationUtil.instance().init(getApplicationContext());
        LocationUtil.instance().start();

        // xg push
        if(Constants.config != Constants.Config.RELEASE) {
            XGPushConfig.enableDebug(this, true);
        }
        XGPushManager.registerPush(getApplicationContext());
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // check version
        new VersionUpdate(this, new UpdateResultListener()
        {
            @Override
            public void onNotUpdate(int result)
            {
                // improve login success
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        directGoWithoutGuidePage();
                    }
                }, 500L);
            }
        });
    }

    private void whereToGo()
    {
        // whether to show guide page
        String versionName = PackageUtil.getVersionName(mContext);
        if (versionName.equalsIgnoreCase(PersistentData.instance()
                .getVersionName()))
        {
            directGoWithoutGuidePage();
        }
        else
        {
            PersistentData.instance().setVersionName(versionName);
            UIHelper.showGuideActivity(mContext);
        }
    }

    private void directGoWithoutGuidePage()
    {
        if (!AppApplication.isCookieEmpty())
        {
            doSignin(mContext, LoginFromType.FROM_APP_START);
        }
        else
        {
            gotoHomePage();
        }
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        super.onLoginResult(from, result);

        gotoHomePage();
    }

    private void gotoHomePage()
    {
        UIHelper.showHomePageActivity(mContext);
        finish();
    }
}
