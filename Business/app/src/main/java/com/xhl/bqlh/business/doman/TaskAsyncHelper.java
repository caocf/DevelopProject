package com.xhl.bqlh.business.doman;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Db.TaskRecord;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignConfigModel;
import com.xhl.bqlh.business.Model.TaskModel;
import com.xhl.bqlh.business.Model.Type.ShopStateType;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.xhl_library.utils.TimeUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sum on 16/4/22.
 */
public class TaskAsyncHelper {

    public void setmNeedPullRecord(boolean mNeedPullRecord) {
        this.mNeedPullRecord = mNeedPullRecord;
    }

    public interface TaskListener {
        void onPullStart();

        void onPullFinish();

        void onPullFailed(String msg);
    }

    private boolean mNeedPullRecord = true;

    private TaskListener listener;

    public TaskAsyncHelper(TaskListener listener) {
        this.listener = listener;
    }


    public void pullServiceTask() {
        if (listener != null) {
            listener.onPullStart();
        }
        ApiControl.getApi().pullTask(new Callback.CommonCallback<ResponseModel<TaskModel>>() {
            @Override
            public void onSuccess(final ResponseModel<TaskModel> result) {
                if (result.isSuccess()) {
                    x.task().run(new Runnable() {
                        @Override
                        public void run() {
                            List<TaskModel> objList = result.getObjList();
                            saveTask(objList);
                            if (mNeedPullRecord) {
                                pullServiceTaskRecord();
                            } else if (listener != null) {
                                listener.onPullFinish();
                            }
                        }
                    });
                } else {
                    if (listener != null) {
                        listener.onPullFailed(result.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getMessage());
                if (listener != null) {
                    listener.onPullFailed(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveTask(List<TaskModel> tasks) {
        List<TaskShop> shops = new ArrayList<>();

        for (TaskModel task : tasks) {
            TaskShop shop = new TaskShop();
            shop.setWeek(task.getWeek());
            shop.setIndex(task.getSort());
            shop.setShopId(task.getRetailerId());
            shop.setShopName(task.getCompanyName());
            shop.setShopLocation(task.getAddress());
            shop.setShopCity(task.getPosition());
            shop.setShopAddress(task.getArea());
            shop.setLiableName(task.getLiableName());
            shop.setLiablePhone(task.getLiablePhone());
            shop.setShopLongitude(task.getCoordinateX());
            shop.setShopLatitude(task.getCoordinateY());
            //添加门店状态
            shop.setData1(ShopStateType.STATE_CLOSE);

            shops.add(shop);
        }
        DbTaskHelper.getInstance().addTaskShop(shops);
    }

    private void pullServiceTaskRecord() {
        ApiControl.getApi().pullTaskRecord(new Callback.CommonCallback<ResponseModel<TaskModel>>() {
            @Override
            public void onSuccess(final ResponseModel<TaskModel> result) {
                if (result.isSuccess()) {

                    x.task().run(new Runnable() {
                        @Override
                        public void run() {
                            saveTaskRecord(result.getObjList());
                            checkConfig();
                        }
                    });

                } else {
                    if (listener != null) {
                        listener.onPullFailed(result.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getMessage());
                if (listener != null) {
                    listener.onPullFailed(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void saveTaskRecord(List<TaskModel> tasks) {
        List<TaskRecord> records = new ArrayList<>();
        for (TaskModel task : tasks) {
            TaskRecord record = new TaskRecord();
            record.setShopId(task.getRetailerId());
            record.setTaskState(TaskType.STATE_FINISH);
            record.setTaskType((task.getChecktype()));
            String chinkintime = task.getChinkintime();
            Date dateFromString = TimeUtil.getDateFromString(chinkintime);
            record.setTaskTime(dateFromString);

            records.add(record);
        }
        if (records.size() > 0) {
            DbTaskHelper.getInstance().addTaskRecord(records);
        }
    }

    private void checkConfig() {

        ApiControl.getApi().signRule(new Callback.CommonCallback<ResponseModel<SignConfigModel>>() {
            @Override
            public void onSuccess(ResponseModel<SignConfigModel> result) {
                if (result.isSuccess()) {
                    SignConfigModel obj = result.getObj();
                    PreferenceData.getInstance().signConfig(obj);
                    if (listener != null) {
                        listener.onPullFinish();
                    }
                } else {
                    if (listener != null) {
                        listener.onPullFailed(result.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (listener != null) {
                    listener.onPullFailed(ex.getMessage());
                }
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
