package com.ebox.mgt.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.database.alarm.Alarm;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

public class FragAlarmAdapter extends BaseListAdapter<Alarm> {

	private Context mContext;

	public FragAlarmAdapter(Context context) {
		mContext = context;
	}

	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.mgt_item_alarm, null);
			holder.tv_dateAdd = (TextView) layout.findViewById(R.id.tv_dateAdd);
			holder.tv_cardno = (TextView) layout.findViewById(R.id.tv_cardno);
			layout.setTag(holder);
            MGViewUtil.scaleContentView((ViewGroup) layout);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		  Alarm alarm = list.get(position);
		String boxId = alarm.getBoxId();
		if (boxId!=null||boxId.equals("")) {
			holder.tv_dateAdd.setText("告警码:" +alarm.getAlarmCode()+" 箱门号:"+boxId);
		}else
		{
			holder.tv_dateAdd.setText("告警码:" +alarm.getAlarmCode());
		}
		holder.tv_cardno.setText("内容:" + alarm.getContent());
		return layout;
	}

	class ViewHolder {
		TextView tv_cardno;
		TextView tv_dateAdd;
	}

}
