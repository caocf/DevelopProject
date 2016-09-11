package com.ebox.yc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.yc.model.WorkItem;

public class WorkItemsAdapter extends BaseListAdapter<WorkItem> {

	private Context mContext;

	public WorkItemsAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.yc_item_workitem, null);
			holder.tv_name=(TextView) layout.findViewById(R.id.tv_name);
			MGViewUtil.scaleContentView((ViewGroup)layout);
			layout.setTag(holder);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		WorkItem model = list.get(position);
		holder.tv_name.setText("  " + model.getItemName());
		return layout;
	}


	class ViewHolder {
		TextView tv_name;
	}

}
