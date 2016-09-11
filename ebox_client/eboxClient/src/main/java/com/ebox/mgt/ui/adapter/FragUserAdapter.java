package com.ebox.mgt.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.operator.OperatorInfo;
import com.ebox.ex.network.model.enums.OperatorReserveStatus;
import com.ebox.ex.network.model.enums.OperatorStatus;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

public class FragUserAdapter extends BaseListAdapter<OperatorInfo> {

	private Context mContext;

	public FragUserAdapter(Context context) {
		mContext = context;
	}

	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.mgt_item_user_info, null);
			holder.tv_name = (TextView) layout.findViewById(R.id.tv_name);
			holder.tv_pwd = (TextView) layout.findViewById(R.id.tv_pwd);
			holder.tv_balance = (TextView) layout.findViewById(R.id.tv_balance);
			holder.tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
			holder.tv_state = (TextView) layout.findViewById(R.id.tv_state);
			holder.tv_status=(TextView) layout.findViewById(R.id.tv_status);
			holder.tv_reserve_status=(TextView) layout.findViewById(R.id.tv_reserve_status);
			layout.setTag(holder);
            MGViewUtil.scaleContentView((ViewGroup)layout);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		OperatorInfo info = list.get(position);
		holder.tv_name.setText("用户名:"+info.getOperatorName());
		//holder.tv_pwd.setText(info.getPassword());
		holder.tv_phone.setText("手机号:"+info.getTelephone());
		holder.tv_balance.setText("余额:"+info.getBalance());
		holder.tv_state.setText("CardId:"+info.getCardId());
	//	holder.tv_status.setText(getStatus(info.getOperatorStatus()));
		holder.tv_reserve_status.setText("权限:"+getReserveState(info.getReserveStatus()));
		return layout;
	}

	private CharSequence getReserveState(Integer reserveStatus)
	{
		if (reserveStatus==OperatorReserveStatus.unreserveBox)
		{
			return"不可用预留";
		}else if(reserveStatus==OperatorReserveStatus.reserveBox)
		{
			return "可用预留";
		}
		
		return null;
	}

	private CharSequence getStatus(Integer operatorStatus) {
		if (operatorStatus==OperatorStatus.UnActivate) {
			//return "未激活";
			return "";
		}else if(OperatorStatus.Activate==operatorStatus) {
			//return "激活";
			return "";
		}else if(operatorStatus==OperatorStatus.Blocked){
			return "锁定";
		}
		return null;
	}

	class ViewHolder {
		TextView tv_status;
		TextView tv_name;
		TextView tv_pwd;
		TextView tv_phone;
		TextView tv_balance;
		TextView tv_state;
		TextView tv_reserve_status;
	}

}
