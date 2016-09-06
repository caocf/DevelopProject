package com.xhl.bqlh.doman;

import android.app.Activity;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.data.PreferenceData;
import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.VersionInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.xhl_library.network.download.DownloadManager;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
        //购物车
//        carUpdate();
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

    public void carUpdate() {

        if (!AppDelegate.appContext.isLogin()) {
            return;
        }

        ApiControl.getApi().carInfo(new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                HashMap<Object, ArrayList<CarModel>> resultObj = result.getObj().getResult();

                //save
//                GlobalCarInfo.instance().setLoadCarInfo(resultObj);


                Collection<ArrayList<CarModel>> values = resultObj.values();
                List<CarModel> carModels = new ArrayList<>();
                for (ArrayList<CarModel> item : values) {
                    carModels.addAll(item);
                }
                //通知购物车总体数据
                GlobalCarInfo.instance().clear();
                for (CarModel car : carModels) {
                    car.mCurNum = car.getPurchaseQuantity();
                    GlobalCarInfo.instance().putCarInfo(car);
                }
            }

            @Override
            public void finish() {
            }
        });
    }

}
