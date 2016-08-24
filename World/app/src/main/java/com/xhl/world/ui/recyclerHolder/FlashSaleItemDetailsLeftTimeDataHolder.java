package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Entities.FlashSaleDetailsEntities;
import com.xhl.world.model.Type.FlashSaleType;
import com.xhl.world.ui.utils.TimeCountHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.utils.TimeCount;

/**
 * Created by Sum on 15/12/12.
 */
public class FlashSaleItemDetailsLeftTimeDataHolder extends RecyclerDataHolder {


    public FlashSaleItemDetailsLeftTimeDataHolder(FlashSaleDetailsEntities data) {
        super(data);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flash_sale_details_letf_time, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        return new FlashSaleDetailsTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        FlashSaleDetailsTimeViewHolder holder = (FlashSaleDetailsTimeViewHolder) vHolder;
        holder.setData((FlashSaleDetailsEntities) data);
    }

    static class FlashSaleDetailsTimeViewHolder extends RecyclerViewHolder {

        private TextView tv_left_time_hint;
        private LinearLayout ll_content;
        private TextView tv_hour, tv_min, tv_sec;
        private TimeCount mTimeCount;

        public FlashSaleDetailsTimeViewHolder(View view) {
            super(view);

            tv_left_time_hint = (TextView) view.findViewById(R.id.tv_left_time_hint);
            tv_hour = (TextView) view.findViewById(R.id.tv_hour);
            tv_min = (TextView) view.findViewById(R.id.tv_min);
            tv_sec = (TextView) view.findViewById(R.id.tv_sec);
            ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
        }

        public void setData(FlashSaleDetailsEntities data) {

            if (data.getSale_state() == FlashSaleType.Flash_sale_finish) {
                tv_left_time_hint.setText(R.string.false_sale_left_time_null);
                ll_content.setVisibility(View.GONE);
                return;
            } else if (data.getSale_state() == FlashSaleType.Flash_sale_start) {
                tv_left_time_hint.setText(R.string.false_sale_left_time);
            } else if (data.getSale_state() == FlashSaleType.Flash_sale_unstart) {
                tv_left_time_hint.setText(R.string.false_sale_start_time);
            }
            setNextSaleStartTime(data.getShow_time());
        }

        /**
         * ex: 下场抢购开始时间 16:00:00
         * 抢购剩余时间是从当前时间计算到下场开始时间结束所剩余时间
         *
         * @param time 下一场开始时间
         */
        public void setNextSaleStartTime(String time) {
            stopCount();
            long total = TimeCountHelper.countLeftTime(time);
            long inter = 1000;
            mTimeCount = new TimeCount(total, inter, new TimeCount.TimeOutCallback() {
                @Override
                public void onFinish() {
                    ll_content.setVisibility(View.GONE);
                    tv_left_time_hint.setText(R.string.false_sale_left_time_null);
                }

                @Override
                public void onTick(long st) {
                    getLeftTime(st);
                }
            });
            mTimeCount.start();
        }

        private void getLeftTime(long leftTime) {
            int second = (int) (leftTime / 1000);
            int day = second / 86400;
            if (day != 0) {
                second = second % 86400;
            }
            int hour = second / 3600 + (day * 24);
            int leftSecond = second % 3600;
            int min = leftSecond / 60;
            int sec = leftSecond % 60;

            tv_hour.setText(TimeCountHelper.checkTime(hour));
            tv_min.setText(TimeCountHelper.checkTime(min));
            tv_sec.setText(TimeCountHelper.checkTime(sec));
        }

        private void stopCount() {
            if (mTimeCount != null) {
                mTimeCount.cancel();
            }
        }
    }

}
