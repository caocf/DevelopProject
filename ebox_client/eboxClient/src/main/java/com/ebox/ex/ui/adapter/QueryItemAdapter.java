package com.ebox.ex.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.ui.bar.QueryItemBar;
import com.ebox.ex.ui.bar.QueryItemBar.QueryItemBarListener;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

public class QueryItemAdapter extends BaseAdapter 
	implements OnItemClickListener{
	
	private List<OrderLocalInfo> mList;
	private Activity activity;
	private QueryItemBarListener listener;
	
	public QueryItemAdapter(Activity context) {
		activity = context;
	}
	
	public void setData(List<OrderLocalInfo> list, QueryItemBarListener listener) {
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
		QueryItemBar item;
		if (convertView == null) {
			item = new QueryItemBar(activity);
            MGViewUtil.scaleContentView(item);
		} else {
			item = (QueryItemBar) convertView;
		}
		if ((position+1)%2==0){
			item.ll_item.setBackgroundColor(activity.getResources().getColor(R.color.pub_white));
		}
		item.setData(mList.get(position));
		item.setQueryItemBarListener(listener);
		return item;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		if (position == -1 || position >= mList.size()) {
//			return;
//		}
//
//		((QueryItemBar)view).listener.onDealClick(mList.get(position));
//		((QueryItemBar)view).listener.onVoiceClick(mList.get(position));
	}

	private static class ViewHolder{

	}


}
