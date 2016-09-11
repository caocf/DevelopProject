package com.ebox.st.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.st.model.PopulationMemberModel;

public class MemberAdatpter extends BaseListAdapter<PopulationMemberModel> {

	private Context context;
	private onCancelClickListener listener;
	
	public MemberAdatpter(Context context) {
		this.context=context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) 
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.st_member_bar, null);
            holder.rl_st_member_bar =(RelativeLayout) convertView.findViewById(R.id.rl_st_member_bar);
            MGViewUtil.scaleContentView(holder.rl_st_member_bar);
			holder.rl_member=(RelativeLayout) convertView.findViewById(R.id.rl_member);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.iv_cancel=(ImageView) convertView.findViewById(R.id.iv_cancel);
			holder.rl_add=(RelativeLayout) convertView.findViewById(R.id.rl_add);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		
		final PopulationMemberModel info = list.get(position);
		
		if(info.getIsAdd())
		{
			holder.rl_member.setVisibility(View.GONE);
			holder.rl_add.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.rl_member.setVisibility(View.VISIBLE);
			holder.tv_name.setText(info.getPopulation().getIdcard().getName());
			holder.iv_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					switch(v.getId())
					{
					case R.id.iv_cancel:
						listener.OnCancelClickListener(info);
						break;
					}
				}
				
			});
			holder.rl_add.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	public interface onCancelClickListener{
		void OnCancelClickListener(PopulationMemberModel info);
	}
	
	public void setListener(onCancelClickListener listener)
	{
		this.listener = listener;
	}
	
	class ViewHolder{
		RelativeLayout rl_member;
		TextView tv_name;
		ImageView iv_cancel;
		RelativeLayout rl_add;
        RelativeLayout rl_st_member_bar;
	}
};
