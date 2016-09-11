package com.moge.ebox.phone.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.model.AcitveModel;

public class ActiveAdapter extends BaseListAdapter<AcitveModel> {

	private Context mContext;

	public ActiveAdapter(Context context) {
		mContext = context;
	}

	@Override
	public View getView(final int position, View layout, ViewGroup parent) {

		if (layout == null) {
			layout = View.inflate(mContext, R.layout.item_active, null);
		}
		TextView tv_charge = ViewHolder.get(layout, R.id.tv_charge);
		TextView tv_send = ViewHolder.get(layout, R.id.tv_send);

		AcitveModel model = list.get(position);
		tv_charge.setText(getMoney(model.getAct_value()));
		tv_send.setText(getdetails(model));
		return layout;
	}

	private String getdetails(AcitveModel model) {
		StringBuffer sb = new StringBuffer();
		String s = getMoney(model.getAct_amount());// 送
		sb.append("送").append(s).append("元");
		return sb.toString();
	}

	private String getMoney(Integer act_value) {

		return act_value / 100 + "";
	}

}
