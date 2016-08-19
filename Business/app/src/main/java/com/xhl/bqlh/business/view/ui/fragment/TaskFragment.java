package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.TaskRecord;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.recyclerHolder.TaskDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sum on 16/3/22.
 */
@ContentView(R.layout.fragment_task)
public class TaskFragment extends BaseAppFragment implements RecycleViewCallBack {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    @ViewInject(R.id.tv_task_time)
    private TextView tv_task_time;

    @ViewInject(R.id.tv_task_state)
    private TextView tv_task_state;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private int mDay;//表示今天是星期几
    private Date mDayDate;//表示今天日期
    private int mShopSize;//任务个数

    private int mDayInterval;//日期差距
    private boolean mNeedRefresh = true;

    private List<TaskShop> mShowShops;

    public static TaskFragment newInstance(int day) {
        TaskFragment fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("day", day);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    protected void initParams() {
        mDay = getArguments().getInt("day", 0);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        calculate();
    }

    @Override
    protected void onVisibleLoadData() {
        if (mNeedRefresh) {
            calculate();
        }
    }

    public void onEvent(CommonEvent event) {
        if (event.eventType == CommonEvent.EVENT_REFRESH_TASK) {
            //刷新任务
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 500);
        }
    }

    //添加一个临时任务
    public void addOneTemporaryTask(TaskShop record) {
        if (record != null && mShowShops != null) {
            record.setDayInterval(mDayInterval);
            ViewHelper.setViewGone(mRecyclerView, false);
            mShowShops.add(record);
            TaskDataHolder holder = new TaskDataHolder(record);
            holder.setCallBack(this);
            mAdapter.addDataHolder(holder);
        }
    }

    //刷新一个任务状态
    public void finishOneTask(String shopId) {
        calculate();
    }


    private void reload() {
        try {
            List<TaskShop> list = loadData();
            mShowShops = list;
            //计算百分比
            calculatePercent();
            //显示数据
            showData(list);
        } catch (Exception e) {
            e.printStackTrace();
            AVAnalytics.reportError(getContext(), e);
        }
    }

    //设置显示的店铺数据
    private void showData(List<TaskShop> shops) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (TaskShop shop : shops) {
            TaskDataHolder holder = new TaskDataHolder(shop);
            holder.setCallBack(this);
            holders.add(holder);
        }
        if (holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        if (mAdapter != null) {
            mAdapter.setDataHolders(holders);
        }
    }

    //加载完成后才进行再次加载
    public void reLoadInfo() {
        if (!mNeedRefresh) {
            calculate();
        }
    }

    //计算当前日期星期几的日期
    private void calculate() {
        mNeedRefresh = false;//完成刷新

        Calendar instance = Calendar.getInstance();
        //今天周几
        int week = instance.get(Calendar.DAY_OF_WEEK);
        int dayInterval = mDay - week;
        //设置日期差距
        instance.add(Calendar.DAY_OF_WEEK, dayInterval);
        mDayInterval = dayInterval;
        //昨天日期需要设置为23:59:59
        if (dayInterval < 0) {
            instance.set(Calendar.HOUR_OF_DAY, 23);
            instance.set(Calendar.SECOND, 59);
            instance.set(Calendar.MINUTE, 59);
        }
        mDayDate = instance.getTime();
//        Logger.v("today:" + week + " day:" + mDay + " dayInterval:" + dayInterval + " date:" + mDayDate);

        //当天日期
        String time = "";
        if (dayInterval == 0) {
            time = "今天";
        } else if (dayInterval == -1) {
            time = "昨天";
        } else if (dayInterval == 1) {
            time = "明天";
        } else {
            time = TimeUtil.getNormalStringDate(mDayDate);
        }
        try {
            if (tv_task_time != null) {
                tv_task_time.setText(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AVAnalytics.reportError(getContext(), e);
        }

        //加载一次数据
        reload();

    }

    private void calculatePercent() {
        try {
            //完成任务数量
            float finishCount = DbTaskHelper.getInstance().getFinishTaskByEndTime(mDayDate);
            //正常任务数量
            int shopCount = mShopSize;
            if (shopCount == 0 && mDayInterval <= 0) {
                String percent = (String) TextUtils.concat("完成:", String.valueOf(finishCount), "%");
                if (tv_task_state != null) {
                    tv_task_state.setText(percent);
                }
                return;
            }
            if (mDayInterval > 0) {
                tv_task_state.setText("待计算");
                return;
            }
            float res = (finishCount / shopCount) * 100;
            String format = new DecimalFormat("#0").format(res);

            String percent = (String) TextUtils.concat("完成:", format, "%");
            if (tv_task_state != null) {
                tv_task_state.setText(percent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AVAnalytics.reportError(getContext(), e);
        }
    }

    //读取数据库店铺数据和任务状态类型
    private List<TaskShop> loadData() {
        //获取任务店铺数据
        List<TaskShop> shops = DbTaskHelper.getInstance().getTaskShopByDay(mDay);
        if (shops == null) {
            shops = new ArrayList<>();
        }
        //获取任务数据
        List<TaskRecord> records = DbTaskHelper.getInstance().getRecordByEndTime(mDayDate);
        //正常任务数量
        mShopSize = shops.size();

        int interval = mDayInterval;
        //设置店铺完成的任务状态
        for (TaskShop shop : shops) {
            //记录日期差
            shop.setDayInterval(mDayInterval);
            if (interval > 0) {//未开始
                shop.setTaskState(TaskType.STATE_UN_START);
                continue;
            } else if (interval < 0) {//过期任务处理
                //过期任务未完成
                shop.setTaskState(TaskType.STATE_UN_FINISH);
            } else if (interval == 0) {//当天任务处理
                //当天待拜访
                shop.setTaskState(TaskType.STATE_START);
            }
            if (records == null) {
                continue;
            }
            //获取店铺对应的任务状态
            for (TaskRecord record : records) {
                String shopId = record.getShopId();
                //一家店铺的一个任务
                if (shopId.equals(shop.getShopId())) {
                    shop.setTaskState(record.getTaskState());
//                    Logger.v("任务状态:" + record.getTaskState() + " shopId:" + shopId);
                    break;
                }
            }
        }
        //临时任务处理
        List<TaskRecord> temporaryRecords = DbTaskHelper.getInstance().getTemporaryByEndTime(mDayDate);
        if (temporaryRecords != null) {
            for (TaskRecord temporary : temporaryRecords) {
                TaskShop taskShop = temporary.getTaskShop();
                //避免拜访计划删除出现问题
                if (taskShop == null) {
                    continue;
                }
                taskShop.setTaskType(TaskType.TYPE_Temporary);
                taskShop.setDayInterval(mDayInterval);
                //临时任务的id，用于删除操作
                taskShop.setTaskId(temporary.getId());
                //添加时间判断在退出应用后重新设置
                if (interval > 0) {//未开始
                    taskShop.setTaskState(TaskType.STATE_UN_START);
                } else if (interval < 0) {//过期任务处理
                    //过期任务可能完成或者未完成
                    int taskState = temporary.getTaskState();
                    if (taskState != TaskType.STATE_FINISH) {
                        taskShop.setTaskState(TaskType.STATE_UN_FINISH);
                    } else {
                        taskShop.setTaskState(taskState);
                    }
                } else if (interval == 0) {//当天任务处理
                    taskShop.setTaskState(temporary.getTaskState());
                }
//                Logger.v("临时任务:" + temporary.toString());
                shops.add(taskShop);
            }
        }
        return shops;
    }

    @Override
    public void onItemClick(int position) {
        mAdapter.removeDataHolder(position);
        if (position < mShowShops.size()) {
            mShowShops.remove(position);
            if (mShowShops.size() == 0) {
                ViewHelper.setViewGone(mRecyclerView, true);
            }
        }
    }
}
