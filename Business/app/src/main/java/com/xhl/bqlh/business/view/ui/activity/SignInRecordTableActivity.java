package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.recyclerHolder.SignRecordTableDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.TimeUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sum on 16/5/13.
 */
@ContentView(R.layout.activity_sign_record_table)
public class SignInRecordTableActivity extends BaseAppActivity {


    @ViewInject(R.id.ll_content)
    private View ll_content;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_work_late)
    private TextView tv_work_late;

    @ViewInject(R.id.tv_work_early)
    private TextView tv_work_early;

    @ViewInject(R.id.tv_work_null)
    private TextView tv_work_null;

    @ViewInject(R.id.tv_work_warn)
    private TextView tv_work_warn;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_clear);
        setTitle(R.string.menu_sign_record);

        GridLayoutManager manager = new GridLayoutManager(this, 7);

        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        ll_content.setVisibility(View.INVISIBLE);
        loadData();
    }

    private void loadData() {

        showLoadingDialog();

        ApiControl.getApi().signRecordTable(new Callback.CommonCallback<ResponseModel<SignRecordModel>>() {
            @Override
            public void onSuccess(ResponseModel<SignRecordModel> result) {
                if (result.isSuccess()) {
                    createData(result.getObjList());
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
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

    private void createData(List<SignRecordModel> records) {

        statistics(records);

        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DAY_OF_MONTH);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        //本月的第一天星期几
        int week = instance.get(Calendar.DAY_OF_WEEK);
        List<RecyclerDataHolder> holders = new ArrayList<>();
        //空的显示
        for (int i = 0; i < week - 1; i++) {
            holders.add(new SignRecordTableDataHolder(null));
        }
        //从第一天开始显示
        for (SignRecordModel sign : records) {
            sign.curDay = day;
            holders.add(new SignRecordTableDataHolder(sign));
        }
        //设置显示
        mAdapter.setDataHolders(holders);
        ll_content.setVisibility(View.VISIBLE);
    }

    private void statistics(List<SignRecordModel> recordModels) {

        int countLate = 0;
        int countEarly = 0;
        int countNull = 0;
        int countWarn = 0;

        //这个月的第几天
        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DAY_OF_MONTH);

        for (SignRecordModel record : recordModels) {
            int recordDay = record.getDay();
            if (recordDay > day) {
                Logger.v("结束计算：" + recordDay + " curD:" + day);
                break;//超出当天后结束
            }
            int workStatus = record.getWorkStatus();
            //迟到
            if (workStatus == 2 || workStatus == 4) {
                countLate++;
                if (workStatus == 4) {
                    countWarn++;
                }
            }
            //早退
            int dutyStatus = record.getDutyStatus();
            if (dutyStatus == 2 || dutyStatus == 4) {
                countEarly++;
                if (dutyStatus == 4) {
                    countWarn++;
                }
            }
            //未打卡
            if (workStatus == 0) {
                countNull++;
            }
            if (dutyStatus == 0) {
                countNull++;
            }
        }
        //显示次数
        tv_work_late.setText(String.valueOf(countLate));
        tv_work_early.setText(String.valueOf(countEarly));
        tv_work_null.setText(String.valueOf(countNull));
        tv_work_warn.setText(String.valueOf(countWarn));
        tv_time.setText(TimeUtil.currentTime("yyyy年MM月"));
    }


}
