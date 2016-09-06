package com.xhl.bqlh.view.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.CheckServiceHelper;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.bqlh.view.ui.activity.HomeActivity;
import com.xhl.xhl_library.AppFileConfig;
import com.xhl.xhl_library.utils.FileUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/7/15.
 */
@ContentView(R.layout.fragment_user_setting)
public class UserSettingFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_cache_size)
    private TextView tv_cache_size;

    @ViewInject(R.id.btn_log_out)
    private Button btn_log_out;

    @ViewInject(R.id.tv_version)
    private TextView tv_version;

    @Event(R.id.btn_log_out)
    private void onLogOutClick(View view) {
        quit();
    }

    @Event(value = R.id.ripple_setting_clear)
    private void onClearClick(View view) {
        clearCache();
    }

    @Event(value = R.id.ripple_setting_check_version)
    private void onCheckVersionClick(View view) {
        CheckServiceHelper helper = new CheckServiceHelper(getActivity());
        helper.isNeedShowHint = true;
        helper.checkVersion();
    }

    private void quit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("退出登陆");
        builder.setMessage("您确定要退出账号吗？");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();

    }

    protected void exit() {

        AppDelegate.appContext.setUserLogout();

        AppDelegate.appContext.setCookie("");

        AppDelegate.appContext.mCookie = null;

//        AppDelegate.appContext.exitApp();

        GlobalCarInfo.instance().clear();

        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void showCacheSize() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                long dirSize = FileUtil.getDirSize(AppFileConfig.getDownloadAppFile());
                long dirSize1 = FileUtil.getDirSize(AppFileConfig.getTakePhoneFile());
                String size = FileUtil.formatFileSize(dirSize + dirSize1);
                return size;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                tv_cache_size.setText(aVoid);
            }
        }.execute();

    }

    private void clearCache() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AppDelegate.appContext.clearAppCache();
                FileUtil.clearFileWithPath(AppFileConfig.getDownloadAppFile().getPath());
                FileUtil.clearFileWithPath(AppFileConfig.getTakePhoneFile().getPath());
                return "清理完成";
            }

            @Override
            protected void onPostExecute(String aVoid) {
                ToastUtil.showToastShort(aVoid);
                showCacheSize();
            }
        }.execute();
    }


    @Override
    protected void initParams() {
        super.initBackBar("设置", true, false);

        if (AppDelegate.appContext.isLogin()) {
            btn_log_out.setVisibility(View.VISIBLE);
        } else {
            btn_log_out.setVisibility(View.GONE);
        }
        showCacheSize();
        String version = AppDelegate.appContext.getPackageInfo().versionName;
        if (!TextUtils.isEmpty(version)) {
            tv_version.setText("当前版本: V" + version);
        }

    }
}
