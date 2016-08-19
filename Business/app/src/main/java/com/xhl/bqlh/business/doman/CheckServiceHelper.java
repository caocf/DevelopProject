package com.xhl.bqlh.business.doman;

import android.app.Activity;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.Member;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignConfigModel;
import com.xhl.bqlh.business.Model.VersionInfo;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.xhl_library.network.download.DownloadManager;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Sum on 16/4/23.
 * 获取服务的配置的信息
 */
public class CheckServiceHelper {

    private Activity context;

    public boolean isNeedShowHint = false;

    public CheckServiceHelper(Activity context) {
        this.context = context;
    }

    public void start() {
        //版本检测
        checkVersion();
        //配置检测
        checkConfig();
        //
        pullMember();
        //判断是否签到
        long lastTime = PreferenceData.getInstance().signLastTime();
        if (lastTime != 0) {
            Calendar instance = Calendar.getInstance();
            //今天周几
            int week = instance.get(Calendar.DAY_OF_WEEK);
            instance.setTimeInMillis(lastTime);
            int lastWeek = instance.get(Calendar.DAY_OF_WEEK);
            if (week != lastWeek) {
                PreferenceData.getInstance().todaySignEnd(false);
                PreferenceData.getInstance().todaySignStart(false);
            }
        }
    }

    private void pullMember() {
        ApiControl.getApi().pullTaskMember(new Callback.CommonCallback<ResponseModel<Member>>() {
            @Override
            public void onSuccess(final ResponseModel<Member> result) {
                if (result.isSuccess()) {
                    x.task().run(new Runnable() {
                        @Override
                        public void run() {
                            List<Member> objList = result.getObjList();
                            DbTaskHelper.getInstance().addMember(objList);
                            //读取到内存中
                            ModelHelper.saveOneMember(null);
                        }
                    });
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

            }
        });
    }

    public void checkConfig() {

        ApiControl.getApi().signRule(new Callback.CommonCallback<ResponseModel<SignConfigModel>>() {
            @Override
            public void onSuccess(ResponseModel<SignConfigModel> result) {
                if (result.isSuccess()) {
                    SignConfigModel obj = result.getObj();
                    PreferenceData.getInstance().signConfig(obj);
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

            }
        });

    }

    public void checkVersion() {

        ApiControl.getApi().versionInfo(new Callback.CommonCallback<ResponseModel<VersionInfo>>() {
            @Override
            public void onSuccess(ResponseModel<VersionInfo> result) {
                if (result.isSuccess()) {

                    VersionInfo obj = result.getObj();

                    PreferenceData.getInstance().setDownloadUrl(obj.getUrl());

                    boolean needUpdate = PreferenceData.getInstance().versionNeedUpdate(obj.getVersion());

                    if (needUpdate) {
                        DownloadManager manager = new DownloadManager();
                        manager.setActivity(context).setIsForceDownload(obj.isupdate()).
                                setIsNeedShowView(true).setLabel(obj.getVersionupdateinfo()).setUrl(obj.getUrl()).start();

                    } else {
                        if (isNeedShowHint) {
                            ToastUtil.showToastLong("当前已是最新版本");
                        }
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

            }
        });

    }

}
