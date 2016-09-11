package com.ebox.ex.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.ui.bar.ItemBar;
import com.ebox.ex.network.model.base.type.TimeOutOrderType;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

public class ItemAdapter extends BaseAdapter 
	implements OnItemClickListener{
	
	private List<OrderInfo> mList;
	private Activity activity;
	
	public ItemAdapter(Activity context) {
		activity = context;
	}
	
	public void setData(List<OrderInfo> list) {
		mList = list;
	};
	
	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ItemBar item;
		if (convertView == null) {
			item = new ItemBar(activity);
            MGViewUtil.scaleContentView(item);
		} else {
			item = (ItemBar) convertView;
		}
	
		item.setData(mList.get(position));
		return item;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == -1 || position >= mList.size()) {
			return;
		}
	}
}
