package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/5/13.
 */
public class SignRecordTableDataHolder extends RecyclerDataHolder<SignRecordModel> {
    public SignRecordTableDataHolder(SignRecordModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new SignTable(View.inflate(context, R.layout.item_sign_record_table, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, SignRecordModel data) {
        SignTable h = (SignTable) vHolder;
        h.onBindData(data);
    }

    class SignTable extends RecyclerViewHolder {

        @ViewInject(R.id.tv_day)
        private TextView tv_day;

        @ViewInject(R.id.iv_am)
        private ImageView iv_am;

        @ViewInject(R.id.iv_am_warm)
        private ImageView iv_am_warm;

        @ViewInject(R.id.iv_pm)
        private ImageView iv_pm;

        @ViewInject(R.id.iv_pm_warm)
        private ImageView iv_pm_warm;

        public SignTable(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(SignRecordModel data) {
            if (data == null) {
                return;
            }
            int recordDay = data.getDay();
            //第几天
            tv_day.setText(String.valueOf(recordDay));

            if (recordDay <= data.curDay) {
                boolean normal = true;
                int workStatus = data.getWorkStatus();
                //迟到
                if (workStatus == 2 || workStatus == 4) {
                    iv_am.setImageResource(R.drawable.ic_sign_state_late);
                    normal = false;
                }
                //迟到警告
                ViewHelper.setViewGone(iv_am_warm, workStatus != 4);
                //早退
                int dutyStatus = data.getDutyStatus();
                if (dutyStatus == 2 || dutyStatus == 4) {
                    iv_pm.setImageResource(R.drawable.ic_sign_state_early);
                    normal = false;
                }
                //早退警告
                ViewHelper.setViewGone(iv_pm_warm, dutyStatus != 4);
                //未打卡
                if (workStatus == 0) {
                    iv_am.setImageResource(R.drawable.ic_sign_state_null);
                    normal = false;
                }
                if (dutyStatus == 0) {
                    iv_pm.setImageResource(R.drawable.ic_sign_state_null);
                    normal = false;
                }
                if (!normal) {
                    tv_day.setTextColor(ContextCompat.getColor(mContext, R.color.base_disable_text_color));
                } else {
                    tv_day.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                }
                //当天
                if (recordDay == data.curDay) {
                    tv_day.setTextColor(ContextCompat.getColor(mContext, R.color.app_while));
                    tv_day.setBackgroundResource(R.drawable.code_oval_end);
                }
            }

        }
    }
}
