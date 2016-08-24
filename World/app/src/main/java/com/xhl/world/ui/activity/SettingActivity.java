package com.xhl.world.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xhl.sum.chatlibrary.controller.ChatManager;
import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.chat.service.PushManager;
import com.xhl.world.data.PreferenceData;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.VersionModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.main.MainActivity;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.AppFileConfig;
import com.xhl.xhl_library.network.download.DownloadManager;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.FileUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/12/28.
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseAppActivity {

    @ViewInject(R.id.ripple_setting_clear)
    private RippleView ripple_setting_clear;

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

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    @Event(value = R.id.ripple_setting_check_version, type = RippleView.OnRippleCompleteListener.class)
    private void onCheckVersionClick(View view) {
        checkVersion();
    }

    private void checkVersion() {
        showLoadingDialog();
        ApiControl.getApi().version(new Callback.CommonCallback<ResponseModel<VersionModel>>() {
            @Override
            public void onSuccess(ResponseModel<VersionModel> result) {
                if (result.isSuccess()) {
                    String newCode = result.getResultObj().version;//版本号

                    if (PreferenceData.getInstance().versionNeedUpdate(newCode)) {
                        boolean isForce = false;

                        String url = result.getResultObj().url;
                        String content = result.getResultObj().version_update_info;

                        new DownloadManager().setActivity(SettingActivity.this).setIsNeedShowView(true).setUrl(url).setIsForceDownload(isForce).setLabel(content).start();

                    } else {
                        SnackMaker.shortShow(tv_cache_size, "当前已是最新版本");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }

    @Event(value = R.id.ripple_setting_clear, type = RippleView.OnRippleCompleteListener.class)
    private void onClearClick(View view) {
      /*  if (!mIsNeedClear) {
            SnackMaker.shortShow(view,"清理已完成");
            return;
        }
        DialogMaker.showAlterDialog(this, "清理缓存", "您确定需要清理缓存吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCache();
            }
        });*/
        clearCache();
    }

    @Event(value = R.id.ripple_setting_msg, type = RippleView.OnRippleCompleteListener.class)
    private void onMsgClick(View view) {

    }

    @ViewInject(R.id.title_name)
    private TextView title_name;

    private boolean mIsNeedClear = false;

    @Override
    protected void initParams() {
        title_name.setText("设置");
        if (AppApplication.appContext.isLogin()) {
            btn_log_out.setVisibility(View.VISIBLE);
        } else {
            btn_log_out.setVisibility(View.GONE);
        }
        showCacheSize();
        String version = AppApplication.appContext.getPackageInfo().versionName;
        if (!TextUtils.isEmpty(version)) {
            tv_version.setText("当前版本: V" + version);
        }
    }

    private void showCacheSize() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                long dirSize = FileUtil.getDirSize(AppFileConfig.getDownloadAppFile());
                long dirSize1 = FileUtil.getDirSize(AppFileConfig.getTakePhoneFile());
                if (dirSize + dirSize1 > 0) {
                    mIsNeedClear = true;
                }
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
                AppApplication.appContext.clearAppCache();
                FileUtil.clearFileWithPath(AppFileConfig.getDownloadAppFile().getPath());
                FileUtil.clearFileWithPath(AppFileConfig.getTakePhoneFile().getPath());
                return "清理完成";
            }

            @Override
            protected void onPostExecute(String aVoid) {
                SnackMaker.shortShow(tv_cache_size, aVoid);
                showCacheSize();
            }
        }.execute();
    }


    private void quit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        AppApplication.appContext.setUserLogout();
        //断开聊天链接
        if (ChatManager.getInstance().isConnect()) {
            ChatManager.getInstance().closeWithCallback(null);
        }
        //取消个人消息注册
        PushManager.getInstance().unsubscribeCurrentUserChannel();

        ApiControl.getApi().userLogout(new Callback.CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    Logger.v("clear service session success");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(tv_cache_size, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        //通知退出登录
        EventBusHelper.postExitLogin();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
        finish();
    }
}
