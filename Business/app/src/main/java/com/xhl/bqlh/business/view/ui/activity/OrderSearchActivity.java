package com.xhl.bqlh.business.view.ui.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.ui.fragment.OrderManagerFragment;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

/**
 * Created by Sum on 16/4/21.
 */
@ContentView(R.layout.activity_order_manager_search)
public class OrderSearchActivity extends BaseAppActivity {
    @ViewInject(R.id.tv_start_time)
    private TextView tv_start_time;//开始日期

    @ViewInject(R.id.tv_end_time)
    private TextView tv_end_time;//结束日期

    @ViewInject(R.id.tv_order_type)
    private TextView tv_order_type;//订单类型

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;//订单状态

    @Event(R.id.ll_start_time)
    private void onStartTimeClick(View view) {
        selectSearchTime(0);
    }

    @Event(R.id.ll_end_time)
    private void onEndTimeClick(View view) {
        selectSearchTime(1);
    }

    @Event(R.id.ll_order_type)
    private void onOrderTypeClick(View view) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.order_type);
        final String[] type = mCondition.getOrderTypeArray();

        dialog.setSingleChoiceItems(type, mCondition.orderType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mCondition.orderType = OrderType.order_type_all;
                } else if (which == 1) {
                    mCondition.orderType = OrderType.order_type_car;
                } else if (which == 2) {
                    mCondition.orderType = OrderType.order_type_self;
                } else if (which == 3) {
                    mCondition.orderType = OrderType.order_type_shop;
                }
                tv_order_type.setText(type[which]);

                //重置状态
                tv_order_state.setText(R.string.order_search_all);
                mCondition.selectWhich = 0;
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setPositiveButton(R.string.dialog_ok, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Event(R.id.ll_order_state)
    private void onOrderStateClick(View view) {
        //获取订单状态
        final String[] state = mCondition.getStateByType(mCondition.orderType);
        if (state == null) {//全部订单
            SnackUtil.longShow(view, "该订单类型无订单状态");
            return;
        }
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.order_state);

        dialog.setSingleChoiceItems(state, mCondition.orderState, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCondition.orderState = which;//订单状态
                tv_order_state.setText(state[which]);
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setPositiveButton(R.string.dialog_ok, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Event(R.id.btn_query)
    private void onQueryClick(View view) {
        query();
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

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                } //结束日期小于开始日期
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
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private Calendar getCalendarTime(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtil.getDateFromString(time, "yyyy-MM-dd"));
        return calendar;
    }

    private void query() {
        final OrderManagerFragment fragment = OrderManagerFragment.newInstance(mCondition);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).addToBackStack(null).commit();

        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.onRefreshTop();
            }
        }, 200);

        AVAnalytics.onEvent(this, "click search order");
    }

    private OrderQueryCondition mCondition;

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    protected void initParams() {
        super.initToolbar();
        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            setTitle("赊账查询");
        } else {
            setTitle(R.string.order_search);
        }


        //初始化查询条件
        String currentTime = TimeUtil.currentTime("yyyy-MM-dd");
        tv_start_time.setText(currentTime);
        tv_end_time.setText(currentTime);

        mCondition = new OrderQueryCondition();
        if (type == 1) {
            mCondition.creditStatus = "2";
        }
        mCondition.hint = "未查询到相关订单";
        mCondition.startTime = currentTime;
        mCondition.endTime = currentTime;
        //车销订单状态
        mCondition.setStateCar(getResources().getStringArray(R.array.order_state_by_car));
        //门店订单状态
        mCondition.setStateNor(getResources().getStringArray(R.array.order_state));
        //订单类型
        mCondition.setOrderTypeArray(getResources().getStringArray(R.array.order_type));

    }
}
