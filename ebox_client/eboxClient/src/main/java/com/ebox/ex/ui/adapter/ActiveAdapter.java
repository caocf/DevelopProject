package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.AcitveType;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

public class ActiveAdapter extends BaseListAdapter<AcitveType> {

	private Context mContext;
	private activeOnItemClickListener ml;

	public ActiveAdapter(Context context) {
		mContext = context;
	}
	public void setListener(activeOnItemClickListener l){
		this.ml=l;
	}

	private int pos=0;

	@Override
	public View getView(final int position, View layout, ViewGroup parent) {
		ViewHolder holder;
		if (layout == null) {
			holder = new ViewHolder();
			layout = View.inflate(mContext, R.layout.ex_item_active, null);
			holder.tv_hint=(TextView) layout.findViewById(R.id.tv_hint);
//			holder.tv_mz=(TextView) layout.findViewById(R.id.tv_mz);
			holder.tv_p=(TextView) layout.findViewById(R.id.tv_p);
			holder.tv_price=(TextView) layout.findViewById(R.id.tv_price);
			holder.rl_details=(RelativeLayout) layout.findViewById(R.id.rl_details);
			layout.setTag(holder);
            //缩放View大小
            MGViewUtil.scaleContentView((ViewGroup) layout);
        } else {
			holder = (ViewHolder) layout.getTag();
		}
		AcitveType model = list.get(position);

		holder.tv_price.setText(getMoney(model.getAct_value()));
		holder.tv_hint.setText(getdetails(model));
		holder.rl_details.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pos=position;
				if (ml!=null)
				{
					ml.OnItemClickListener(pos);
					notifyDataSetChanged();
				}
			}
		});

		if (pos==position)
		{
			layout.setBackgroundResource(R.drawable.ex_bg_recharge_press);
// 			holder.tv_mz.setTextColor(mContext.getResources().getColor(R.color.pub_white));
 			holder.tv_p.setTextColor(mContext.getResources().getColor(R.color.pub_white));
 			holder.tv_price.setTextColor(mContext.getResources().getColor(R.color.pub_white));
		}
		else {
			layout.setBackgroundResource(R.drawable.ex_bg_recharge_normal);
//			holder.tv_mz.setTextColor(mContext.getResources().getColor(R.color.pub_grey));
			holder.tv_p.setTextColor(mContext.getResources().getColor(R.color.pub_grey));
			holder.tv_price.setTextColor(mContext.getResources().getColor(R.color.pub_grey));
		}
		return layout;
	}

	private String getdetails(AcitveType model) {
		StringBuffer sb=new StringBuffer();
		String c=getMoney(model.getAct_value());//充值
		String s=getMoney(model.getAct_amount());//送
		sb.append("充").append(c).append("送").append(s);
		return sb.toString();
	}
	private String getMoney(Integer act_value) {

		return act_value/100+"";
	}

	public interface activeOnItemClickListener{
		void OnItemClickListener(int positon);
	}

	class ViewHolder {
		TextView tv_hint;
//		TextView tv_mz;
		TextView tv_p;
		TextView tv_price;
		RelativeLayout rl_details;
	}

}
