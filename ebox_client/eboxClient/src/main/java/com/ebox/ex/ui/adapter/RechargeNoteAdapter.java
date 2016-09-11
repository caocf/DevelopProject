package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.RechargeNoteType;
import com.ebox.pub.ui.adapter.BaseListAdapter;

/**
 * Created by prin on 2015/11/2.
 * 充值记录的数据适配器
 */
public class RechargeNoteAdapter extends BaseListAdapter<RechargeNoteType> {

    private Context mContext;

    public RechargeNoteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ex_item_note, null);
            holder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ll_content = (LinearLayout) convertView.findViewById(R.id.ex_ll_in_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RechargeNoteType model = list.get(position);

        //对控件进行数据填充
        showTime(holder.tv_date, holder.tv_year, model.getDate());

        Long balance=model.getValue();
        String re;
        if(balance < 0) {
            balance = Math.abs(balance);
            re =balance/100+"."+String.format("%02d",balance%100);
        } else {
            re =balance/100+"."+String.format("%02d",balance%100);
        }

        holder.tv_money.setText("+" + re + "（送" + model.getAct_amount() / 100 + ")");
        holder.tv_type.setText(model.getCharge_channel_zh());

        if (position % 2 == 0) {
            holder.ll_content.setBackgroundColor(mContext.getResources().getColor(R.color.ex_recharge_content));
        } else {
            holder.ll_content.setBackgroundColor(mContext.getResources().getColor(R.color.pub_white));
        }


        return convertView;
    }

    private void showTime(TextView tv_date, TextView tv_year, String date) {
        String clean_date=date.replace(" ","");
        String clean2=clean_date.replace("-", "/");
        String year=clean2.substring(5,10);
        String time=clean2.substring(10);
        tv_year.setText(year);
        tv_date.setText(time);
    }

    class ViewHolder {
        public TextView tv_year;
        public TextView tv_date;
        public TextView tv_money;
        public TextView tv_type;
        public LinearLayout ll_content;
    }


}
