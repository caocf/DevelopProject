package com.moge.ebox.phone.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.model.OrderDetailsModel;

import java.util.List;

public class QueryOrderAdapter extends BaseListAdapter<OrderDetailsModel> {

	private Context mContext;

	public QueryOrderAdapter(Context context) {
		mContext = context;
	}

	public void addData(List<OrderDetailsModel> data) {
		list.addAll(data);
		notifyDataSetChanged();
	}

	public void setData(List<OrderDetailsModel> data) {
		if (list.size() > 0) {
			list.clear();
		}
		list.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_query_time_out,
					null);
		}
		TextView tv_day = ViewHolder.get(convertView, R.id.tv_day);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_order_id = ViewHolder.get(convertView, R.id.tv_order_id);
		TextView tv_phone = ViewHolder.get(convertView, R.id.tv_phone);
		TextView tv_place = ViewHolder.get(convertView, R.id.tv_place);
		OrderDetailsModel orderModel = list.get(position);
		if (list!=null){
			Logger.i("QueryModel"+getDay(orderModel.getDelivery_at()));
			Logger.i("QueryModel"+getTime(orderModel.getDelivery_at()));
			Logger.i("QueryModel"+orderModel.getItem_id());
			Logger.i("QueryModel"+orderModel.getMsisdn());
			Logger.i("QueryModel"+orderModel.getTerminal().terminal_name);


			tv_day.setText(getDay(orderModel.getDelivery_at()));
			tv_time.setText(getTime(orderModel.getDelivery_at()));
			tv_order_id.setText(orderModel.getItem_id());
			tv_phone.setText(orderModel.getMsisdn());
			tv_place.setText(orderModel.getTerminal().terminal_name);
		}else
		{
			Toast.makeText(mContext,"数据为空",Toast.LENGTH_SHORT).show();
		}

		return convertView;
	}

	private CharSequence getDay(String deliver_time) {
		int index = deliver_time.indexOf(" ");
		return deliver_time.substring(0, index);
	}

	private CharSequence getTime(String deliver_time) {
		int index = deliver_time.indexOf(" ");
		return deliver_time.substring(index, deliver_time.length());
	}

}
