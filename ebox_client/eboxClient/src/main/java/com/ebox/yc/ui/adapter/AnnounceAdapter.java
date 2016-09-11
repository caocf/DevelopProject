package com.ebox.yc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.yc.model.Content;

public class AnnounceAdapter extends BaseListAdapter<Content> {

	private Context mContext;

	public AnnounceAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.yc_item_announce, null);
			holder.tv_create_at=(TextView) layout.findViewById(R.id.tv_create_at);
			holder.tv_title=(TextView) layout.findViewById(R.id.tv_title);
			layout.setTag(holder);
			MGViewUtil.scaleContentView((ViewGroup)layout);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		Content model = list.get(position);
		holder.tv_create_at.setText(model.getCreateDate());
		holder.tv_title.setText(model.getTitle());
		return layout;
	}


	class ViewHolder {
		TextView tv_create_at;
		TextView tv_title;
	}

}
