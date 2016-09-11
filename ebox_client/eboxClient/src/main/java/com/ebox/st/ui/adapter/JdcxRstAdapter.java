package com.ebox.st.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.st.model.StateModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class JdcxRstAdapter extends BaseListAdapter<StateModel> {

	private Context mContext;

	public JdcxRstAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.st_item_jdcx_rst, null);
            holder.rl_item_jdcx_rst = (RelativeLayout)layout.findViewById(R.id.rl_item_jdcx_rst);
            MGViewUtil.scaleContentView(holder.rl_item_jdcx_rst);
			holder.tv_create_at=(TextView) layout.findViewById(R.id.tv_create_at);
			holder.tv_state=(TextView) layout.findViewById(R.id.tv_state);
			holder.tv_msg=(TextView) layout.findViewById(R.id.tv_msg);
			holder.tv_msg.setVisibility(View.GONE);
			layout.setTag(holder);
		} else {
			holder = (ViewHolder) layout.getTag();
		}
		StateModel model = list.get(position);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date dDate = new Date();
		try {
			dDate = format.parse(model.getCreat_at());
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		String reTime = format.format(dDate);
		holder.tv_create_at.setText(reTime);
		holder.tv_state.setText(FunctionUtil.getCutString(model.getState(),8));
		holder.tv_msg.setText(model.getDesc());
		return layout;
	}


	class ViewHolder {
		TextView tv_create_at;
		TextView tv_state;
		TextView tv_msg;
        RelativeLayout rl_item_jdcx_rst;
	}

}
