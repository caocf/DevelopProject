package com.ebox.ex.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.ui.bar.TimeoutItemBar;
import com.ebox.ex.ui.bar.TimeoutItemBar.TimeoutItemBarListener;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

public class TimeoutItemAdapter extends BaseAdapter 
	implements OnItemClickListener{
	
	private List<OrderLocalInfo> mList;
	private Activity activity;
	private TimeoutItemBarListener listener;
	
	public TimeoutItemAdapter(Activity context) {
		activity = context;
	}
	
	public void setData(List<OrderLocalInfo> list, TimeoutItemBarListener listener) {
		mList = list;
		this.listener = listener;
	}
	
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
		TimeoutItemBar item;
		if (convertView == null) {
			item = new TimeoutItemBar(activity);
            MGViewUtil.scaleContentView(item);
		} else {
			item = (TimeoutItemBar) convertView;
		}
	
		item.setData(mList.get(position));
		item.setTimeoutItemBarListener(listener);
		return item;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == -1 || position >= mList.size()) {
			return;
		}
		
		((TimeoutItemBar)view).listener.onDealClick(mList.get(position));
	}
}
