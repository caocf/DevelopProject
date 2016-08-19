package com.xhl.bqlh.business.view.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.ui.activity.StatisticsProductDetailsActivity;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_statistics_order)
public class HomeStatisticsOrderFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_start_time)
    private TextView tv_start_time;//开始日期

    @ViewInject(R.id.tv_end_time)
    private TextView tv_end_time;//结束日期

    @Event(R.id.ll_start_time)
    private void onStartTimeClick(View view) {
        selectSearchTime(0);
    }

    @Event(R.id.ll_end_time)
    private void onEndTimeClick(View view) {
        selectSearchTime(1);
    }

    private void selectSearchTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        long maxDate = calendar.getTimeInMillis();
        String time;
        if (type == 0) {
            time = mCondition.startTime;
        } else {
            time = mCondition.endTime;
        }
        calendar = getCalendarTime(time);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String time = (String) TextUtils.concat(String.valueOf(year), "-", NumberUtil.fixNumber(monthOfYear + 1), "-", NumberUtil.fixNumber(dayOfMonth));
                //开始日期不能大于结束日期
                if (type == 0) {
                    int i = time.compareTo(mCondition.endTime);
                    if (i > 0) {
                        SnackUtil.longShow(tv_start_time, R.string.order_search_time_error);
                        return;
                    }
                }
                //结束日期小于开始日期
                else if (type == 1) {
                    int i = time.compareTo(mCondition.startTime);
                    if (i < 0) {
                        SnackUtil.longShow(tv_start_time, R.string.order_search_time_error1);
                        return;
                    }
                }
                if (type == 0) {
                    tv_start_time.setText(time);
                    mCondition.startTime = time;
                } else {
                    tv_end_time.setText(time);
                    mCondition.endTime = time;
                }
            }
        }, year, month, day);
        //设置最大日期
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMaxDate(maxDate);
        if (type == 0) {
            dialog.setTitle(R.string.order_search_start_time);
        } else {
            dialog.setTitle(R.string.order_search_end_time);
        }

        //
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private Calendar getCalendarTime(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtil.getDateFromString(time, "yyyy-MM-dd"));
        return calendar;
    }

    @Event(R.id.btn_query)
    private void onQueryClick(View view) {
        query();
    }

    private void query() {
        if (mCondition != null) {
            Intent intent = new Intent(getContext(), StatisticsProductDetailsActivity.class);
            intent.putExtra("data", mCondition);
            startActivity(intent);
        }
    }


    private ProductQueryCondition mCondition;

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        mToolbar.setTitle(R.string.user_nav_main_order_statistics);
        //初始化查询条件
        String currentTime = TimeUtil.currentTime("yyyy-MM-dd");
        tv_start_time.setText(currentTime);
        tv_end_time.setText(currentTime);

        mCondition = new ProductQueryCondition();
        mCondition.startTime = currentTime;
        mCondition.endTime = currentTime;
        mCondition.queryType = ProductQueryCondition.TYPE_ORDER_PRODUCT;

    }
}
