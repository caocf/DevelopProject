package com.ebox.st.ui.lic;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ebox.pub.utils.MGViewUtil;
import com.ebox.st.model.IdentiOrderModel;

import java.util.List;

public class GetFileItemAdapter extends BaseAdapter
	implements OnItemClickListener{
	
	private List<IdentiOrderModel> mList;
	private Activity activity;
	private GetfileItemBar.TimeoutItemBarListener listener;
	
	public GetFileItemAdapter(Activity context) {
		activity = context;
	}
	
	public void setData(List<IdentiOrderModel> list, GetfileItemBar.TimeoutItemBarListener listener) {
		mList = list;
		this.listener = listener;
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
		GetfileItemBar item;
		if (convertView == null) {
			item = new GetfileItemBar(activity);
            MGViewUtil.scaleContentView(item);
		} else {
			item = (GetfileItemBar) convertView;
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
		
		((GetfileItemBar)view).listener.onDealClick(mList.get(position));
	}
}
