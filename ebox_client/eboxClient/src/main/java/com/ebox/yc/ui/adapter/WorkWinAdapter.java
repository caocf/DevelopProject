package com.ebox.yc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.ImageLoaderUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.yc.model.WorkWin;

public class WorkWinAdapter extends BaseListAdapter<WorkWin> {

	private Context mContext;

	public WorkWinAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.yc_item_workwin, null);
			holder.iv_work_win=(ImageView) layout.findViewById(R.id.iv_work_win);
			holder.tv_name=(TextView) layout.findViewById(R.id.tv_name);
			layout.setTag(holder);
			MGViewUtil.scaleContentView((ViewGroup)layout);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		WorkWin model = list.get(position);
		ImageLoaderUtil.instance().displayImage(model.getWinPic(), holder.iv_work_win);
		holder.tv_name.setText(model.getWindowName());
		return layout;
	}


	class ViewHolder {
		ImageView iv_work_win;
		TextView tv_name;
	}

}
