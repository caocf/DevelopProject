package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sum on 16/4/28.
 */
public class SignRecordDataHolder extends RecyclerDataHolder<SignRecordModel> {

    public SignRecordDataHolder(SignRecordModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_sign_reocrd, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, SignRecordModel data) {
        SignViewHolder holder = (SignViewHolder) vHolder;
        holder.onBindData(data);
    }

    private class SignViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.tv_sign_time)
        private TextView tv_sign_time;

        @ViewInject(R.id.tv_sign_date)
        private TextView tv_sign_date;

        @ViewInject(R.id.tv_start_time)
        private TextView tv_start_time;

        @ViewInject(R.id.tv_start_state)
        private TextView tv_start_state;

        @ViewInject(R.id.tv_start_location)
        private TextView tv_start_location;

        @ViewInject(R.id.tv_end_location)
        private TextView tv_end_location;

        @ViewInject(R.id.tv_end_time)
        private TextView tv_end_time;

        @ViewInject(R.id.tv_end_state)
        private TextView tv_end_state;

        public SignViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        private void onBindData(SignRecordModel data) {
            if (data == null) {
                return;
            }
            //签到日期
            Date signDate = data.signDate();
            String time = TimeUtil.getNormalStringDate(signDate);
            tv_sign_time.setText(time);
            String week = TimeUtil.dateToWeek(signDate);
            tv_sign_date.setText(week);

            //签到地址
            tv_start_location.setText(data.getWorkPlace());
            tv_end_location.setText(data.getDutyPlace());

            //状态
            tv_start_state.setText(data.signWorkState());
            tv_start_state.setTextColor(ContextCompat.getColor(mContext, data.signWorkStateBg()));

            tv_end_state.setText(data.signDutyState());
            tv_end_state.setTextColor(ContextCompat.getColor(mContext, data.signDutyStateBg()));

            Calendar instance = Calendar.getInstance();

            //签到时间
            String workTime = data.getWorkTime();
            if (!TextUtils.isEmpty(workTime)) {
                Date date = TimeUtil.getDateFromString(workTime);
                instance.setTime(date);
                int hour = instance.get(Calendar.HOUR_OF_DAY);
                int minute = instance.get(Calendar.MINUTE);
                tv_start_time.setText(NumberUtil.fixNumber(hour) + ":" + NumberUtil.fixNumber(minute));
            }else {
                tv_start_time.setText("");
            }
            //签退时间
            String dutyTime = data.getDutyTime();
            if (!TextUtils.isEmpty(dutyTime)) {
                Date date = TimeUtil.getDateFromString(dutyTime);
                instance.setTime(date);
                int hour = instance.get(Calendar.HOUR_OF_DAY);
                int minute = instance.get(Calendar.MINUTE);
                tv_end_time.setText(NumberUtil.fixNumber(hour) + ":" + NumberUtil.fixNumber(minute));
            }else {
                tv_end_time.setText("");
            }
        }

    }
}
