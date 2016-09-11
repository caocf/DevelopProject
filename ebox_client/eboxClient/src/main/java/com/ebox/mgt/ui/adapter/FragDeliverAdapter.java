package com.ebox.mgt.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

public class FragDeliverAdapter extends BaseListAdapter<Deliver> {

	private Context mContext;

	public FragDeliverAdapter(Context context) {
		mContext = context;
	}

	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.ex_item_deliver, null);
			holder.tv_itemId = (TextView) layout.findViewById(R.id.tv_itemId);
			holder.tv_boxId = (TextView) layout.findViewById(R.id.tv_boxId);
			holder.tv_operatorId = (TextView) layout
					.findViewById(R.id.tv_operatorId);
			holder.tv_telephone = (TextView) layout
					.findViewById(R.id.tv_telephone);
			holder.tv_state = (TextView) layout.findViewById(R.id.tv_state);
			holder.tv_fee = (TextView) layout.findViewById(R.id.tv_fee);
			layout.setTag(holder);
            MGViewUtil.scaleContentView((ViewGroup) layout);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		Deliver deliver = list.get(position);
		holder.tv_itemId.setText("运单号：" + deliver.getItemId());
		holder.tv_boxId.setText("箱门:" + deliver.getBox_code());
		holder.tv_operatorId.setText("快递员:" + deliver.getOperatorId());
		holder.tv_telephone.setText("收件人:" + deliver.getTelephone());
		holder.tv_state.setText("状态:" + getState(deliver.getState()));
		holder.tv_fee.setText("费用:" + deliver.getFee());
		return layout;
	}

	private String getState(Integer state) {
		if (state == 0)
		{
			return "生成待同步";
		}
		else if (state == 1)
		{
			return "失败待告警";
		}
		return null;
	}

	class ViewHolder {
		TextView tv_itemId;
		TextView tv_boxId;
		TextView tv_operatorId;
		TextView tv_telephone;
		TextView tv_state;
		TextView tv_fee;
	}

}
